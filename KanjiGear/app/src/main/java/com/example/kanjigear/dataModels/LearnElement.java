package com.example.kanjigear.dataModels;

import android.util.Log;

public class LearnElement implements Comparable {

    public int getNextTestDate() {return 0;};

    public void setNextTestDate(int nextTestDate) {};

    public int getLearningProgress() {return 0;}


    @Override
    public int compareTo(Object o) {
        LearnElement cTo = (LearnElement) o;
        Log.d("le","compare " + getNextTestDate() + " to " + cTo.getNextTestDate());
        return getNextTestDate() - cTo.getNextTestDate();
    }
}
