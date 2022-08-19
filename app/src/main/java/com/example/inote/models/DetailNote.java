package com.example.inote.models;

import java.util.List;

public class  DetailNote {
    String type;
    String values;

    public DetailNote(String type, String values) {
        this.type = type;
        this.values = values;
    }

    public String getStringype() {
        return type;
    }

    public void setStringype(String type) {
        this.type = type;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}
