package com.example.kanjigear.views.components;

import com.example.kanjigear.dataModels.Stroke;

import java.util.ArrayList;

public class KanjiAnimator extends Thread {

    KanjiView view;
    ArrayList<Stroke> strokes;
    float sleepTime;
    float steps;

    public KanjiAnimator(KanjiView v, ArrayList<Stroke> s, float st, float sp) {
        view = v;
        strokes = s;
        sleepTime = st;
        steps = sp;
    }

    public void run() {
        for (int s = 0; s < strokes.size(); s += 1) {
            for (float i = 0; i < 1; i += steps) {
                view.drawKanji(s, i);
                try {
                    sleep((long)(sleepTime * strokes.get(s).getLength()));
                } catch (InterruptedException e) {
                    return;
                }
            }
            view.drawKanji(s, 1);
            try {
                sleep((long)(sleepTime * 6));
            } catch (InterruptedException e) {
                return;
            }
        }
        view.stopAnimation();
    }


}
