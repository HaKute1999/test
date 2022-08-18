package com.example.inote.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "note1")
public class Note1 {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private int idFolder;
    private boolean isPinned;
    private NoteStyle noteStyle;
    private int protectionType;
    private long timeEdit;
    private String title;
    private int type;
    private List<DetailNote> value;

    public Note1(int idFolder, boolean isPinned, int protectionType, long timeEdit, String title, int type, List<DetailNote> value, NoteStyle noteStyle) {

        this.idFolder = idFolder;
        this.isPinned = isPinned;
        this.protectionType = protectionType;
        this.timeEdit = timeEdit;
        this.title = title;
        this.type = type;
        this.value = value;
        this.noteStyle = noteStyle;
    }

    public NoteStyle getNoteStyle() {
        return noteStyle;
    }

    public void setNoteStyle(NoteStyle noteStyle) {
        this.noteStyle = noteStyle;
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

    public List<DetailNote> getValue() {
        return value;
    }

    public void setValue(List<DetailNote> value) {
        this.value = value;
    }




}
