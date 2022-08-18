package com.example.inote.models;

public class StyleText {
    private String name;
    private int posStartB;
    private int posEndB;
    private int posStartI;
    private int posEndI;
    private int posStartU;
    private int posEndU;
    private int posStartS;
    private int posEndS;
    private boolean checkIContent;
    private boolean checkBContent;
    private boolean checkSContent;
    private boolean checkUContent;

    public StyleText(String name, int posStartB, int posEndB, int posStartI, int posEndI, int posStartU, int posEndU, int posStartS, int posEndS, boolean checkIContent, boolean checkBContent, boolean checkSContent, boolean checkUContent) {
        this.name = name;
        this.posStartB = posStartB;
        this.posEndB = posEndB;
        this.posStartI = posStartI;
        this.posEndI = posEndI;
        this.posStartU = posStartU;
        this.posEndU = posEndU;
        this.posStartS = posStartS;
        this.posEndS = posEndS;
        this.checkIContent = checkIContent;
        this.checkBContent = checkBContent;
        this.checkSContent = checkSContent;
        this.checkUContent = checkUContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosStartB() {
        return posStartB;
    }

    public void setPosStartB(int posStartB) {
        this.posStartB = posStartB;
    }

    public int getPosEndB() {
        return posEndB;
    }

    public void setPosEndB(int posEndB) {
        this.posEndB = posEndB;
    }

    public int getPosStartI() {
        return posStartI;
    }

    public void setPosStartI(int posStartI) {
        this.posStartI = posStartI;
    }

    public int getPosEndI() {
        return posEndI;
    }

    public void setPosEndI(int posEndI) {
        this.posEndI = posEndI;
    }

    public int getPosStartU() {
        return posStartU;
    }

    public void setPosStartU(int posStartU) {
        this.posStartU = posStartU;
    }

    public int getPosEndU() {
        return posEndU;
    }

    public void setPosEndU(int posEndU) {
        this.posEndU = posEndU;
    }

    public int getPosStartS() {
        return posStartS;
    }

    public void setPosStartS(int posStartS) {
        this.posStartS = posStartS;
    }

    public int getPosEndS() {
        return posEndS;
    }

    public void setPosEndS(int posEndS) {
        this.posEndS = posEndS;
    }

    public boolean isCheckIContent() {
        return checkIContent;
    }

    public void setCheckIContent(boolean checkIContent) {
        this.checkIContent = checkIContent;
    }

    public boolean isCheckBContent() {
        return checkBContent;
    }

    public void setCheckBContent(boolean checkBContent) {
        this.checkBContent = checkBContent;
    }

    public boolean isCheckSContent() {
        return checkSContent;
    }

    public void setCheckSContent(boolean checkSContent) {
        this.checkSContent = checkSContent;
    }

    public boolean isCheckUContent() {
        return checkUContent;
    }

    public void setCheckUContent(boolean checkUContent) {
        this.checkUContent = checkUContent;
    }
}
