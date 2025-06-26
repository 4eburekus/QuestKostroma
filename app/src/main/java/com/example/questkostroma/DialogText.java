package com.example.questkostroma;

import java.util.LinkedHashMap;

public class DialogText {
    public static class Line {
        String characterName;
        String text;

        public Line(String name, String text) {
            this.characterName = name;
            this.text = text;
        }

        public String getCharacterName() {
            return characterName;
        }

        public String getText() {
            return text;
        }
    }

    private LinkedHashMap<Integer, Line> textMap;
    private int currentId;

    public DialogText() {
        textMap = new LinkedHashMap<>();
        currentId = 0;
    }

    public void addText(int id, String name, String text) {
        textMap.put(id, new Line(name, text));
    }

    public String getCurrentText() {
        return textMap.get(currentId).getText();
    }

    public String getCurrentSpeaker() {
        return textMap.get(currentId).getCharacterName();
    }

    public boolean nextText() {
        if (textMap.containsKey(currentId + 1)) {
            currentId++;
            return true;
        }
        return false;
    }

    public int getCurrentId() {
        return currentId;
    }

    public void reset() {
        currentId = 0;
    }
}

