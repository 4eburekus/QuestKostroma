package com.example.kostromaquest;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.kostromaquest.R;

import java.util.*;

public class CollectItemsFragment extends Fragment {

    private ImageView backgroundImageView, option1ImageView, option2ImageView;
    private TextView questionTextView, resultTextView;
    private Button confirmButton, cancelButton;

    private List<Question> questions;
    private int currentQuestionIndex;
    private boolean isAnswered;
    private int selectedOptionIndex = -1;
    private CountDownTimer questionTimer, gameTimer;
    private final long QUESTION_TIMEOUT = 5000;
    private final long GAME_TIMEOUT = 60000;

    static class Question {
        String questionText;
        int correctOptionIndex;
        int image1ResId;
        int image2ResId;

        public Question(String questionText, int correctOptionIndex, int image1ResId, int image2ResId) {
            this.questionText = questionText;
            this.correctOptionIndex = correctOptionIndex;
            this.image1ResId = image1ResId;
            this.image2ResId = image2ResId;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collect_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        backgroundImageView = view.findViewById(R.id.backgroundImageView);
        questionTextView = view.findViewById(R.id.questionTextView);
        resultTextView = view.findViewById(R.id.resultTextView);
        option1ImageView = view.findViewById(R.id.option1ImageView);
        option2ImageView = view.findViewById(R.id.option2ImageView);
        confirmButton = view.findViewById(R.id.confirmButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        questions = createQuestions();
        currentQuestionIndex = 0;
        isAnswered = false;

        backgroundImageView.setImageResource(R.drawable.table_background);

        displayQuestion();

        option1ImageView.setOnClickListener(v -> onOptionSelected(0));
        option2ImageView.setOnClickListener(v -> onOptionSelected(1));
        confirmButton.setOnClickListener(v -> onConfirmButtonClick());
        cancelButton.setOnClickListener(v -> onCancelButtonClick());

        startGameTimer();
    }

    private List<Question> createQuestions() {
        List<Question> questionList = new ArrayList<>();
        questionList.add(new Question("У вас ровно минута на сбор", 0, R.drawable.topor, R.drawable.sovok));
        questionList.add(new Question("У вас ровно минута на сбор", 1, R.drawable.grabli, R.drawable.kaska));
        questionList.add(new Question("У вас ровно минута на сбор", 0, R.drawable.rukavici, R.drawable.doska1));
        return questionList;
    }

    private void displayQuestion() {
        resetOptionStyles();
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            questionTextView.setText(question.questionText);
            option1ImageView.setImageResource(question.image1ResId);
            option2ImageView.setImageResource(question.image2ResId);
            resultTextView.setText("Выберите необходимый предмет");
            isAnswered = false;
            selectedOptionIndex = -1;
            startQuestionTimer();
        } else {
            stopGameTimer();
            questionTextView.setText("Сбор окончен!");
            option1ImageView.setVisibility(View.GONE);
            option2ImageView.setVisibility(View.GONE);
            confirmButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            new android.os.Handler().postDelayed(() -> {
                requireActivity().finish(); // Закрыть мини-игру и вернуться назад
            }, 1500);

        }
    }

    private void onOptionSelected(int optionIndex) {
        if (isAnswered) return;
        resetOptionStyles();
        selectedOptionIndex = optionIndex;
        if (optionIndex == 0) {
            option1ImageView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.selected_option));
        } else {
            option2ImageView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.selected_option));
        }
    }

    private void onConfirmButtonClick() {
        if (isAnswered || selectedOptionIndex == -1) {
            Toast.makeText(getContext(), "Выберите вариант!", Toast.LENGTH_SHORT).show();
            return;
        }

        isAnswered = true;
        stopQuestionTimer();

        Question currentQuestion = questions.get(currentQuestionIndex);

        if (selectedOptionIndex == currentQuestion.correctOptionIndex) {
            resultTextView.setText("Правильно!");
            currentQuestionIndex++;
            displayQuestion();
        } else {
            resultTextView.setText("Неверно!");
            highlightWrongAnswer();
            new android.os.Handler().postDelayed(this::resetQuestion, QUESTION_TIMEOUT);
        }
    }

    private void onCancelButtonClick() {
        resetOptionStyles();
        selectedOptionIndex = -1;
    }

    private void highlightWrongAnswer() {
        if (selectedOptionIndex == 0) {
            option1ImageView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.wrong_answer));
        } else {
            option2ImageView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.wrong_answer));
        }
    }

    private void resetQuestion() {
        resetOptionStyles();
        displayQuestion();
    }

    private void resetOptionStyles() {
        option1ImageView.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent));
        option2ImageView.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent));
        selectedOptionIndex = -1;
    }

    private void startQuestionTimer() {
        questionTimer = new CountDownTimer(QUESTION_TIMEOUT, 1000) {
            @Override public void onTick(long millisUntilFinished) {}
            @Override public void onFinish() {
                resetQuestion();
            }
        };
        questionTimer.start();
    }

    private void stopQuestionTimer() {
        if (questionTimer != null) {
            questionTimer.cancel();
            questionTimer = null;
        }
    }

    private void startGameTimer() {
        gameTimer = new CountDownTimer(GAME_TIMEOUT, 1000) {
            @Override public void onTick(long millisUntilFinished) {}
            @Override public void onFinish() {
                Toast.makeText(getContext(), "Время вышло!", Toast.LENGTH_SHORT).show();
                restartGame();
            }
        }.start();
    }

    private void stopGameTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer = null;
        }
    }

    private void restartGame() {
        stopGameTimer();
        currentQuestionIndex = 0;
        displayQuestion();
        startGameTimer();
    }
}
