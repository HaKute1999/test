package com.example.inote.models;

public class NoteStyle {
    private boolean checkBContent;
    private boolean checkIContent;
    private boolean checkITitle;
    private boolean checkSContent;
    private boolean checkSTitle;
    private boolean checkUContent;
    private boolean checkUTitle;
    private int gravityNote;

    public NoteStyle(boolean checkBContent, boolean checkIContent, boolean checkITitle, boolean checkSContent, boolean checkSTitle, boolean checkUContent, boolean checkUTitle, int gravityNote) {
        this.checkBContent = checkBContent;
        this.checkIContent = checkIContent;
        this.checkITitle = checkITitle;
        this.checkSContent = checkSContent;
        this.checkSTitle = checkSTitle;
        this.checkUContent = checkUContent;
        this.checkUTitle = checkUTitle;
        this.gravityNote = gravityNote;
    }

    public boolean isCheckBContent() {
        return checkBContent;
    }

    public void setCheckBContent(boolean checkBContent) {
        this.checkBContent = checkBContent;
    }

    public boolean isCheckIContent() {
        return checkIContent;
    }

    public void setCheckIContent(boolean checkIContent) {
        this.checkIContent = checkIContent;
    }

    public boolean isCheckITitle() {
        return checkITitle;
    }

    public void setCheckITitle(boolean checkITitle) {
        this.checkITitle = checkITitle;
    }

    public boolean isCheckSContent() {
        return checkSContent;
    }

    public void setCheckSContent(boolean checkSContent) {
        this.checkSContent = checkSContent;
    }

    public boolean isCheckSTitle() {
        return checkSTitle;
    }

    public void setCheckSTitle(boolean checkSTitle) {
        this.checkSTitle = checkSTitle;
    }

    public boolean isCheckUContent() {
        return checkUContent;
    }

    public void setCheckUContent(boolean checkUContent) {
        this.checkUContent = checkUContent;
    }

    public boolean isCheckUTitle() {
        return checkUTitle;
    }

    public void setCheckUTitle(boolean checkUTitle) {
        this.checkUTitle = checkUTitle;
    }

    public int getGravityNote() {
        return gravityNote;
    }

    public void setGravityNote(int gravityNote) {
        this.gravityNote = gravityNote;
    }
}
