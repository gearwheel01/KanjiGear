package com.example.kanjigear.dataModels;

import java.util.ArrayList;

public class Sentence {

    private String SID;
    private String text;
    private int learningProgress;
    private ArrayList<SentenceMeaning> meanings;

    public Sentence(String SID, String text, int learningProgress) {
        this.SID = SID;
        this.text = text;
        this.learningProgress = learningProgress;
        meanings = new ArrayList<>();
    }

    public String getSID() {
        return SID;
    }

    public String getText() {
        return text;
    }

    public int getLearningProgress() {
        return learningProgress;
    }

    public ArrayList<SentenceMeaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(ArrayList<SentenceMeaning> meanings) {
        this.meanings = meanings;
    }
}
