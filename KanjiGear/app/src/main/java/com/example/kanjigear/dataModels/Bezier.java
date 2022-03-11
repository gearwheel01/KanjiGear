package com.example.kanjigear.dataModels;

public class Bezier {

    private boolean relative;

    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private float x;
    private float y;

    public Bezier(boolean relative, float x1, float y1, float x2, float y2, float x, float y) {
        this.relative = relative;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x = x;
        this.y = y;
    }

    public boolean isRelative() {
        return relative;
    }

    public float getX1() {
        return x1;
    }

    public float getY1() {
        return y1;
    }

    public float getX2() {
        return x2;
    }

    public float getY2() {
        return y2;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
