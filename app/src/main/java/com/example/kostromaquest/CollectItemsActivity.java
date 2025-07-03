package com.example.kostromaquest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;

public class CollectItemsActivity extends AppCompatActivity {

    // UI элементы
    private ImageView backgroundImageView;
    private TextView questionTextView;
    private ImageView option1ImageView;
    private ImageView option2ImageView;
    private Button confirmButton;
    private Button cancelButton;
    //   private TextView resultTextView;

    // Игровые переменные
    private List<Question> questions;
    private int currentQuestionIndex;
    private boolean isAnswered;
    private int score;
    private int selectedOptionIndex = -1; // Track selected option
    private boolean isTimerRunning;

    // Таймер для вопроса и для всей игры
    private CountDownTimer questionTimer;
    private CountDownTimer gameTimer;
    private final long QUESTION_TIMEOUT = 2000; // 2 seconds
    private final long GAME_TIMEOUT = 60000;  // 1 minute
    private TextView option1DescriptionTextView;
    private TextView option2DescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_collect_items);

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


        // Блокируем вертикальную ориентацию
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Инициализация UI элементов
        backgroundImageView = findViewById(R.id.backgroundImageView);
        questionTextView = findViewById(R.id.questionTextView);
        option1ImageView = findViewById(R.id.option1ImageView);
        option2ImageView = findViewById(R.id.option2ImageView);
        confirmButton = findViewById(R.id.confirmButton);
        cancelButton = findViewById(R.id.cancelButton);
        //resultTextView = findViewById(R.id.resultTextView);

        // Инициализируем TextView для описания
        option1DescriptionTextView = findViewById(R.id.option1DescriptionTextView);
        option2DescriptionTextView = findViewById(R.id.option2DescriptionTextView);

        // Инициализация игровых переменных
        questions = createQuestions();
        currentQuestionIndex = 0;
        isAnswered = false;
        score = 0;
        isTimerRunning = false; // Add this

        // Загрузка фона
        backgroundImageView.setImageResource(R.drawable.table_background);

        // Отображение первого вопроса
        displayQuestion();

        // Обработчики событий
        option1ImageView.setOnClickListener(v -> {
            onOptionSelected(0);
        });
        option2ImageView.setOnClickListener(v -> {
            onOptionSelected(1);
        });

        confirmButton.setOnClickListener(v -> {
            onConfirmButtonClick();
        });

        cancelButton.setOnClickListener(v -> {
            onCancelButtonClick();
        });

        // Инициализация таймера для игры
        startGameTimer();
    }

    // Класс для представления вопроса
    static class Question
    {
        String questionText;
        int correctOptionIndex;
        int image1ResId;
        int image2ResId;
        String option1Description; // Add this
        String option2Description; // Add this

        public Question(String questionText, int correctOptionIndex, int image1ResId, int image2ResId, String option1Description, String option2Description) {
            this.questionText = questionText;
            this.correctOptionIndex = correctOptionIndex;
            this.image1ResId = image1ResId;
            this.image2ResId = image2ResId;
            this.option1Description = option1Description;
            this.option2Description = option2Description;
        }
    }


    // Функция для создания вопросов
    private List<Question> createQuestions() {
        List<Question> questionList = new ArrayList<>();
        questionList.add(new Question("У вас ровно минута на сбор", 0, R.drawable.topor, R.drawable.sovok, "Топор для рубки дерева", "Совок для уборки мусора"));
        questionList.add(new Question("У вас ровно минута на сбор", 1, R.drawable.grabli, R.drawable.kaska, "Грабли для садоводства", "Каска для защиты головы"));
        questionList.add(new Question("У вас ровно минута на сбор", 0, R.drawable.rukavici, R.drawable.doska1, "Рукавицы для работы", "Доска из дерева"));
        return questionList;
    }


    // Отображение вопроса и вариантов ответа
    private void displayQuestion() {
        resetOptionStyles(); // Ensure styles are reset

        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            questionTextView.setText(question.questionText);
            option1ImageView.setImageResource(question.image1ResId);
            option2ImageView.setImageResource(question.image2ResId);
            option1DescriptionTextView.setText(question.option1Description); // Set description 1
            option2DescriptionTextView.setText(question.option2Description); // Set description 2
            //resultTextView.setText("Выберите необходимый предмет");
            isAnswered = false;
            selectedOptionIndex = -1; // Reset selected option

            startQuestionTimer(); // Start the question timer

        } else {
            stopGameTimer(); // Stop the game timer
            questionTextView.setText("Сбор окончен!");
            option1ImageView.setVisibility(View.GONE);
            option2ImageView.setVisibility(View.GONE);
            option1DescriptionTextView.setVisibility(View.GONE);
            option2DescriptionTextView.setVisibility(View.GONE);

            confirmButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            finish();
        }
    }

    // Method for selecting option
    private void onOptionSelected(int optionIndex) {
        if (isAnswered) return;  // Prevent double clicks
        resetOptionStyles();

        selectedOptionIndex = optionIndex;

        if (optionIndex == 0) {
            option1ImageView.setBackgroundResource(R.drawable.selected_border);
        } else {
            option2ImageView.setBackgroundResource(R.drawable.selected_border);
        }
    }


    // Метод для кнопки "Подтвердить"
    private void onConfirmButtonClick() {
        if (isAnswered) return; // Prevent double clicks
        if (selectedOptionIndex == -1){
            Toast.makeText(this, "Выберите вариант!", Toast.LENGTH_SHORT).show();
            return;
        }

        isAnswered = true;
        stopQuestionTimer(); // Stop the question timer

        Question currentQuestion = questions.get(currentQuestionIndex);

        if (selectedOptionIndex == currentQuestion.correctOptionIndex) {
            score++;
            //resultTextView.setText("Правильно!");
            // Move to the next question
            currentQuestionIndex++;
            displayQuestion();
        } else {
            //resultTextView.setText("Неверно!");
            highlightWrongAnswer(); //Highlight incorrect answer

            // Reset the question after a delay
            new android.os.Handler().postDelayed(() ->
            {
                resetQuestion();
            }, QUESTION_TIMEOUT);
        }
    }

    //Метод для кнопки "Отменить"
    private void onCancelButtonClick() {
        resetOptionStyles();
        selectedOptionIndex = -1;
    }

    // Метод подсветки неправильного ответа
    private void highlightWrongAnswer() {
        if (selectedOptionIndex == 0) {
            option1ImageView.setBackgroundResource(R.drawable.wrong_border);
        } else {
            option2ImageView.setBackgroundResource(R.drawable.wrong_border);
        }
    }


    // Метод для сброса вопроса
    private void resetQuestion() {
        resetOptionStyles();
        displayQuestion();
    }

    // Метод сброса стилей вариантов ответа
    private void resetOptionStyles() {
        option1ImageView.setBackground(null);
        option2ImageView.setBackground(null);
        selectedOptionIndex = -1;
    }


    // Метод инициализации таймера для вопроса
    private void startQuestionTimer() {
        questionTimer = new CountDownTimer(QUESTION_TIMEOUT, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished) {
                //You can update UI here, if needed
            }

            @Override
            public void onFinish()
            {
                // Time's up, reset question
                resetQuestion();
            }
        };
        questionTimer.start();
    }

    // Метод остановки таймера для вопроса
    private void stopQuestionTimer() {
        if (questionTimer != null) {
            questionTimer.cancel();
            questionTimer = null;
        }
    }

    // Метод инициализации таймера для игры
    private void startGameTimer() {
        isTimerRunning = true;
        gameTimer = new CountDownTimer(GAME_TIMEOUT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // You can update UI here
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                // Show AlertDialog
                new AlertDialog.Builder(CollectItemsActivity.this)
                        .setTitle("Время вышло!")
                        .setMessage("Попробуйте еще раз.")
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            restartGame(); //Restart
                            dialog.dismiss(); //Dismiss Dialog
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert) // Optional icon
                        .setCancelable(false)
                        .show();
            }
        }.start();
    }

    // Метод остановки таймера для игры
    private void stopGameTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer = null;
        }
    }

    // Метод перезапуска игры
    private void restartGame() {
        stopGameTimer();
        score = 0;
        currentQuestionIndex = 0;
        displayQuestion();
        startGameTimer();
    }
}
