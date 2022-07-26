package com.example.inote.models;

public class CheckItem {
    private int id;
    private boolean isDone;
    private String title;

    public CheckItem(int id, boolean isDone, String title) {
        this.id = id;
        this.isDone = isDone;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
