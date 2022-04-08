package com.example.kanjigear.views.dictionary;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.LearnElement;
import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseContentLoader;
import com.example.kanjigear.db.DatabaseModelLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.example.kanjigear.views.dictionary.Dictionary;
import com.example.kanjigear.views.dictionary.RecyclerAdapterWord;

import java.util.ArrayList;
import java.util.Locale;

public class DictElementSearch extends Thread {

    private Dictionary dictionary;
    private String searchString;
    private String elementType;
    private DatabaseOpenHelper db;

    public DictElementSearch(Dictionary d, String s, String type) {
        dictionary = d;
        searchString = s;
        elementType = type;
        db = new DatabaseOpenHelper(dictionary.getApplicationContext());
    }

    @Override
    public void run() {
        if (elementType.equals("words")) {
            ArrayList<Word> words = searchWords();
            if (!Thread.interrupted()) {
                dictionary.setWords(words);
                dictionary.handler.sendMessage(dictionary.handler.obtainMessage());
            }
        }
        if (elementType.equals("kanji")) {
            ArrayList<Kanji> kanji = searchKanji();
            if (!Thread.interrupted()) {
                dictionary.setKanji(kanji);
                dictionary.handler.sendMessage(dictionary.handler.obtainMessage());
            }
        }
        if (elementType.equals("sentences")) {
            ArrayList<Sentence> sentences = searchSentences();
            if (!Thread.interrupted()) {
                dictionary.setSentences(sentences);
                dictionary.handler.sendMessage(dictionary.handler.obtainMessage());
            }
        }

    }

    public ArrayList<Word> searchWords() {
        ArrayList<Word> words = new ArrayList<>();
        if (searchString.length() > 0) {
            db.openDatabaseRead();
            ArrayList<Cursor> cl = new ArrayList<>();
            cl.add(db.handleQuery("SELECT w.WID FROM wordwriting ww, word w WHERE w.WID = ww.Word_WID AND ww.writing LIKE '" + searchString + "%' " +
                    "ORDER BY length(ww.writing), w.frequency DESC LIMIT 50;"));
            cl.add(db.handleQuery("SELECT w.WID FROM wordreading wr, word w WHERE w.WID = wr.Word_WID AND wr.reading LIKE '" + searchString + "%' " +
                    "ORDER BY length(wr.reading), w.frequency DESC LIMIT 50;"));
            cl.add(db.handleQuery("SELECT w.WID FROM wordmeaning m, word w WHERE w.WID = m.Word_WID AND m.meaning LIKE '" + searchString + "%' " +
                    "ORDER BY length(m.meaning), w.frequency DESC LIMIT 50;"));

            ArrayList<String> ids = new ArrayList<>();
            for (int i = 0; i < cl.size(); i += 1) {
                while (cl.get(i).moveToNext()) {
                    ids.add(cl.get(i).getString(0));
                }
            }
            db.closeDatabase();

            DatabaseContentLoader loader = new DatabaseContentLoader();
            for (int i = 0; i < ids.size(); i += 1) {
                words.add(loader.getWord(db, ids.get(i)));
            }
        }

        return words;
    }

    public ArrayList<Kanji> searchKanji() {
        ArrayList<Kanji> kanji = new ArrayList<>();
        if (searchString.length() > 0) {
            db.openDatabaseRead();
            ArrayList<Cursor> cl = new ArrayList<>();
            cl.add(db.handleQuery("SELECT symbol FROM kanji WHERE symbol = '" + searchString + "' ORDER BY frequency DESC LIMIT 50;"));
            cl.add(db.handleQuery("SELECT k.symbol FROM kanjireading kr, kanji k WHERE k.symbol = kr.Kanji_symbol AND kr.reading LIKE '" + searchString + "%' " +
                    "ORDER BY length(kr.reading), k.frequency DESC LIMIT 50;"));
            cl.add(db.handleQuery("SELECT k.symbol FROM kanjimeaning m, kanji k WHERE k.symbol = m.Kanji_symbol AND m.meaning LIKE '" + searchString + "%' " +
                    "ORDER BY length(m.meaning), k.frequency DESC LIMIT 50;"));

            ArrayList<String> ids = new ArrayList<>();
            for (int i = 0; i < cl.size(); i += 1) {
                while (cl.get(i).moveToNext()) {
                    ids.add(cl.get(i).getString(0));
                }
            }
            db.closeDatabase();

            DatabaseContentLoader loader = new DatabaseContentLoader();
            for (int i = 0; i < ids.size(); i += 1) {
                kanji.add(loader.getKanji(db, ids.get(i)));
            }
        }

        return kanji;
    }

    public ArrayList<Sentence> searchSentences() {
        ArrayList<Sentence> sentences = new ArrayList<>();
        if (searchString.length() > 0) {
            db.openDatabaseRead();
            ArrayList<Cursor> cl = new ArrayList<>();
            cl.add(db.handleQuery("SELECT SID FROM sentence WHERE text LIKE '%" + searchString + "%' LIMIT 50;"));
            cl.add(db.handleQuery("SELECT s.SID FROM sentencemeaning m, sentence s WHERE s.SID = m.Sentence_SID AND m.meaning LIKE '%" + searchString + "%' " +
                    "ORDER BY length(m.meaning) LIMIT 50;"));

            ArrayList<String> ids = new ArrayList<>();
            for (int i = 0; i < cl.size(); i += 1) {
                while (cl.get(i).moveToNext()) {
                    ids.add(cl.get(i).getString(0));
                }
            }
            db.closeDatabase();

            DatabaseContentLoader loader = new DatabaseContentLoader();
            for (int i = 0; i < ids.size(); i += 1) {
                sentences.add(loader.getSentence(db, ids.get(i)));
            }
        }

        return sentences;
    }
}
