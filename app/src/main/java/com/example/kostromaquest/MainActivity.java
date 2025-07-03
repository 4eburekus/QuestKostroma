package com.example.kostromaquest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ImageButton startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FirstDialogPart.class);
            startActivity(intent);
        });

    }
}