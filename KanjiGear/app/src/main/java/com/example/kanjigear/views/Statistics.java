package com.example.kanjigear.views;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kanjigear.R;
import com.example.kanjigear.db.DatabaseOpenHelper;

public class Statistics extends AppCompatActivity {

    private DatabaseOpenHelper db;

    private TextView viewKanji;
    private TextView viewWords;
    private TextView viewSentences;

    private final int LEARNED_THRESHHOLD = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        loadResources();
        setStatisticTexts();
    }

    public void loadResources() {
        viewKanji = findViewById(R.id.statisticsKanji);
        viewWords = findViewById(R.id.statisticsWords);
        viewSentences = findViewById(R.id.statisticsSentences);
        db = new DatabaseOpenHelper(getApplicationContext());
    }

    public void setStatisticTexts() {
        viewKanji.setText("Learned Kanji: " + getLearnedKanji());
        viewWords.setText("Learned Words: " + getLearnedWords());
        viewSentences.setText("Learned Sentences: " + getLearnedSentences());
    }

    public int getLearnedKanji() {
        return getLearnedElements("kanji");
    }

    public int getLearnedWords() {
        return getLearnedElements("word");
    }

    public int getLearnedSentences() {
        return getLearnedElements("sentence");
    }

    public int getLearnedElements(String table) {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT * FROM " + table + " WHERE learningProgress >= " + LEARNED_THRESHHOLD + ";");
        int count = c.getCount();
        db.closeDatabase();
        return count;
    }
}