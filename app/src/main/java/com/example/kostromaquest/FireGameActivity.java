package com.example.kostromaquest;

import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FireGameActivity extends AppCompatActivity{

    private ImageView buildingImageView;
    private List<ImageButton> fireButtons = new ArrayList<>();

    private TextView timerTextView;

    private CountDownTimer gameTimer;
    private final long GAME_DURATION = 30 * 1000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_game);

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

        buildingImageView = findViewById(R.id.buildingImageView);

        timerTextView = findViewById(R.id.timerTextView);



        // Add fire ImageButtons to the list.
        fireButtons.add(findViewById(R.id.fireButton1));
        fireButtons.add(findViewById(R.id.fireButton2));
        fireButtons.add(findViewById(R.id.fireButton3));
        fireButtons.add(findViewById(R.id.fireButton4));
        // Add more fire buttons as necessary

        // Set OnClickListeners for each fire button
        for (ImageButton button : fireButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    extinguishFire((ImageButton) v);
                }
            });
        }

        startGameTimer();
    }

    private void extinguishFire(ImageButton fireButton) {
        // Make the fire disappear
        fireButton.setVisibility(View.INVISIBLE);

        // Check if all fires are extinguished.
        if (areAllFiresExtinguished()) {
            stopGameTimer();
            showGameWonDialog();
        }
    }

    private boolean areAllFiresExtinguished() {
        for (ImageButton button : fireButtons) {
            if (button.getVisibility() == View.VISIBLE) {
                return false;
            }
        }
        return true;
    }

    private void startGameTimer() {
        gameTimer = new CountDownTimer(GAME_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                timerTextView.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                timerTextView.setText("00:00");
                showGameLostDialog();
            }
        }.start();
    }



    private void stopGameTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer = null;
        }
    }

    private void showGameLostDialog() {
        stopGameTimer();
        new AlertDialog.Builder(this)
                .setTitle("Время вышло!")
                .setMessage("Вы не успели потушить пожар!")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    restartGame();
                })
                .setCancelable(false)
                .show();
    }

    private void showGameWonDialog() {
        stopGameTimer();
        new AlertDialog.Builder(this)
                .setTitle("Вы победили!")
                .setMessage("Вы успешно потушили все пожары!")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    finish();

                })
                .setCancelable(false)
                .show();
    }

    private void restartGame() {
        stopGameTimer();
        for (ImageButton button : fireButtons)
        {
            button.setVisibility(View.VISIBLE);
        }
        startGameTimer();
    }
}
