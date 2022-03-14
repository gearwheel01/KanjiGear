package com.example.kanjigear.db;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.views.dictionary.Dictionary;
import com.example.kanjigear.views.dictionary.RecyclerAdapterWord;

import java.util.ArrayList;
import java.util.Locale;

public class DictWordSearch extends Thread {

    private Dictionary dictionary;
    private String searchString;
    private boolean stopped = false;

    public DictWordSearch(Dictionary d, String s) {
        dictionary = d;
        searchString = s;
    }

    @SuppressLint("Range")
    @Override
    public void run() {
        DatabaseOpenHelper db = new DatabaseOpenHelper(dictionary.getApplicationContext());
        Cursor c;
        db.openDatabaseRead();
        db.handleQuery("PRAGMA case_sensitive_like = ON;");
        ArrayList<Word> words = new ArrayList<>();
        if (searchString.length() > 0) {
            ArrayList<Cursor> cl = new ArrayList<>();
            cl.add(db.handleQuery("SELECT Word_WID FROM wordwriting ww WHERE ww.writing LIKE '" + searchString + "%' ORDER BY length(ww.writing) LIMIT 100;"));
            cl.add(db.handleQuery("SELECT Word_WID FROM wordreading wr WHERE wr.reading LIKE '" + searchString + "%' ORDER BY length(wr.reading) LIMIT 100;"));
            cl.add(db.handleQuery("SELECT Word_WID FROM wordmeaning m WHERE m.meaning LIKE '" + searchString + "%' ORDER BY length(m.meaning) LIMIT 100;"));

            for (int i = 0; i < cl.size(); i += 1) {
                while (cl.get(i).moveToNext()) {
                    c = db.handleQuery("SELECT * FROM word WHERE WID = " + cl.get(i).getString(0) + ";");
                    words.addAll(new DatabaseModelLoader().getWordsFromCursor(c));
                }
            }

            for (int i = 0; i < words.size(); i += 1) {
                c = db.handleQuery("SELECT * FROM wordmeaning WHERE Word_WID = '" + words.get(i).getWID() + "';");
                words.get(i).setTranslations(new DatabaseModelLoader().getWordMeaningsFromCursor(c));
                c = db.handleQuery("SELECT * FROM wordwriting WHERE Word_WID = " + words.get(i).getWID() + ";");
                words.get(i).setWordWritings(new DatabaseModelLoader().getWordWritingsFromCursor(c));
                c = db.handleQuery("SELECT * FROM wordreading WHERE Word_WID = " + words.get(i).getWID() + ";");
                words.get(i).setWordReadings(new DatabaseModelLoader().getWordReadingsFromCursor(c));
            }
        }

        db.closeDatabase();

        if (!Thread.interrupted()) {
            dictionary.setWords(words);
            dictionary.handler.sendMessage(dictionary.handler.obtainMessage());
        }

    }
}
