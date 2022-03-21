package com.example.kanjigear.views.components;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.example.kanjigear.R;
import com.example.kanjigear.alerts.AddToList;
import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.KanjiMeaning;
import com.example.kanjigear.dataModels.Stroke;
import com.example.kanjigear.dataModels.StudyList;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseContentLoader;
import com.example.kanjigear.db.DatabaseModelLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.example.kanjigear.views.dictionary.RecyclerAdapterWord;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class KanjiView extends AppCompatActivity {

    private ImageView viewSVG;
    private ImageView ViewBG;
    private TextView viewMeaning;
    private TextView viewKUN;
    private TextView viewON;
    private Button viewAnimate;
    private RecyclerView viewWords;
    private TabLayout viewWordTabs;
    private Button viewAddlist;

    private DatabaseOpenHelper db;
    private int size;
    private double thickness = 5;

    private Kanji kanji;
    private ArrayList<KanjiMeaning> meanings;
    private ArrayList<Word> wordsLearned;
    private ArrayList<Word> wordsNew;

    private ArrayList<Stroke> strokes;
    KanjiAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_view);

        loadResources();

        Intent intent = getIntent();
        getKanjiInformation(intent.getStringExtra("symbol"));
        viewMeaning.setText(kanji.getMeaningsString(""));
        viewKUN.setText(kanji.getReadingsString("KUN"));
        viewON.setText(kanji.getReadingsString("ON"));

        getStrokes();
        loadWords();

        if (intent.hasExtra("SLID")) {
            updateAddlistButton();
        }

        viewWords.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewWords.setItemAnimator(new DefaultItemAnimator());
        setAdapter(wordsLearned);

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
        
        viewWordTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals(getResources().getString(R.string.tabNew))) {
                    setAdapter(wordsNew);
                } else {
                    setAdapter(wordsLearned);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void loadResources() {
        viewSVG = findViewById(R.id.imageView);
        ViewBG = findViewById(R.id.kanjiViewBG);
        viewMeaning = findViewById(R.id.kanjiViewMeaning);
        viewKUN = findViewById(R.id.kanjiViewKUN);
        viewON = findViewById(R.id.kanjiViewON);
        viewAnimate = findViewById(R.id.kanjiViewAnimate);
        viewWords = findViewById(R.id.kanjiViewListKanji);
        viewWordTabs = findViewById(R.id.kanjiViewTab);
        viewAddlist = findViewById(R.id.kanjiViewAddlist);
        db = new DatabaseOpenHelper(getApplicationContext());
    }


    public void getKanjiInformation(String symbol) {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT * FROM kanji WHERE symbol = '" + symbol + "';");
        kanji = new DatabaseModelLoader().getKanjiFromCursor(c).get(0);
        c = db.handleQuery("SELECT * FROM kanjimeaning WHERE Kanji_symbol = '" + symbol + "';");
        kanji.setMeanings(new DatabaseModelLoader().getKanjiMeaningsFromCursor(c));
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
        if (animator == null) {
            animator = new KanjiAnimator(this, strokes, 20, 3);
            animator.start();
            viewAnimate.setBackgroundResource(R.drawable.button_animation_stop);
        } else {
            stopAnimation();
        }
    }

    public void stopAnimation() {
        if (animator != null) {
            animator.interrupt();
            openSVG();
            animator = null;
            viewAnimate.setBackgroundResource(R.drawable.button_animation_play);
        }
    }

    public void loadWords() {
        db.openDatabaseRead();

        Cursor c = db.handleQuery("SELECT * FROM word w, wordwriting ww " +
                "WHERE ww.Word_WID = w.WID AND ww.writing LIKE '%" + kanji.getSymbol() + "%' AND w.learningProgress > 0 " +
                "ORDER BY w.frequency DESC LIMIT 10;");
        wordsLearned = new DatabaseModelLoader().getWordsFromCursor(c);
        wordsLearned = new DatabaseContentLoader().addDetailsToWords(db, wordsLearned);
        c = db.handleQuery("SELECT * FROM word w, wordwriting ww " +
                "WHERE ww.Word_WID = w.WID AND ww.writing LIKE '%" + kanji.getSymbol() + "%' AND w.learningProgress = 0 " +
                "ORDER BY w.frequency DESC LIMIT 10;");
        wordsNew = new DatabaseModelLoader().getWordsFromCursor(c);
        wordsNew = new DatabaseContentLoader().addDetailsToWords(db, wordsNew);

        db.close();
    }
    
    public void setAdapter(ArrayList<Word> words) {
        RecyclerAdapterWord adapter = new RecyclerAdapterWord(this, words);
        viewWords.setAdapter(adapter);
    }

    public void openWord(String WID) {
        Intent intent = new Intent(this, WordView.class);
        intent.putExtra("WID", WID);
        if (getIntent().hasExtra("SLID")) {
            intent.putExtra("SLID", getIntent().getStringExtra("SLID"));
        }
        startActivity(intent);
    }

    public boolean isKanjiInList(String SLID) {
        db.openDatabaseRead();
        boolean ret = (db.handleQuery("SELECT * FROM listcontainskanji WHERE Kanji_symbol = '" + kanji.getSymbol() + "' AND StudyList_SLID = " + SLID + ";").getCount() > 0);
        db.closeDatabase();
        return ret;
    }
    public void updateAddlistButton() {
        viewAddlist.setBackgroundResource(isKanjiInList(getIntent().getStringExtra("SLID"))?
                R.drawable.button_listadded:R.drawable.button_listadd);
    }

    public void addToList(View v) {
        if (getIntent().hasExtra("SLID")) {
            String SLID = getIntent().getStringExtra("SLID");
            if (isKanjiInList(SLID)) {
                db.openDatabase();
                db.delete("listcontainskanji", "StudyList_SLID", SLID, "Kanji_symbol", kanji.getSymbol());
                db.closeDatabase();
            } else {
                ContentValues values = new ContentValues();
                values.put("StudyList_SLID", SLID);
                values.put("Kanji_symbol", kanji.getSymbol());
                db.openDatabase();
                db.insert("listcontainskanji", values);
                db.closeDatabase();
            }
            updateAddlistButton();
        } else {
            AddToList dialog = new AddToList(this, db, kanji);
            dialog.showDialog(this);
        }
    }
}