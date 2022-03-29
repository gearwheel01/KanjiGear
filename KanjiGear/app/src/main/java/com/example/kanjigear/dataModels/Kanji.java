package com.example.kanjigear.dataModels;

import java.util.ArrayList;

public class Kanji {

    private String symbol;
    private int grade;
    private int jlpt;
    private int frequency;
    private int learningProgress;

    private ArrayList<KanjiMeaning> meanings;
    private ArrayList<Reading> readings;

    public Kanji(String symbol, int grade, int jlpt, int frequency, int learningProgress) {
        this.symbol = symbol;
        this.grade = grade;
        this.jlpt = jlpt;
        this.frequency = frequency;
        this.learningProgress = learningProgress;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getGrade() {
        return grade;
    }

    public int getLearningProgress() {
        return learningProgress;
    }

    public int getJlpt() {
        return jlpt;
    }

    public int getFrequency() {
        return frequency;
    }

    public ArrayList<KanjiMeaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(ArrayList<KanjiMeaning> meanings) {
        this.meanings = meanings;
    }

    public ArrayList<Reading> getReadings() {
        return readings;
    }

    public void setReadings(ArrayList<Reading> readings) {
        this.readings = readings;
    }

    // empty string for no lang criteria
    public String getMeaningsString(String lang) {
        String ret = "";
        for (int i = 0; i < meanings.size(); i += 1) {
            if ( (lang.equals("")) || (lang.equals(meanings.get(i).getLanguage())) ) {
                if (!ret.equals("")) {
                    ret += ", ";
                }
                ret += meanings.get(i).getMeaning();
            }
        }
        return ret;
    }
    // empty string for no type criteria
    public String getReadingsString(String type, String inBetween) {
        String ret = "";
        for (int i = 0; i < readings.size(); i += 1) {
            if ( (type.equals("")) || (type.equals(readings.get(i).getType())) ) {
                if (!ret.equals("")) {
                    ret += inBetween;
                }
                ret += readings.get(i).getReading();
            }
        }
        return ret;
    }

}
