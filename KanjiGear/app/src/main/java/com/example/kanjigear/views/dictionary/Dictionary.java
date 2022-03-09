package com.example.kanjigear.views.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.dataModels.WordTranslation;
import com.example.kanjigear.db.DatabaseModelLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.example.kanjigear.views.components.WordView;

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
        Intent intent = new Intent(this, WordView.class);
        intent.putExtra("WID", WID);
        startActivity(intent);
    }

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
                DatabaseModelLoader loader = new DatabaseModelLoader();
                words.addAll(loader.getWordsFromCursor(c));
            }
            db.closeDatabase();
            addTranslationsToWords();
        }
    }

    public void addTranslationsToWords() {
        db.openDatabase();
        for (int i = 0; i < words.size(); i += 1) {
            Cursor c = db.handleQuery("SELECT * FROM wordmeaning WHERE Word_WID = '" + words.get(i).getWID() + "';");
            words.get(i).setTranslations(new DatabaseModelLoader().getWordTranslationsFromCursor(c));
        }
        db.closeDatabase();
    }
}