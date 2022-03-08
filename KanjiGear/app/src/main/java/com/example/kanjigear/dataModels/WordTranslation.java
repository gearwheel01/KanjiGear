package com.example.kanjigear.dataModels;

public class WordTranslation {

    private String WMID;
    private String translation;
    private String language;

    public WordTranslation(String WMID, String translation, String language) {
        this.WMID = WMID;
        this.translation = translation;
        this.language = language;
    }

    public String getWMID() {
        return WMID;
    }

    public String getTranslation() {
        return translation;
    }

    public String getLanguage() {
        return language;
    }
}
