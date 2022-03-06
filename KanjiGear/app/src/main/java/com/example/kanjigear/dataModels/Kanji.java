package com.example.kanjigear.dataModels;

public class Kanji {

    private String symbol;
    private String grade;
    private int learningProgress;

    public Kanji(String symbol, String grade, int learningProgress) {
        this.symbol = symbol;
        this.grade = grade;
        this.learningProgress = learningProgress;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getGrade() {
        return grade;
    }

    public int getLearningProgress() {
        return learningProgress;
    }
}
