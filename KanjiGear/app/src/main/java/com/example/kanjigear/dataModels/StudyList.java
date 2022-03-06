package com.example.kanjigear.dataModels;

public class StudyList {

    private String SLID;
    private String name;

    public StudyList(String SLID, String name) {
        this.SLID = SLID;
        this.name = name;
    }

    public String getSLID() {
        return SLID;
    }

    public String getName() {
        return name;
    }
}
