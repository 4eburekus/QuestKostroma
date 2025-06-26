package com.example.questkostroma;

public class Character {
    private int imageCharacter; // resource ID (R.drawable.xxx)
    private CharacterPosition characterPosition;

    public Character(int imageCharacter, CharacterPosition position) {
        this.imageCharacter = imageCharacter;
        this.characterPosition = position;
    }

    public int getImageCharacter() {
        return imageCharacter;
    }

    public CharacterPosition getCharacterPosition() {
        return characterPosition;
    }



    public void setCharacterPosition(CharacterPosition characterPosition) {
        this.characterPosition = characterPosition;
    }


    public enum CharacterPosition {
        LEFT, CENTER, RIGHT
    }
}

