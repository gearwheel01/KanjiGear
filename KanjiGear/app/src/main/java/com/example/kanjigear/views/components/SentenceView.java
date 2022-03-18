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
import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseContentLoader;
import com.example.kanjigear.db.DatabaseModelLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.example.kanjigear.views.dictionary.RecyclerAdapterWord;

import java.util.ArrayList;

public class SentenceView extends AppCompatActivity {

    private TextView viewText;
    private TextView viewMeaning;
    private RecyclerView viewListWords;

    private DatabaseOpenHelper db;
    private Sentence sentence;
    private ArrayList<Word> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence_view);
        db = new DatabaseOpenHelper(getApplicationContext());

        viewText = findViewById(R.id.sentenceViewText);
        viewMeaning = findViewById(R.id.sentenceViewMeaning);
        viewListWords = findViewById(R.id.sentenceViewListWords);

        Intent intent = getIntent();
        loadSentence(intent.getStringExtra("SID"));

        viewText.setText(sentence.getText());
        viewMeaning.setText(sentence.getMeanings().get(0).getMeaning());

        viewListWords.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewListWords.setItemAnimator(new DefaultItemAnimator());
        RecyclerAdapterWord adapter = new RecyclerAdapterWord(this, words);
        viewListWords.setAdapter(adapter);
    }

    public void loadSentence(String SID) {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT * FROM sentence WHERE SID = " + SID + ";");
        sentence = new DatabaseContentLoader().addDetailsToSentences(db, new DatabaseModelLoader().getSentencesFromCursor(c)).get(0);
        loadWords();
        db.closeDatabase();
    }

    public void loadWords() {
        Cursor c = db.handleQuery("SELECT w.* FROM word w, sentencecontainsword sw WHERE w.WID = sw.Word_WID AND sw.Sentence_SID = " + sentence.getSID() + ";");
        words = new DatabaseContentLoader().addDetailsToWords(db, new DatabaseModelLoader().getWordsFromCursor(c));
    }

    public void openWord(String WID) {
        Intent intent = new Intent(this, WordView.class);
        intent.putExtra("WID", WID);
        startActivity(intent);
    }
}