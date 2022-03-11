package com.example.kanjigear.dataModels;

import java.util.ArrayList;

public class Word {

    private String WID;
    private String word;
    private String grade;
    private int learningProgress;
    private String pronunciation;
    private String romaji;
    private ArrayList<WordTranslation> wordTranslations;

    public Word(String WID, String word, String grade, int learningProgress, String pronunciation, String romaji)
    {
        this.WID = WID;
        this.word = word;
        this.grade = grade;
        this.learningProgress = learningProgress;
        this.pronunciation = pronunciation;
        wordTranslations = new ArrayList<>();
    }

    public String getWID() {
        return WID;
    }

    public String getWord() {
        return word;
    }

    public String getGrade() {
        return grade;
    }

    public int getLearningProgress() {
        return learningProgress;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getRomaji() {return romaji;}

    public void setTranslations(ArrayList<WordTranslation> t) {wordTranslations = t;}

    public ArrayList<WordTranslation> getWordTranslations() {return wordTranslations;}

    // empty string for no lang criteria
    public String getTranslationString(String lang) {
        String ret = "";
        for (int i = 0; i < wordTranslations.size(); i += 1) {
            if ( (lang.equals("")) || (lang.equals(wordTranslations.get(i).getLanguage())) ) {
                if (!ret.equals("")) {
                    ret += ", ";
                }
                ret += wordTranslations.get(i).getTranslation();
            }
        }
        return ret;
    }

}
