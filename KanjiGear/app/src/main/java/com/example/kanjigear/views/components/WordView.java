package com.example.kanjigear.views.components;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseModelLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;

import java.util.ArrayList;

public class WordView extends AppCompatActivity {

    private Word word;
    private ArrayList<Kanji> kanjiInWord;
    private DatabaseOpenHelper db;

    private TextView viewTitle;
    private TextView viewReading;
    private TextView viewMeaning;
    private RecyclerView viewListKanji;

    private String notkanjichars=" あいうえおかきくけこがぎぐげごさしすせそざじずぜぞたちつてとだぢづでどなにぬねのはひふへほばびぶべぼぱぴぷぺぽまみむめもやゆよらりるれろわをんっゃょゅぁぃぅぇぉゖゕ"
        + "アイウエオカキクケコガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨラリルレロワヲンーャョュァィゥェォヵヶッ"
        +"abcdefghaijklmnopqrstuvwxvzöäüABCDEFGHIJKLMNOPQRSTUVWXYZÖÄÜ1234567890<>|-_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_view);
        Intent intent = getIntent();

        db = new DatabaseOpenHelper(getApplicationContext());
        viewTitle = findViewById(R.id.wordViewWord);
        viewListKanji = findViewById(R.id.kanjiViewListWords);
        viewReading = findViewById(R.id.wordViewReading);
        viewMeaning = findViewById(R.id.wordViewMeaning);

        loadWord(intent.getStringExtra("WID"));
        if (word.getWordWritings().size() > 0) {
            viewTitle.setText(word.getWordWritings().get(0));
        }
        if (word.getWordReadings().size() > 0) {
            viewReading.setText(word.getWordReadings().get(0));
        }
        viewMeaning.setText(word.getTranslationString(""));

        viewListKanji.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        viewListKanji.setItemAnimator(new DefaultItemAnimator());
        viewListKanji.setAdapter(new RecyclerAdapterKanji(this, kanjiInWord));
    }


    public void openKanji(String symbol) {
        Intent intent = new Intent(this, KanjiView.class);
        intent.putExtra("symbol", symbol);
        startActivity(intent);
    }

    public void loadWord(String WID) {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT * FROM word WHERE WID = '" + WID + "';");
        word = new DatabaseModelLoader().getWordsFromCursor(c).get(0);
        word.setWordWritings(new DatabaseModelLoader().getWordWritingsFromCursor(db.handleQuery("SELECT * FROM wordwriting WHERE Word_WID = " + WID + ";")));
        word.setWordReadings(new DatabaseModelLoader().getWordReadingsFromCursor(db.handleQuery("SELECT * FROM wordreading WHERE Word_WID = " + WID + ";")));

        c = db.handleQuery("SELECT * FROM wordmeaning WHERE Word_WID = '" + word.getWID() + "';");
        word.setTranslations(new DatabaseModelLoader().getWordMeaningsFromCursor(c));

        getKanjiInWord();

        db.closeDatabase();
    }

    public void getKanjiInWord() {
        kanjiInWord = new ArrayList<>();
        ArrayList<Character> kanji = new ArrayList<>();
        for(int i = 0; i < word.getWordWritings().size(); i += 1) {
            String writing = word.getWordWritings().get(i);
            for (int c = 0; c < writing.length(); c += 1) {
                char symbol = writing.charAt(c);
                if ( (!notkanjichars.contains(symbol + "")) && (!kanji.contains(symbol)) ) {
                    kanji.add(symbol);
                }
            }
        }


        for (int i = 0; i < kanji.size();  i += 1) {
            Cursor c = db.handleQuery("SELECT * FROM kanji WHERE symbol = '" + kanji.get(i) + "';");
            kanjiInWord.addAll(new DatabaseModelLoader().getKanjiFromCursor(c));
        }
    }


}