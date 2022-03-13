package com.example.kanjigear.dataModels;

public class WordMeaning {

    private String WMID;
    private String meaning;
    private String language;

    public WordMeaning(String WMID, String meaning, String language) {
        this.WMID = WMID;
        this.meaning = meaning;
        this.language = language;
    }

    public String getWMID() {
        return WMID;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getLanguage() {
        return language;
    }
}
