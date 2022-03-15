package com.example.kanjigear.views.components;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Bezier;
import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.KanjiMeaning;
import com.example.kanjigear.dataModels.Stroke;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseModelLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;

import java.util.ArrayList;

public class KanjiView extends AppCompatActivity {

    private ImageView viewSVG;
    private ImageView ViewBG;
    private TextView viewMeaning;
    private TextView viewReadings;

    private DatabaseOpenHelper db;
    private int size;
    private double thickness = 5;

    private Kanji kanji;
    private ArrayList<KanjiMeaning> meanings;
    private ArrayList<Word> words;
    private ArrayList<Stroke> strokes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_view);

        viewSVG = findViewById(R.id.imageView);
        ViewBG = findViewById(R.id.kanjiViewBG);
        viewMeaning = findViewById(R.id.kanjiViewMeaning);
        viewReadings = findViewById(R.id.kanjiViewReadings);

        db = new DatabaseOpenHelper(getApplicationContext());

        Intent intent = getIntent();
        getKanjiInformation(intent.getStringExtra("symbol"));
        viewMeaning.setText(kanji.getMeaningsString(""));
        viewReadings.setText(kanji.getReadingsString(""));

        getStrokes();

        ViewTreeObserver viewTreeObserver = ViewBG.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewBG.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    size = (int)(ViewBG.getWidth());
                    openSVG();
                }
            });
        }
    }

    public void getKanjiInformation(String symbol) {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT * FROM kanji WHERE symbol = '" + symbol + "';");
        kanji = new DatabaseModelLoader().getKanjiFromCursor(c).get(0);
        c = db.handleQuery("SELECT * FROM kanjimeaning WHERE Kanji_symbol = '" + symbol + "';");
        kanji.setMeanings(new DatabaseModelLoader().getMeaningsFromCursor(c));
        c = db.handleQuery("SELECT * FROM kanjireading WHERE Kanji_symbol = '" + symbol + "';");
        kanji.setReadings(new DatabaseModelLoader().getReadingsFromCursor(c));
        db.closeDatabase();
    }

    public void getStrokes() {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT * FROM stroke WHERE Kanji_symbol = '" + kanji.getSymbol() + "';");
        strokes = new DatabaseModelLoader().getStrokesFromCursor(c);
        for (int i = 0; i < strokes.size(); i += 1) {
            c = db.handleQuery("SELECT * FROM bezier WHERE SID = " + strokes.get(i).getSID());
            strokes.get(i).setBeziers(new DatabaseModelLoader().getBeziersFromCursor(c));
        }
        db.closeDatabase();
    }

    public void openSVG() {
        openSVG(strokes.size(), 0);
    }

    public void openSVG(int untilStroke, double lastStrokePercentage) {
        try {
            SVG svg = SVG.getFromString(getSVGString(untilStroke, lastStrokePercentage));
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            svg.setDocumentWidth(size);
            svg.setDocumentHeight(size);
            PictureDrawable pd = new PictureDrawable(svg.renderToPicture());
            viewSVG.setImageDrawable(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSVGString(int untilStroke, double lastStrokeLength) {
        String ret = "";
        ret += "<svg height=\"109\" viewBox=\"0 0 109 109\">";
        ret += "<g id=\"kvg:StrokePaths_0660e\" style=\"fill:none;stroke:#000000;stroke-width:" + thickness + ";stroke-linecap:round;stroke-linejoin:round;\">";
        for(int i = 0; i < strokes.size(); i += 1) {
            if (i <= untilStroke) {
                if (i == untilStroke) {
                    ret += "<path d = '" + strokes.get(i).getSVGString() + "' stroke-dasharray = '" + strokes.get(i).getLength() +
                            "' stroke-dashoffset = '" + lastStrokeLength + "'  />";
                } else {
                    ret += "<path d = '" + strokes.get(i).getSVGString() + "' />";
                }
            }
        }
        ret += "</g>";
        ret += "</svg>";

        return ret;
    }

    public void animate(View v) {

        Thread t = new Thread(new KanjiAnimator(this, strokes, 20, 2));
        t.start();
    }
}