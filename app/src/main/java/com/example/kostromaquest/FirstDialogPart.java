package com.example.kostromaquest;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FirstDialogPart extends AppCompatActivity {
    private DialogPanel dialogPanel;
    private DialogArray dialogArray;
    private Button backButton;
    private MediaPlayer mediaPlayer;
    private CharacterView characterView;  // поле класса
    private CharacterView yeenImage;      // поле класса

    private  boolean spaceMinimaze; // проверка на сокращение растояния

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_dialog_part);

         characterView = findViewById(R.id.character_view);
         yeenImage = findViewById(R.id.yeen);
        backButton = findViewById(R.id.back_button);

        // Инициализация музыки
        mediaPlayer = MediaPlayer.create(this, R.raw.whiteorchards);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

// Задать картинку
        characterView.setCharacterImage(R.drawable.pngegg);
        yeenImage.setCharacterImage(R.drawable.pustaia);
// Сдвинуть влево (пример: -150 = левый край, 0 = центр, 150 = правый край)
        characterView.setHorizontalOffset(-150); // влево
        yeenImage.setHorizontalOffset(+150);

        dialogPanel = findViewById(R.id.dialog_panel);
        backButton = findViewById(R.id.back_button);

        // Здесь будет логика диалога

        setupDialogArray();
        showCurrentDialog();

        // Пролистывание вперёд по нажатию на диалог
        dialogPanel.setOnClickListener(v -> {
            dialogArray.next();
            showCurrentDialog();
        });

        // Кнопка назад
        backButton.setOnClickListener(v -> {
            dialogArray.previous();
            showCurrentDialog();
        });

        if(dialogArray.getCurrentIndex()==1){
            yeenImage.setCharacterImage(R.drawable.yennefer);
        }
    }

    private void setupDialogArray() {
        dialogArray = new DialogArray();

        dialogArray.addTextElement(new TextElement(0, "Геральт", "йеен, не хочешь сыграть в гвинт?"));
        dialogArray.addTextElement(new TextElement(1, "Йенефер", "не против"));
        dialogArray.addTextElement(new TextElement(2, "Геральт", "тогда начнем"));

        // и т.д.
    }

    private void showCurrentDialog() {
        TextElement current = dialogArray.getCurrentElement();
        if (current != null) {
            dialogPanel.setDialog(current);
        }


// Обновляем картинку персонажа в зависимости от индекса диалога
        switch (dialogArray.getCurrentIndex()) {
            case 0:
                yeenImage.setCharacterImage(R.drawable.pustaia);
                break;
            case 1:
                yeenImage.setCharacterImage(R.drawable.yennefer);
                if(spaceMinimaze == true){
                    spaceMinimaze = false;
                    characterView.animateOffset(-150);
                    yeenImage.animateOffset(150);
                }
                break;
            case 2:
                characterView.animateOffset(150);
                yeenImage.animateOffset(-150);
                spaceMinimaze = true;

        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }




}
