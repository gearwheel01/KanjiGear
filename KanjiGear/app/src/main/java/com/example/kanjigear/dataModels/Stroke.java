package com.example.kanjigear.dataModels;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.Log;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Stroke {

    private String SID;
    private int number;
    private String Kanji_symbol;
    private String component;
    private float mx;
    private float my;
    Path path;
    float length;
    private ArrayList<Bezier> beziers;


    public Stroke(String SID, int number, String kanji_symbol, String component, float mx, float my) {
        this.SID = SID;
        this.number = number;
        Kanji_symbol = kanji_symbol;
        this.component = component;
        this.mx = mx;
        this.my = my;
        length = 0;
    }

    public String getSID() {
        return SID;
    }

    public int getNumber() {
        return number;
    }

    public String getKanji_symbol() {
        return Kanji_symbol;
    }

    public String getComponent() {
        return component;
    }

    public float getMx() {
        return mx;
    }

    public float getMy() {
        return my;
    }

    public ArrayList<Bezier> getBeziers() {
        return beziers;
    }

    public void setBeziers(ArrayList<Bezier> beziers) {
        this.beziers = beziers;
        calculatePath();
        length = new PathMeasure(path, false).getLength();
    }

    public String getSVGString() {
        String ret = "M" + mx + "," + my;
        for (int i = 0; i < beziers.size(); i += 1) {
            Bezier b = beziers.get(i);
            if (b.isRelative()) {
                ret += "c";
            } else
            {
                ret += "C";
            }
            ret += b.getX1() + "," + b.getY1() + ",";
            ret += b.getX2() + "," + b.getY2() + ",";
            ret += b.getX() + "," + b.getY();
        }

        return ret;
    }

    public Path getPath() {
        return path;
    }

    public void calculatePath() {
        Path p = new Path();
        p.moveTo(mx, my);
        for (int i = 0; i < beziers.size(); i += 1) {
            Bezier b = beziers.get(i);
            if (b.isRelative()) {
                p.rCubicTo(b.getX1(),b.getY1(),b.getX2(),b.getY2(),b.getX(),b.getY());
            } else {
                p.cubicTo(b.getX1(),b.getY1(),b.getX2(),b.getY2(),b.getX(),b.getY());
            }
        }
        path = p;

    }

    public float getLength() {
        return length;
    }
}
