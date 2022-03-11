package com.example.kanjigear.dataModels;

public class Reading {

    private int RID;
    private String type;
    private String reading;

    public Reading(int RID, String type, String reading) {
        this.RID = RID;
        this.type = type;
        this.reading = reading;
    }

    public int getRID() {
        return RID;
    }

    public String getType() {
        return type;
    }

    public String getReading() {
        return reading;
    }
}
