package com.example.kanjigear.views;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;
import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Stroke;
import com.example.kanjigear.db.DatabaseOpenHelper;

import java.util.ArrayList;

public class KanjiView extends AppCompatActivity {

    private ImageView view;
    private DatabaseOpenHelper db;
    private String kanji = "本";
    private int size;
    private ArrayList<Stroke> strokes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanji_view);

        view = findViewById(R.id.imageView);
        db = new DatabaseOpenHelper(getApplicationContext());

        try {
            SVG svg = SVG.getFromString(getSVGString());
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            size = (int)(displayMetrics.widthPixels * 0.8);
            svg.setDocumentWidth(size);
            svg.setDocumentHeight(size);
            PictureDrawable pd = new PictureDrawable(svg.renderToPicture());
            view.setImageDrawable(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("Range")
    public void getStrokes() {
        db.openDatabase();
        strokes = new ArrayList<Stroke>();
        Cursor c = db.handleQuery("SELECT * FROM stroke WHERE Kanji_symbol = '" + kanji + "';");
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            String SID = c.getString(c.getColumnIndex("SID"));
            String strokeInformation = c.getString(c.getColumnIndex("strokeInformation"));
            int number = c.getInt(c.getColumnIndex("number"));
            String symbol = c.getString(c.getColumnIndex("Kanji_symbol"));
            String component = c.getString(c.getColumnIndex("component"));
            strokes.add(new Stroke(SID, strokeInformation, number, symbol, component));
            c.moveToNext();
        }
        c.close();
        db.closeDatabase();
    }

    public String getSVGString() {
        getStrokes();
        String ret = "";

        ret += "<svg height=\"109\" viewBox=\"0 0 109 109\">";
        ret += "<g id=\"kvg:StrokePaths_0660e\" style=\"fill:none;stroke:#000000;stroke-width:3;stroke-linecap:round;stroke-linejoin:round;\">";
        for(int i = 0; i < strokes.size(); i += 1) {
            ret += "<path d=\""+ strokes.get(i).getStrokeInformation() + "\"/>";
        }
        ret += "</g>";
        ret += "</svg>";

        return ret;
    }
}