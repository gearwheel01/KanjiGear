package com.example.kanjigear.dataModels;

public class StudyList {

    private String name;
    private String SLID;
    private boolean active;

    public StudyList(String SLID, String name, boolean active) {
        this.SLID = SLID;
        this.name = name;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public String getSLID() {return SLID;}

    public boolean isActive() {return active;}

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
