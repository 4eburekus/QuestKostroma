package com.example.kostromaquest;

public class TextElement {
    private int id;
    private String characterName;
    private String text;

    // Конструктор
    public TextElement(int id, String characterName, String text) {
        this.id = id;
        this.characterName = characterName;
        this.text = text;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // Удобное представление
    @Override
    public String toString() {
        return "TextElement{" +
                "id=" + id +
                ", characterName='" + characterName + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
