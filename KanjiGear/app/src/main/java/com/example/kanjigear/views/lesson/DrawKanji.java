package com.example.kanjigear.views.lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.Stroke;
import com.example.kanjigear.db.DatabaseContentLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.example.kanjigear.views.components.KanjiGraphicView;

import java.util.ArrayList;

public class DrawKanji extends AppCompatActivity {

    private TextView viewContent;
    private Button viewDone;
    private KanjiGraphicView viewKanji;

    private Kanji kanji;
    private ArrayList<Stroke> strokes;
    private int strokeWidth = 5;

    private DatabaseOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_kanji);
        loadResources();

        Intent intent = getIntent();
        DatabaseContentLoader loadHelper = new DatabaseContentLoader();
        kanji = loadHelper.getKanji(db, intent.getStringExtra("symbol"));
        strokes = loadHelper.getStrokes(db, kanji);

        viewKanji.setDrawMode(true);
        viewKanji.setStrokes(strokes);
    }

    public void loadResources() {
        viewContent = findViewById(R.id.drawKanjiContent);
        viewDone = findViewById(R.id.drawKanjiDone);
        viewKanji = findViewById(R.id.drawKanjiKanji);
        db = new DatabaseOpenHelper(getApplicationContext());
    }

    public void drawKanji() {
        drawKanji(strokes.size(), 0);
    }

    public void drawKanji(int untilStroke, double lastStrokeLength) {
    }
}