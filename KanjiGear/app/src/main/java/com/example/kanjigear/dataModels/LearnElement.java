package com.example.kanjigear.dataModels;

import android.util.Log;

public class LearnElement implements Comparable {

    private String inList;

    public int getNextTestDate() {return 0;};

    public void setNextTestDate(int nextTestDate) {};

    public int getLearningProgress() {return 0;}

    public String getInList() {
        return inList;
    }

    public void setInList(String inList) {
        this.inList = inList;
    }

    @Override
    public int compareTo(Object o) {
        LearnElement cTo = (LearnElement) o;
        return getNextTestDate() - cTo.getNextTestDate();
    }
}
