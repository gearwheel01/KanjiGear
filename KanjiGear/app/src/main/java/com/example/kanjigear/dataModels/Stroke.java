package com.example.kanjigear.dataModels;

public class Stroke {

    private String SID;
    private String strokeInformation;
    private int number;
    private String Kanji_symbol;
    private String component;

    public Stroke(String SID, String strokeInformation, int number, String kanji_symbol, String component) {
        this.SID = SID;
        this.strokeInformation = strokeInformation;
        this.number = number;
        Kanji_symbol = kanji_symbol;
        this.component = component;
    }

    public String getSID() {
        return SID;
    }

    public String getStrokeInformation() {
        return strokeInformation;
    }

    public int getNumber() {
        return number;
    }

    public String getKanji_symbol() {
        return Kanji_symbol;
    }

    public String getComponent() {
        return component;
    }
}
