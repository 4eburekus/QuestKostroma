package com.example.questkostroma;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ImageView backgroundImage;
    private ImageView characterImage;
    private TextView dialogText;
    private MediaPlayer mediaPlayer;
    private TextView speakerName;
    private ConstraintLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundImage = findViewById(R.id.backgroundImage);
        characterImage = findViewById(R.id.characterImage);
        dialogText = findViewById(R.id.dialogText);
        speakerName =  findViewById(R.id.speakerName);
        rootLayout = findViewById(R.id.rootLayout);


        // Создание тестового экрана
        Character character = new Character(R.drawable.hero, Character.CharacterPosition.CENTER);


        // добавление текста в текстовое поле
        DialogText dialog = new DialogText();
        dialog.addText(0, "1персонаж" ,"Привет!");
        dialog.addText(1,  "1персонаж", "Добро пожаловать в игру.");
        dialog.addText(2, "1персонаж", "id=2, тест перемещение позиции обьекта(LEFT)");
        dialog.addText(3, "1персонаж", "id=3, тест перемещение позиции обьекта(CENTER)");
        dialog.addText(4, "1персонаж", "id=4, тест перемещение позиции обьекта(RIGHT)");
        dialog.addText(5, "2персонаж", "id=5, Тест Проверка вместимости строки и возможности переноса строки ............................................................................................");

        BaseScreen screen = new BaseScreen(character, R.drawable.testbackgroungtest, R.raw.music_bg, dialog);



        // Отрисовка интерфейса
        renderScreen(screen);



        // Обработка нажатия для перехода по диалогу
        rootLayout.setOnClickListener(v -> {
            if (screen.getDialogText().nextText()) {
                dialogText.setText(screen.getDialogText().getCurrentText());
                speakerName.setText(screen.getDialogText().getCurrentSpeaker());

                // изменение позиции персонажа
                switch (screen.getDialogText().getCurrentId()) {
                    case 2:
                        character.setCharacterPosition(Character.CharacterPosition.LEFT);
                        break;
                    case 3:
                        character.setCharacterPosition(Character.CharacterPosition.CENTER);
                        break;
                    case 4:
                        character.setCharacterPosition(Character.CharacterPosition.RIGHT);
                        break;
                }

                updateCharacterPosition(character.getCharacterPosition());
            }
        });

    }

    private void renderScreen(BaseScreen screen) {
        // Установка фона
        backgroundImage.setImageResource(screen.getBackgroundResId());

        // Установка изображения персонажа
        characterImage.setImageResource(screen.getCharacter().getImageCharacter());

        // Позиция персонажа (ConstraintLayout.LayoutParams)
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) characterImage.getLayoutParams();

        // Сброс всех старых ограничений
        params.startToStart = ConstraintLayout.LayoutParams.UNSET;
        params.endToEnd = ConstraintLayout.LayoutParams.UNSET;
        params.horizontalBias = 0.5f; // центр по умолчанию

        // Установка позиции
        switch (screen.getCharacter().getCharacterPosition()) {
            case LEFT:
                params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                params.horizontalBias = 0f;
                break;
            case CENTER:
                params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
                params.horizontalBias = 0.5f;
                break;
            case RIGHT:
                params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
                params.horizontalBias = 1f;
                break;
        }

        characterImage.setLayoutParams(params);

        // Установка текста
        dialogText.setText(screen.getDialogText().getCurrentText());
        speakerName.setText(screen.getDialogText().getCurrentSpeaker());

        // Воспроизведение музыки
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, screen.getMusicResId());
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    /// это как renderScreen, то распостраняется только на персонажа
    /// мы не затрагиваем звук и задний фон, только позицию персонажа
    private void updateCharacterPosition(Character.CharacterPosition position) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) characterImage.getLayoutParams();

        // Удаляем старые ограничения
        params.startToStart = ConstraintLayout.LayoutParams.UNSET;
        params.endToEnd = ConstraintLayout.LayoutParams.UNSET;
        params.leftToLeft = ConstraintLayout.LayoutParams.UNSET;
        params.rightToRight = ConstraintLayout.LayoutParams.UNSET;
        params.horizontalBias = 0.5f; // по центру по умолчанию

        // Задаём новые ограничения
        switch (position) {
            case LEFT:
                params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                params.horizontalBias = 0f;
                break;
            case CENTER:
                params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
                params.horizontalBias = 0.5f;
                break;
            case RIGHT:
                params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
                params.horizontalBias = 1f;
                break;
        }

        characterImage.setLayoutParams(params);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }



}
