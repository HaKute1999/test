package com.example.inote.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import androidx.annotation.NonNull;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "recent")

public class Recent {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private int idFolder;
    private boolean isPinned;
    private List<String> listImage;

    private int protectionType;
    private long timeEdit;
    private String title;
    private int type;
    private List<String> value;
    private List<CheckItem> valueChecklist;

    public Recent(int idFolder, boolean isPinned, List<String> listImage, int protectionType, long timeEdit, String title, int type, List<String> value, List<CheckItem> valueChecklist) {

        this.idFolder = idFolder;
        this.isPinned = isPinned;
        this.listImage = listImage;

        this.protectionType = protectionType;
        this.timeEdit = timeEdit;
        this.title = title;
        this.type = type;
        this.value = value;
        this.valueChecklist = valueChecklist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFolder() {
        return idFolder;
    }

    public void setIdFolder(int idFolder) {
        this.idFolder = idFolder;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    public List<String> getListImage() {
        return listImage;
    }

    public void setListImage(List<String> listImage) {
        this.listImage = listImage;
    }

    public int getProtectionType() {
        return protectionType;
    }

    public void setProtectionType(int protectionType) {
        this.protectionType = protectionType;
    }

    public long getTimeEdit() {
        return timeEdit;
    }

    public void setTimeEdit(long timeEdit) {
        this.timeEdit = timeEdit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public List<CheckItem> getValueChecklist() {
        return valueChecklist;
    }

    public void setValueChecklist(List<CheckItem> valueChecklist) {
        this.valueChecklist = valueChecklist;
    }
}
