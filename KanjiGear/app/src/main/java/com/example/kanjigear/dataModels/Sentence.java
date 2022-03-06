package com.example.kanjigear.dataModels;

public class Sentence {

    private String SID;
    private String text;
    private int learningProgress;

    public Sentence(String SID, String text, int learningProgress) {
        this.SID = SID;
        this.text = text;
        this.learningProgress = learningProgress;
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
}
