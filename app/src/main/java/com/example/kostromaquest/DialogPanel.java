package com.example.kostromaquest;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogPanel extends FrameLayout {

    private TextView characterNameView;
    private TextView dialogTextView;
    private ImageView backgroundImageView;

    private Handler handler;
    private Runnable textRunnable;
    private boolean isAnimating = false;

    public DialogPanel(Context context) {
        super(context);
        init(context);
    }

    public DialogPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DialogPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.dialog_panel, this, true);
        characterNameView = findViewById(R.id.character_name);
        dialogTextView = findViewById(R.id.dialog_text);
        backgroundImageView = findViewById(R.id.dialog_background);

        handler = new Handler();
    }

    public void setDialog(TextElement element) {
        if (element != null) {
            characterNameView.setText(element.getCharacterName());

            // Если сейчас идёт анимация, прерываем её
            if (isAnimating) {
                finishTypingImmediately(); // мгновенно показать весь текст
            }

            animateDialogText(element.getText());
        }
    }

    public void setDialogBackground(int drawableResId) {
        backgroundImageView.setImageResource(drawableResId);
    }

    private void animateDialogText(String fullText) {
        dialogTextView.setAlpha(1f);
        dialogTextView.setText("");

        final int[] index = {0};
        final long delay = 30;
        isAnimating = true;

        textRunnable = new Runnable() {
            @Override
            public void run() {
                if (index[0] < fullText.length()) {
                    dialogTextView.append(String.valueOf(fullText.charAt(index[0])));
                    index[0]++;
                    handler.postDelayed(this, delay);
                } else {
                    isAnimating = false;
                }
            }
        };
        handler.post(textRunnable);
    }

    public void finishTypingImmediately() {
        if (textRunnable != null) {
            handler.removeCallbacks(textRunnable);
        }

        // Показываем весь текст сразу
        TextElement current = ((FirstDialogPart) getContext()).getCurrentTextElement();
        if (current != null) {
            dialogTextView.setText(current.getText());
        }

        isAnimating = false;
    }

    ///сокрытие интерфейса диалогового окна
    public void hide() {
        this.setVisibility(View.GONE);
    }
    ///возвращение интерфейса диалогового окна
    public void show() {
        this.setVisibility(View.VISIBLE);
    }

    public boolean isAnimating() {
        return isAnimating;
    }
}




