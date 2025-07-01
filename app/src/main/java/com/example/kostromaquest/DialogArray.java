package com.example.kostromaquest;

import java.util.ArrayList;
import java.util.List;

public class DialogArray {
    private List<TextElement> dialogList;
    private int currentIndex;

    public DialogArray() {
        this.dialogList = new ArrayList<>();
        this.currentIndex = 0;
    }

    // Добавить элемент в диалог
    public void addTextElement(TextElement element) {
        dialogList.add(element);
    }

    // Получить текущий элемент
    public TextElement getCurrentElement() {
        if (dialogList.isEmpty()) return null;
        return dialogList.get(currentIndex);
    }

    // Перейти к следующему элементу
    public boolean next() {
        if (currentIndex < dialogList.size() - 1) {
            currentIndex++;
            return true;
        }
        return false; // конец диалога
    }

    // Откатиться на один элемент назад
    public boolean previous() {
        if (currentIndex > 0) {
            currentIndex--;
            return true;
        }
        return false; // уже в начале
    }

    // Получить элемент по id
    public TextElement getElementById(int id) {
        for (TextElement element : dialogList) {
            if (element.getId() == id) {
                return element;
            }
        }
        return null;
    }

    // Получить текущую позицию
    public int getCurrentIndex() {
        return currentIndex;
    }

    // Установить текущую позицию вручную (например, по id)
    public boolean setCurrentById(int id) {
        for (int i = 0; i < dialogList.size(); i++) {
            if (dialogList.get(i).getId() == id) {
                currentIndex = i;
                return true;
            }
        }
        return false;
    }

    // Сброс к началу
    public void reset() {
        currentIndex = 0;
    }

    // Проверка на конец диалога
    public boolean isAtEnd() {
        return currentIndex >= dialogList.size() - 1;
    }

    // Проверка на начало
    public boolean isAtStart() {
        return currentIndex == 0;
    }


}
