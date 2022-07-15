package com.example.inote.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import androidx.annotation.NonNull;

import java.util.List;
@Entity(tableName = "note")
public class Note {
    @PrimaryKey(autoGenerate = true)
    @NonNull private int id;
    private int idFolder;
    private boolean isPinned;
    private String listImage;
    private String noteStyle;
    private String passNote;
    private String path;
    private String protectionHash;
    private int protectionType;
    private long timeEdit;
    private String title;
    private int type;
    private String value;
    private String valueChecklist;

    public Note( int idFolder, boolean isPinned, String listImage, String noteStyle, String passNote, String path, String protectionHash, int protectionType, long timeEdit, String title, int type, String value, String valueChecklist) {

        this.idFolder = idFolder;
        this.isPinned = isPinned;
        this.listImage = listImage;
        this.noteStyle = noteStyle;
        this.passNote = passNote;
        this.path = path;
        this.protectionHash = protectionHash;
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

    public String getListImage() {
        return listImage;
    }

    public void setListImage(String listImage) {
        this.listImage = listImage;
    }

    public String getNoteStyle() {
        return noteStyle;
    }

    public void setNoteStyle(String noteStyle) {
        this.noteStyle = noteStyle;
    }

    public String getPassNote() {
        return passNote;
    }

    public void setPassNote(String passNote) {
        this.passNote = passNote;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProtectionHash() {
        return protectionHash;
    }

    public void setProtectionHash(String protectionHash) {
        this.protectionHash = protectionHash;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueChecklist() {
        return valueChecklist;
    }

    public void setValueChecklist(String valueChecklist) {
        this.valueChecklist = valueChecklist;
    }
}
