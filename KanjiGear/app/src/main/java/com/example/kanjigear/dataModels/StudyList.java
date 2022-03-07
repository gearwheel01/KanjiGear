package com.example.kanjigear.dataModels;

public class StudyList {

    private String name;
    private String SLID;

    public StudyList(String SLID, String name) {
        this.SLID = SLID;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getSLID() {return SLID;}
}
