package com.example.kostromaquest;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.HorizontalScrollView;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class SmokeMiniGameActivity extends Activity implements View.OnTouchListener {

    private ImageView panoramaImage;
    private TextView messageText;
    private HorizontalScrollView scrollView;

    private final float SMOKE_X = 215;
    private final float SMOKE_Y = 180;
    private final float THRESHOLD = 110;
    private final float HIGHLIGHT_RADIUS = 100;

    private boolean smokeFound = false;

    private Bitmap originalBitmap;
    private Bitmap highlightedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoke_game); // Новый layout

        panoramaImage = findViewById(R.id.panorama_image);
        messageText = findViewById(R.id.message_text);
        scrollView = findViewById(R.id.scroll_view);

        panoramaImage.setOnTouchListener(this);
        originalBitmap = ((BitmapDrawable) panoramaImage.getDrawable()).getBitmap();
        highlightedBitmap = originalBitmap.copy(originalBitmap.getConfig(), true);
        panoramaImage.setImageBitmap(highlightedBitmap);


        // Начало убирания шторки

        // Блокируем вертикальную ориентацию
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Делаем так, чтобы контент приложения растягивался под системные панели
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Получаем контроллер для управления системными панелями
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }

        // Скрываем строку состояния
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars());

        // Опционально: скрываем навигационную панель
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());

        // Устанавливаем поведение: системные панели появятся при свайпе от края
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        // Конец убирания шторки

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (smokeFound) return true;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            int scrollX = scrollView.getScrollX();
            float adjustedX = x + scrollX;

            if (isWithinThreshold(adjustedX, y, SMOKE_X, SMOKE_Y, THRESHOLD)) {
                smokeFound = true;
                messageText.setText("Вы нашли дым!");
                highlightArea(SMOKE_X - 90, SMOKE_Y - 30, HIGHLIGHT_RADIUS);

                // Возврат в основную Activity через 2 сек
                panoramaImage.postDelayed(() -> finish(), 2000);
                return true;
            }
        }
        return false;
    }

    private boolean isWithinThreshold(float x, float y, float centerX, float centerY, float threshold) {
        return Math.abs(x - centerX) <= threshold && Math.abs(y - centerY) <= threshold;
    }

    private void highlightArea(float centerX, float centerY, float radius) {
        Canvas canvas = new Canvas(highlightedBitmap);
        Paint paint = new Paint();

        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[]{
                1.2f, 0, 0, 0, -20f,
                0, 1.2f, 0, 0, -20f,
                0, 0, 1.2f, 0, -20f,
                0, 0, 0, 1, 0
        });

        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        paint.setAlpha(80);
        canvas.drawCircle(centerX, centerY, radius, paint);
        panoramaImage.setImageBitmap(highlightedBitmap);
    }
}
