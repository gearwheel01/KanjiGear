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
       // db.handleQuery("PRAGMA case_sensitive_like = ON;");
        ArrayList<Word> words = new ArrayList<>();
        if (searchString.length() > 0) {
            ArrayList<Cursor> cl = new ArrayList<>();
            cl.add(db.handleQuery("SELECT w.WID FROM wordwriting ww, word w WHERE w.WID = ww.Word_WID AND ww.writing LIKE '" + searchString + "%' " +
                    "ORDER BY length(ww.writing), w.frequency DESC LIMIT 50;"));
            cl.add(db.handleQuery("SELECT w.WID FROM wordreading wr, word w WHERE w.WID = wr.Word_WID AND wr.reading LIKE '" + searchString + "%' " +
                    "ORDER BY length(wr.reading), w.frequency DESC LIMIT 50;"));
            cl.add(db.handleQuery("SELECT w.WID FROM wordmeaning m, word w WHERE w.WID = m.Word_WID AND m.meaning LIKE '" + searchString + "%' " +
                    "ORDER BY length(m.meaning), w.frequency DESC LIMIT 50;"));

            for (int i = 0; i < cl.size(); i += 1) {
                while (cl.get(i).moveToNext()) {
                    c = db.handleQuery("SELECT * FROM word WHERE WID = " + cl.get(i).getString(0) + ";");
                    words.addAll(new DatabaseModelLoader().getWordsFromCursor(c));
                }
            }
            words = new DatabaseContentLoader().addDetailsToWords(db, words);
        }

        db.closeDatabase();

        if (!Thread.interrupted()) {
            dictionary.setWords(words);
            dictionary.handler.sendMessage(dictionary.handler.obtainMessage());
        }

    }
}
