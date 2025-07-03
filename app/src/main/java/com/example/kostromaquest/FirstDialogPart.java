package com.example.kostromaquest;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class FirstDialogPart extends AppCompatActivity {
    private DialogPanel dialogPanel;
    private DialogArray dialogArray;
    private Button backButton;
    private MediaPlayer mediaPlayer;
    private MediaPlayer dog;
    private CharacterView characterView;  // поле класса
    private CharacterView yeenImage;      // поле класса
    private View dimOverlay; // поле черного экрана
    private ImageView backgroundImage;

    //private  boolean spaceMinimaze; // проверка на сокращение растояния

    private  boolean dialoPanelIsHide = false; // true если блокируется и прячится диалог, иначе false

    private boolean miniGameisActiv = false;// true если игра была активирована
    private boolean secondMiniGameisActiv = false;// true если игра была активирована

    private boolean dialogIsEnabled = true; // false - если диалог отключен

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_dialog_part);

         characterView = findViewById(R.id.character_view);
         yeenImage = findViewById(R.id.yeen);
        backButton = findViewById(R.id.back_button);

        // Инициализация музыки
        mediaPlayer = MediaPlayer.create(this, R.raw.heroicmusic);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        ////////////////////////////////////// пока не пригодится //////////////////////////////////////
// Задать картинку
        //  characterView.setCharacterImage(R.drawable.pngegg);
        //yeenImage.setCharacterImage(R.drawable.pustaia);
// Сдвинуть влево (пример: -150 = левый край, 0 = центр, 150 = правый край)
        //characterView.setHorizontalOffset(-150); // влево
        //yeenImage.setHorizontalOffset(+150);

        ////////////////////////////////////// пока не пригодится //////////////////////////////////////

        dialogPanel = findViewById(R.id.dialog_panel);
        backButton = findViewById(R.id.back_button);

        backgroundImage = findViewById(R.id.background_image);

        // Здесь будет логика диалога

        setupDialogArray();
        showCurrentDialog();

        // Пролистывание вперёд по нажатию на диалог
        dialogPanel.setOnClickListener(v -> {

            if (dialogPanel.isAnimating()) {
                dialogPanel.finishTypingImmediately();
            } else {
                dialogArray.next();
                showCurrentDialog();
            }


        });




        // Кнопка назад
        backButton.setOnClickListener(v -> {
            dialogArray.previous();
            showCurrentDialog();
        });


    }

    private void fadeImageTransition(int newImageResId, int durationMillis) {
        Drawable currentDrawable = backgroundImage.getDrawable();
        Drawable newDrawable = ContextCompat.getDrawable(this, newImageResId);

        if (currentDrawable == null) {
            backgroundImage.setImageDrawable(newDrawable);
            return;
        }

        Drawable[] layers = new Drawable[]{currentDrawable, newDrawable};
        TransitionDrawable transitionDrawable = new TransitionDrawable(layers);
        backgroundImage.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(durationMillis);
    }

    /**
     * Полное анимированное затемнение на заданное количество секунд.
     * @param seconds сколько секунд держать затемнение
     */
    private void animateFullDim(float seconds) {
        if (dimOverlay == null) {
            dimOverlay = findViewById(R.id.dim_overlay);
        }

        dimOverlay.clearAnimation();
        dimOverlay.setAlpha(0f);
        dimOverlay.setVisibility(View.VISIBLE);

        // Анимация появления
        dimOverlay.animate()
                .alpha(1f)
                .setDuration(500)  // Длительность затемнения (мс)
                .withEndAction(() -> {
                    // После полной заливки ждём указанное время
                    dimOverlay.postDelayed(() -> {
                        // Анимация исчезновения
                        dimOverlay.animate()
                                .alpha(0f)
                                .setDuration(500)
                                .withEndAction(() -> {
                                    dimOverlay.setVisibility(View.GONE);

                                    // Переход к следующему диалогу
                                    dialogArray.next();
                                    showCurrentDialog();
                                })
                                .start();
                    }, (long) (seconds * 1000));
                })
                .start();
    }

    //
    //ДИалоги
    //
    private void setupDialogArray() {
        dialogArray = new DialogArray();

        dialogArray.addTextElement(new TextElement(0, " ", " Кострома – город, где каждый третий дом был деревянным, а каждый второй пожар – смертельным."));
        dialogArray.addTextElement(new TextElement(1, " ", "Но там, где люди бежали прочь, они шли вперёд – пожарные. А впереди всех мчался Бобка, рыжий великан, спасавший детей из огня…"));
        dialogArray.addTextElement(new TextElement(2, " ", " ")); // экран собаки
        dialogArray.addTextElement(new TextElement(3," "," ")); // для экрана с затемнением
        dialogArray.addTextElement(new TextElement(4," ","Ветер с востока… Сухо. Будет пожар – чувствую"));
        dialogArray.addTextElement(new TextElement(5," ","Привет, Бобка, как дела? "));
        dialogArray.addTextElement(new TextElement(6,"Бобка ","Гав-гав!"));
        dialogArray.addTextElement(new TextElement(7," ","")); // кабинет начальника
        dialogArray.addTextElement(new TextElement(8," ","Товарищ начальник, дежурный прибыл."));
        dialogArray.addTextElement(new TextElement(9,"Начальник","Проверишь команду, снаряжение и на площадку. "));
        dialogArray.addTextElement(new TextElement(10,"Начальник","И смотри в оба! Ветер сегодня сильный и погода сухая."));
        dialogArray.addTextElement(new TextElement(11,"","Есть"));
        dialogArray.addTextElement(new TextElement(12,"",""));
        dialogArray.addTextElement(new TextElement(13,"","Кострома… Как всегда красива ты…"));
        dialogArray.addTextElement(new TextElement(14,"","Нашел чертягу! Тревога!")); // бывший 15
        dialogArray.addTextElement(new TextElement(15,""," Бью в колокол!")); // бывший 16
        dialogArray.addTextElement(new TextElement(16,"","")); // бывший 17
        dialogArray.addTextElement(new TextElement(17,"","Верховой, немедленно отправляйся на место пожара!"));
        dialogArray.addTextElement(new TextElement(18,"Верховой","Есть"));
        dialogArray.addTextElement(new TextElement(19," ","Самое время экипироваться."));
        dialogArray.addTextElement(new TextElement(20,"Ствольщик ","Дежурный все готовы!")); // миниигра
        dialogArray.addTextElement(new TextElement(21," ","Готов, отправляемся!")); // бывший 23
        dialogArray.addTextElement(new TextElement(22," ","Брандмейстер, труби, обозначай что мы выехали!")); // бывший 24
        dialogArray.addTextElement(new TextElement(23,"Брандмейстер","Ту-дуууууу!")); // бывший 25
        dialogArray.addTextElement(new TextElement(24,"Бобка","Гав-гав!!")); // бывший 26
        dialogArray.addTextElement(new TextElement(25,"Лошади","Иго-го-го-го-го!")); // бывший 27
        dialogArray.addTextElement(new TextElement(26,"","")); // бывший 28 Мини-игра №3: «Потуши пожар»
        dialogArray.addTextElement(new TextElement(27,"","Пожарный расчет прибыл!")); //return dialog
        dialogArray.addTextElement(new TextElement(28,"Бобка","Гав-гав!")); //
        dialogArray.addTextElement(new TextElement(29,"Рассказчик","Бобка устремляется в глубь горящего здания.")); //
        dialogArray.addTextElement(new TextElement(30,"","Бобка, ты свою работу знаешь.")); //
        dialogArray.addTextElement(new TextElement(31,"","За работу мужики!")); //
    }


    public TextElement getCurrentTextElement() {
        return dialogArray.getCurrentElement();
    }

    private void showCurrentDialog() {
        TextElement current = dialogArray.getCurrentElement();
        if (current != null) {
            dialogPanel.setDialog(current);

        }


// Обновляем картинку персонажа в зависимости от индекса диалога
        // Обновляем картинку персонажа в зависимости от индекса диалога
        switch (dialogArray.getCurrentIndex()) {
            case 0:
                break;
            case 1:
                if (dog != null) dog.release();
                if (!dialogIsEnabled) dialogPanel.setAlpha(1f);
                dialogPanel.show();
                backgroundImage.setImageResource(R.drawable.frame_1);
                break;
            case 2:
                dialogPanel.setAlpha(0f);
                dialogIsEnabled = false;
                fadeImageTransition(R.drawable.frame_2, 500);
                if (dog != null) dog.release();
                dog = MediaPlayer.create(this, R.raw.lay_sobaki);
                dog.setLooping(true);
                dog.start();
                break;
            case 3:
                animateFullDim(3);
                if (dog != null) {
                    dog.release();
                    dog = null;
                }
                break;
            case 4:
                fadeImageTransition(R.drawable.frame_3, 500);
                dialogPanel.setAlpha(1f);

                mediaPlayer = MediaPlayer.create(this, R.raw.calm_music);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();

                if (dog != null) {
                    dog.release();
                    dog = null;
                }
                break;
            case 5:
                fadeImageTransition(R.drawable.frame_4, 500);
                if (dog != null) dog.release();
                dog = MediaPlayer.create(this, R.raw.dog_panting_6876);
                dog.setLooping(true);
                dog.start();
                break;
            case 6:
                if (dog != null) dog.release();
                if (!dialogIsEnabled) dialogPanel.setAlpha(1f);
                dog = MediaPlayer.create(this, R.raw.lay_sobaki);
                dog.setLooping(true);
                dog.start();
                break;
            case 7:
                dialogPanel.setAlpha(0f);
                animateFullDim(1);
                if (dog != null) {
                    dog.release();
                    dog = null;
                }
                break;
            case 8:
                dialogPanel.setAlpha(1f);
                backgroundImage.setImageResource(R.drawable.frame_5);
                break;
            case 9:
                fadeImageTransition(R.drawable.frame_6, 500);
                break;
            case 10:
                break;
            case 11:
                if (!dialogIsEnabled) dialogPanel.setAlpha(1f);
                break;
            case 12:
                fadeImageTransition(R.drawable.panorama, 500);
                dialogPanel.setAlpha(0f);
                break;
            case 13:
                if (!dialogIsEnabled) dialogPanel.setAlpha(1f);
                dialogPanel.setAlpha(1f);
                break;
            case 14: // бывший 15 — мини-игра + фраза "Нашел чертягу! Тревога!"
                if (dog != null) {
                    dog.release();
                    dog = null;
                }
                dialogPanel.setAlpha(1f);
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                mediaPlayer = MediaPlayer.create(this, R.raw.music_is_tense_2);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();

                if (!miniGameisActiv) {
                    Intent intent = new Intent(this, SmokeMiniGameActivity.class);
                    startActivity(intent);
                    miniGameisActiv = true;
                }
                break;
            case 15:
                if (dog != null) dog.release();
                dog = MediaPlayer.create(this, R.raw.ring);
                dog.setLooping(true);
                dog.start();
                if (!dialogIsEnabled) dialogPanel.setAlpha(1f);
                break;
            case 16:
                dialogPanel.setAlpha(0f);
                fadeImageTransition(R.drawable.frame_8_10, 500);
                break;
            case 17:
                if (dog != null) dog.release();
                dialogPanel.setAlpha(1f);
                break;
            case 18:
                if (dog != null) dog.release();
                dog = MediaPlayer.create(this, R.raw.hourse_nouse);
                dog.setLooping(true);
                dog.start();
                break;
            case 19:
                if (dog != null) dog.release();
                break;
            case 20: // бывший 21
                dialogPanel.setAlpha(1f);
                if (!secondMiniGameisActiv) {
                    Intent intent = new Intent(this, CollectItemsActivity.class);
                    startActivity(intent);
                    secondMiniGameisActiv = true;
                }
                break;
            case 21: // бывший 23
                fadeImageTransition(R.drawable.frame_8_10, 500);
                break;
            case 22:
                break;
            case 23:
                if (dog != null) dog.release();
                dog = MediaPlayer.create(this, R.raw.truba);
                dog.setLooping(true);
                dog.start();
                break;
            case 24:
                if (dog != null) dog.release();
                dog = MediaPlayer.create(this, R.raw.lay_sobaki);
                dog.setLooping(true);
                dog.start();
                break;
            case 25:
                animateFullDim(2);
                if (dog != null) dog.release();
                dog = MediaPlayer.create(this, R.raw.hourse_nouse);
                dog.setLooping(true);
                dog.start();
                break;
            case 26:
                mediaPlayer = MediaPlayer.create(this, R.raw.music_is_tense_1);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();

                fadeImageTransition(R.drawable.frame_11, 500);
                break;
            case 27:

                break;
            case 28:
                mediaPlayer = MediaPlayer.create(this, R.raw.lay_sobaki);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                break;

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
        if (dog != null) {
            dog.release();
            dog = null;
        }
    }




}
