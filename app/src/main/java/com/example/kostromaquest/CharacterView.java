package com.example.kostromaquest;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

public class CharacterView extends FrameLayout {

    private ImageView characterImage;

    public CharacterView(Context context) {
        super(context);
        init(context);
    }

    public CharacterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CharacterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        characterImage = new ImageView(context);
        characterImage.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        ));
        addView(characterImage);
    }

    // Установить изображение персонажа
    public void setCharacterImage(@DrawableRes int resId) {
        characterImage.setImageResource(resId);
    }

    // Смещение персонажа по горизонтали (от -100 до 100)
    public void setHorizontalOffset(int percentOffset) {
        int maxOffsetPx = (int) (getResources().getDisplayMetrics().widthPixels * 0.25f); // 25% от ширины экрана
        float clamped = Math.max(-100, Math.min(100, percentOffset));
        float shiftPx = (clamped / 100f) * maxOffsetPx;

        characterImage.setTranslationX(shiftPx);
    }

    public void animateOffset(int toOffset) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationX", getTranslationX(), toOffset);
        animator.setDuration(500);
        animator.start();
    }

}
