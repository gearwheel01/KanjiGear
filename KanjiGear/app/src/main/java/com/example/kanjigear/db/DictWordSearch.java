package com.example.kanjigear.db;

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
    private DatabaseOpenHelper db;
    private boolean stopped = false;

    public DictWordSearch(Dictionary d, String s) {
        dictionary = d;
        searchString = s;
        db = new DatabaseOpenHelper(d.getApplicationContext());
    }

    @Override
    public void run() {
        db.openDatabaseRead();
        ArrayList<Word> words = new ArrayList<>();
        if (searchString.length() > 0) {
            searchString = searchString.toUpperCase(Locale.ROOT);
            ArrayList<Cursor> cl = new ArrayList<Cursor>();
            cl.add(db.handleQuery("SELECT w.* FROM word w, wordwriting ww WHERE ww.Word_WID = w.WID AND UPPER(ww.writing) LIKE '" + searchString + "%' ORDER BY length(ww.writing) LIMIT 10;"));
            cl.add(db.handleQuery("SELECT w.* FROM word w, wordreading wr WHERE wr.Word_WID = w.WID AND UPPER(wr.reading) LIKE '" + searchString + "%' ORDER BY length(wr.reading) LIMIT 10;"));
            cl.add(db.handleQuery("SELECT w.* FROM word w, wordmeaning m WHERE m.Word_WID = w.WID AND UPPER(m.meaning) LIKE '" + searchString + "%' ORDER BY length(m.meaning) LIMIT 10;"));

            for (int i = 0; i < cl.size(); i += 1) {
                Cursor c = cl.get(i);
                DatabaseModelLoader loader = new DatabaseModelLoader();
                words.addAll(loader.getWordsFromCursor(c));
            }

            for (int i = 0; i < words.size(); i += 1) {
                Cursor c = db.handleQuery("SELECT * FROM wordmeaning WHERE Word_WID = '" + words.get(i).getWID() + "';");
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
