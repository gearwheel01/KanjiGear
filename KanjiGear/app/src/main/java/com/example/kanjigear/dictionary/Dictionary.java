package com.example.kanjigear.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.dataModels.WordTranslation;
import com.example.kanjigear.db.DatabaseOpenHelper;

import java.util.ArrayList;
import java.util.Locale;

public class Dictionary extends AppCompatActivity {

    private DatabaseOpenHelper db;
    private RecyclerView list;
    private ArrayList<Word> words;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        db = new DatabaseOpenHelper(getApplicationContext());
        list = findViewById(R.id.wordsList);
        search = findViewById(R.id.search);

        list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list.setItemAnimator(new DefaultItemAnimator());
        setAdapter();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setAdapter();
            }
        });
    }

    private void setAdapter() {
        getWords(String.valueOf(search.getText()));
        RecyclerAdapterWord adapter = new RecyclerAdapterWord(this, words);
        list.setAdapter(adapter);
    }


    public void openWord(String WID) {

    }

    @SuppressLint("Range")
    public void getWords(String searchString) {
        words = new ArrayList<Word>();
        if (searchString.length() > 0) {
            searchString = searchString.toUpperCase(Locale.ROOT);
            db.openDatabase();
            ArrayList<Cursor> cl = new ArrayList<Cursor>();
            cl.add(db.handleQuery("SELECT * FROM word WHERE word LIKE '" + searchString + "%' OR pronunciation LIKE '" +
                    searchString + "%' OR UPPER(romaji) LIKE '" + searchString + "%' LIMIT 10;"));
            cl.add(db.handleQuery("SELECT w.* FROM word w, wordmeaning m WHERE m.Word_WID = w.WID AND UPPER(m.meaning) LIKE '" + searchString + "%' LIMIT 10;"));

            for (int i = 0; i < cl.size(); i += 1) {
                Cursor c = cl.get(i);
                c.moveToFirst();
                for (int ci = 0; ci < c.getCount(); ci += 1) {
                    String WID = c.getString(c.getColumnIndex("WID"));
                    String word = c.getString(c.getColumnIndex("word"));
                    String grade = c.getString(c.getColumnIndex("grade"));
                    int learningProgress = c.getInt(c.getColumnIndex("learningProgress"));
                    String pronunciation = c.getString(c.getColumnIndex("pronunciation"));
                    String romaji = c.getString(c.getColumnIndex("romaji"));
                    words.add(new Word(WID, word, grade, learningProgress, pronunciation, romaji));
                    c.moveToNext();
                }
                c.close();

            }
            db.closeDatabase();
            addTranslationsToWords();
        }
    }

    @SuppressLint("Range")
    public void addTranslationsToWords() {
        db.openDatabase();
        for (int i = 0; i < words.size(); i += 1) {
            Cursor c = db.handleQuery("SELECT * FROM wordmeaning WHERE Word_WID = '" + words.get(i).getWID() + "';");
            c.moveToFirst();
            for (int a = 0; a < c.getCount(); a += 1) {
                String WMID = c.getString(c.getColumnIndex("WMID"));
                String translation = c.getString(c.getColumnIndex("meaning"));
                String language = c.getString(c.getColumnIndex("language"));
                words.get(i).addTranslation(new WordTranslation(WMID, translation, language));
                c.moveToNext();
            }
            c.close();
        }
        db.closeDatabase();
    }
}