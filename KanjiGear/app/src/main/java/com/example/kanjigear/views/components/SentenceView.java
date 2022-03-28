package com.example.kanjigear.views.components;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kanjigear.R;
import com.example.kanjigear.alerts.AddToList;
import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.dataModels.StudyList;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseContentLoader;
import com.example.kanjigear.db.DatabaseModelLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.example.kanjigear.views.dictionary.RecyclerAdapterWord;
import com.example.kanjigear.views.lesson.DrawKanji;

import java.util.ArrayList;

public class SentenceView extends AppCompatActivity {

    private TextView viewText;
    private TextView viewMeaning;
    private RecyclerView viewListWords;
    private Button viewAddlist;

    private DatabaseOpenHelper db;
    private Sentence sentence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence_view);
        loadResources();

        Intent intent = getIntent();
        loadSentence(intent.getStringExtra("SID"));

        viewText.setText(sentence.getText());
        viewMeaning.setText(sentence.getMeanings().get(0).getMeaning());

        if (intent.hasExtra("SLID")) {
            updateAddlistButton();
        }

        viewListWords.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewListWords.setItemAnimator(new DefaultItemAnimator());
        RecyclerAdapterWord adapter = new RecyclerAdapterWord(this, sentence.getWords());
        viewListWords.setAdapter(adapter);
    }

    public void loadResources() {
        db = new DatabaseOpenHelper(getApplicationContext());
        viewText = findViewById(R.id.sentenceViewText);
        viewMeaning = findViewById(R.id.sentenceViewMeaning);
        viewListWords = findViewById(R.id.sentenceViewListWords);
        viewAddlist = findViewById(R.id.sentenceViewAddlist);
    }

    public void loadSentence(String SID) {
        sentence = new DatabaseContentLoader().getSentence(db, SID);
    }

    public void openWord(Word w) {
        Intent intent = new Intent(this, WordView.class);
        intent.putExtra("WID", w.getWID());
        intent.putExtra("writingIndex", w.getSentenceWritingIndex());
        if (getIntent().hasExtra("SLID")) {
            intent.putExtra("SLID", getIntent().getStringExtra("SLID"));
        }
        startActivity(intent);
    }

    public boolean isSentenceInList(String SLID) {
        db.openDatabaseRead();
        boolean ret = (db.handleQuery("SELECT * FROM listcontainssentence WHERE Sentence_SID = " + sentence.getSID() + " AND StudyList_SLID = " + SLID + ";").getCount() > 0);
        db.closeDatabase();
        return ret;
    }
    public void updateAddlistButton() {
        viewAddlist.setBackgroundResource(isSentenceInList(getIntent().getStringExtra("SLID"))?
                R.drawable.button_listadded:R.drawable.button_listadd);
    }

    public void addToList(View v) {
        if (getIntent().hasExtra("SLID")) {
            String SLID = getIntent().getStringExtra("SLID");
            if (isSentenceInList(SLID)) {
                db.openDatabase();
                db.delete("listcontainssentence", "StudyList_SLID", SLID, "Sentence_SID", sentence.getSID());
                db.closeDatabase();
            } else {
                ContentValues values = new ContentValues();
                values.put("StudyList_SLID", SLID);
                values.put("Sentence_SID", sentence.getSID());
                db.openDatabase();
                db.insert("listcontainssentence", values);
                db.closeDatabase();
            }
            updateAddlistButton();
        } else {
            AddToList dialog = new AddToList(this, db, sentence);
            dialog.showDialog(this);
        }
    }

    public void openDrawKanji(View v) {
        if (sentence.getWords().size() > 0) {
            // TODO check for kanji in word size > 0
            Word word = sentence.getWords().get(0);
            int writingIndex = 0;
            Intent intent = new Intent(this, DrawKanji.class);
            intent.putExtra("SID", sentence.getSID());
            intent.putExtra("WID", word.getWID());
            intent.putExtra("symbol", word.getKanjiInWord(writingIndex).get(0) + "");
            intent.putExtra("writingIndex", writingIndex);
            startActivity(intent);
        }
    }
}