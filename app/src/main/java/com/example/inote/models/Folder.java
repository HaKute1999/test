package com.example.inote.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import androidx.annotation.NonNull;

@Entity(tableName = "folder")
public class Folder {
    @PrimaryKey(autoGenerate = true)
    @NonNull private int id;
    @ColumnInfo
    private String color;
    @ColumnInfo
    private String title;

    public Folder( String color, String title) {
        this.color = color;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
