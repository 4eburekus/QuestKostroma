package com.example.questkostroma;

public class BaseScreen {
    private Character character;
    private int backgroundResId; // R.drawable.xxx
    private int musicResId;      // R.raw.xxx
    private DialogText dialogText;

    public BaseScreen(Character character, int backgroundResId, int musicResId, DialogText dialogText) {
        this.character = character;
        this.backgroundResId = backgroundResId;
        this.musicResId = musicResId;
        this.dialogText = dialogText;
    }

    public Character getCharacter() {
        return character;
    }

    public int getBackgroundResId() {
        return backgroundResId;
    }

    public int getMusicResId() {
        return musicResId;
    }

    public DialogText getDialogText() {
        return dialogText;
    }
}
