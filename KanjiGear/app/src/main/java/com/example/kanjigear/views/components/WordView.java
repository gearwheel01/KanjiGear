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
    private TextView viewDetails;
    private RecyclerView viewListKanji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_view);
        Intent intent = getIntent();

        db = new DatabaseOpenHelper(getApplicationContext());
        viewTitle = findViewById(R.id.wordViewWord);
        viewListKanji = findViewById(R.id.kanjiViewListWords);
        viewDetails = findViewById(R.id.wordViewDetails);

        loadWord(intent.getStringExtra("WID"));
        viewTitle.setText(word.getWord());
        viewDetails.setText(word.getPronunciation() + " - " + word.getTranslationString(""));

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
        db.openDatabase();
        Cursor c = db.handleQuery("SELECT * FROM word WHERE WID = '" + WID + "';");
        word = new DatabaseModelLoader().getWordsFromCursor(c).get(0);

        c = db.handleQuery("SELECT * FROM wordmeaning WHERE WMID = '" + word.getWID() + "';");
        word.setTranslations(new DatabaseModelLoader().getWordTranslationsFromCursor(c));

        c = db.handleQuery("SELECT k.* FROM kanji k, wordwrittenwithkanji w WHERE k.symbol = w.Kanji_symbol AND w.Word_WID = " + word.getWID() + ";");
        kanjiInWord = new DatabaseModelLoader().getKanjiFromCursor(c);

        db.closeDatabase();
    }
}