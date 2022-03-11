package com.example.kanjigear.dataModels;

import java.util.ArrayList;

public class Kanji {

    private String symbol;
    private String grade;
    private int learningProgress;

    private ArrayList<KanjiMeaning> meanings;
    private ArrayList<Reading> readings;

    public Kanji(String symbol, String grade, int learningProgress) {
        this.symbol = symbol;
        this.grade = grade;
        this.learningProgress = learningProgress;
        meanings = new ArrayList<>();
        readings = new ArrayList<>();
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
    public String getReadingsString(String type) {
        String ret = "";
        for (int i = 0; i < readings.size(); i += 1) {
            if ( (type.equals("")) || (type.equals(readings.get(i).getReading())) ) {
                if (!ret.equals("")) {
                    ret += ", ";
                }
                ret += readings.get(i).getReading();
            }
        }
        return ret;
    }

}
