package com.example.kanjigear.views.components;

import android.util.Log;

import com.example.kanjigear.dataModels.Stroke;

import java.util.ArrayList;

public class KanjiAnimator extends Thread {

    KanjiView view;
    ArrayList<Stroke> strokes;
    int sleepTime;
    float steps;

    public KanjiAnimator(KanjiView v, ArrayList<Stroke> s, int st, float sp) {
        view = v;
        strokes = s;
        sleepTime = st;
        steps = sp;
    }

    public void run() {
        for (int s = 0; s < strokes.size(); s += 1) {
            for (float i = strokes.get(s).getLength() - 1; i > 0; i -= steps) {
                view.openSVG(s, i);
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
