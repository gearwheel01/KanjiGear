package com.example.kanjigear.dataModels;

public class KanjiMeaning {

    private int KMID;
    private String meaning;
    private String language;

    public KanjiMeaning(int KMID, String meaning, String language) {
        this.KMID = KMID;
        this.meaning = meaning;
        this.language = language;
    }

    public int getKMID() {
        return KMID;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getLanguage() {
        return language;
    }
}
