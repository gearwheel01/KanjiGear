package com.example.kanjigear.dataModels;

public class SentenceMeaning {

    private String language;
    private String meaning;

    public SentenceMeaning(String language, String meaning) {
        this.language = language;
        this.meaning = meaning;
    }

    public String getLanguage() {
        return language;
    }

    public String getMeaning() {
        return meaning;
    }
}
