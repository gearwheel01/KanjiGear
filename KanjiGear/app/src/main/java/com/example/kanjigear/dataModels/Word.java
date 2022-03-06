package com.example.kanjigear.dataModels;

public class Word {

    private String WID;
    private String word;
    private String grade;
    private int learningProgress;
    private String pronunciation;

    public Word(String WID, String word, String grade, int learningProgress, String pronunciation)
    {
        this.WID = WID;
        this.word = word;
        this.grade = grade;
        this.learningProgress = learningProgress;
        this.pronunciation = pronunciation;
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

}
