package com.example.kostromaquest;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogPanel extends FrameLayout {

    private TextView characterNameView;
    private TextView dialogTextView;
    private ImageView backgroundImageView;

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
    }

    // Отображение данных из TextElement
    public void setDialog(TextElement element) {
        if (element != null) {
            characterNameView.setText(element.getCharacterName());
            animateDialogText(element.getText());
        }
    }


    // (Необязательно) Можно менять фон программно
    public void setDialogBackground(int drawableResId) {
        backgroundImageView.setImageResource(drawableResId);
    }
    public void animateDialogText(String fullText) {
        characterNameView.setAlpha(1f); // Имя сразу видно
        dialogTextView.setAlpha(1f);    // Видимость текста включена
        dialogTextView.setText("");     // Очищаем перед началом

        final int[] index = {0};
        final long delay = 40; // скорость: меньше = быстрее

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (index[0] < fullText.length()) {
                    dialogTextView.append(String.valueOf(fullText.charAt(index[0])));
                    index[0]++;
                    handler.postDelayed(this, delay);
                }
            }
        };
        handler.post(runnable);
    }



}
