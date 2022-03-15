package com.example.kanjigear.dataModels;

import java.util.ArrayList;

public class Word {

    private String WID;
    private int learningProgress;
    private int frequency;

    private ArrayList<String> wordWritings;
    private ArrayList<String> wordReadings;
    private ArrayList<WordMeaning> wordTranslations;

    public Word(String WID, int learningProgress, int frequency)
    {
        this.WID = WID;
        this.learningProgress = learningProgress;
        this.frequency = frequency;

        wordTranslations = new ArrayList<>();
        wordReadings = new ArrayList<>();
        wordWritings = new ArrayList<>();
    }

    public String getWID() {
        return WID;
    }

    public int getLearningProgress() {
        return learningProgress;
    }

    public void setTranslations(ArrayList<WordMeaning> t) {wordTranslations = t;}

    public ArrayList<WordMeaning> getWordTranslations() {return wordTranslations;}

    // empty string for no lang criteria
    public String getTranslationString(String lang) {
        String ret = "";
        for (int i = 0; i < wordTranslations.size(); i += 1) {
            if ( (lang.equals("")) || (lang.equals(wordTranslations.get(i).getLanguage())) ) {
                if (!ret.equals("")) {
                    ret += ", ";
                }
                ret += wordTranslations.get(i).getMeaning();
            }
        }
        return ret;
    }

    public ArrayList<String> getWordWritings() {
        return wordWritings;
    }

    public void setWordWritings(ArrayList<String> wordWritings) {
        this.wordWritings = wordWritings;
    }

    public ArrayList<String> getWordReadings() {
        return wordReadings;
    }

    public void setWordReadings(ArrayList<String> wordReadings) {
        this.wordReadings = wordReadings;
    }

    public int getFrequency() {
        return frequency;
    }
}
