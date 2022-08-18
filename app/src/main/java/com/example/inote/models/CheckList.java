package com.example.inote.models;

import android.graphics.drawable.Drawable;

public class CheckList {
    private int type;
    private Drawable image;
    private String text;

    public CheckList(int type, Drawable image, String text) {
        this.type = type;
        this.image = image;
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
