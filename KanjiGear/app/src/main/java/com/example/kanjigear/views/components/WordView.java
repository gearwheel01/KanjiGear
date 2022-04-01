package com.example.kanjigear.views.components;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kanjigear.R;
import com.example.kanjigear.alerts.AddToList;
import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.dataModels.StudyList;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseContentLoader;
import com.example.kanjigear.db.DatabaseModelLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.example.kanjigear.views.dictionary.RecyclerAdapterSentence;
import com.example.kanjigear.views.lesson.DrawKanji;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class WordView extends AppCompatActivity {

    private Word word;
    private ArrayList<Kanji> kanjiInWord;
    private DatabaseOpenHelper db;
    private int writingIndex;
    private ArrayList<Sentence> sentencesLearned;
    private ArrayList<Sentence> sentencesNew;

    private TextView viewTitle;
    private TextView viewReading;
    private TextView viewMeaning;
    private Button viewWritingAlt;
    private RecyclerView viewListKanji;
    private TextView viewListKanjiBG;
    private TextView viewKanjiTitle;
    private TabLayout viewTabSentence;
    private RecyclerView viewListSentences;
    private Button viewAddlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_view);
        loadResources();

        Intent intent = getIntent();
        viewListKanji.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        viewListKanji.setItemAnimator(new DefaultItemAnimator());

        loadWord(intent.getStringExtra("WID"));
        loadSentences();

        int wi = 0;
        if (intent.hasExtra("writingIndex")) {
            wi = intent.getIntExtra("writingIndex",0);
        }
        updateWriting(wi);
        viewMeaning.setText(word.getTranslationString(""));

        if (word.getWordWritings().size() < 2) {
            viewWritingAlt.setVisibility(View.INVISIBLE);
        }
        if (intent.hasExtra("SLID")) {
            updateAddlistButton();
        }

        viewListSentences.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewListSentences.setItemAnimator(new DefaultItemAnimator());
        setAdapterSentences(sentencesLearned);

        viewTabSentence.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals(getResources().getString(R.string.tabNew))) {
                    setAdapterSentences(sentencesNew);
                } else {
                    setAdapterSentences(sentencesLearned);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void loadResources() {
        db = new DatabaseOpenHelper(getApplicationContext());
        viewTitle = findViewById(R.id.wordViewWord);
        viewListKanji = findViewById(R.id.kanjiViewListKanji);
        viewReading = findViewById(R.id.wordViewReading);
        viewMeaning = findViewById(R.id.wordViewMeaning);
        viewWritingAlt = findViewById(R.id.wordViewWritingAlt);
        viewKanjiTitle = findViewById(R.id.wordViewKanjiTitle);
        viewListKanjiBG = findViewById(R.id.kanjiViewWordsBG);
        viewTabSentence = findViewById(R.id.wordViewTabSentence);
        viewListSentences = findViewById(R.id.wordViewListSentences);
        viewAddlist = findViewById(R.id.wordViewAddlist);
    }


    public void loadWord(String WID) {
        word = new DatabaseContentLoader().getWord(db, WID);
    }

    public void getKanjiInWord() {
        db.openDatabaseRead();
        kanjiInWord = new ArrayList<>();
        ArrayList<Character> kanji = word.getKanjiInWord(writingIndex);
        for (int i = 0; i < kanji.size();  i += 1) {
            Cursor c = db.handleQuery("SELECT * FROM kanji WHERE symbol = '" + kanji.get(i) + "';");
            kanjiInWord.addAll(new DatabaseModelLoader().getKanjiFromCursor(c));
        }

        viewListKanji.setAdapter(new RecyclerAdapterKanji(this, kanjiInWord));
        db.closeDatabase();
    }

    public void selectWriting(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("select writing");
        builder.setItems(wordWritingsToArray(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateWriting(which);
            }
        });
        builder.show();
    }

    public String[] wordWritingsToArray() {
        String[] writings = new String[word.getWordWritings().size()];
        for (int i = 0; i < word.getWordWritings().size(); i += 1) {
            writings[i] = word.getWordWritings().get(i);
        }
        return writings;
    }

    public void updateWriting(int i) {
        writingIndex = i;
        if (word.getWordWritings().size() > 0) {
            viewTitle.setText(word.getWordWritings().get(i));
            viewReading.setText(word.getWordReadings().get(0));
            getKanjiInWord();
            if (getIntent().hasExtra("SID")) {
                saveWordWritingInSentence();
            }
        } else {
            viewTitle.setText(word.getWordReadings().get(i));
            viewReading.setVisibility(View.INVISIBLE);
            viewReading.setHeight(0);
            viewListKanjiBG.setVisibility(View.INVISIBLE);
            viewListKanjiBG.setHeight(0);
            viewKanjiTitle.setVisibility(View.INVISIBLE);
            viewKanjiTitle.setHeight(0);
        }
    }

    public void saveWordWritingInSentence() {
        db.openDatabase();
        ContentValues values = new ContentValues();
        values.put("writingIndex", writingIndex);
        db.update("sentencecontainsword", values, "Sentence_SID", getIntent().getStringExtra("SID"), "Word_WID", word.getWID());
        db.closeDatabase();
    }

    public void loadSentences() {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT s.* FROM sentence s, sentencecontainsword sw " +
                "WHERE s.SID = sw.Sentence_SID AND sw.Word_WID = " + word.getWID() + " AND learningProgress > 0;");
        sentencesLearned = new DatabaseModelLoader().getSentencesFromCursor(c);
        sentencesLearned = new DatabaseContentLoader().addDetailsToSentences(db, sentencesLearned);
        c = db.handleQuery("SELECT s.* FROM sentence s, sentencecontainsword sw " +
                "WHERE s.SID = sw.Sentence_SID AND sw.Word_WID = " + word.getWID() + " AND learningProgress = 0;");
        sentencesNew = new DatabaseModelLoader().getSentencesFromCursor(c);
        sentencesNew= new DatabaseContentLoader().addDetailsToSentences(db, sentencesNew);

        db.closeDatabase();
    }

    public void setAdapterSentences(ArrayList<Sentence> sentences) {
        RecyclerAdapterSentence adapter = new RecyclerAdapterSentence(this, sentences);
        viewListSentences.setAdapter(adapter);
    }

    public void openSentence(Sentence sentence) {
        Intent intent = new Intent(this, SentenceView.class);
        intent.putExtra("SID", sentence.getSID());
        if (getIntent().hasExtra("SLID")) {
            intent.putExtra("SLID", getIntent().getStringExtra("SLID"));
        }
        startActivity(intent);
    }
    public void openKanji(String symbol) {
        Intent intent = new Intent(this, KanjiView.class);
        intent.putExtra("symbol", symbol);
        if (getIntent().hasExtra("SLID")) {
            intent.putExtra("SLID", getIntent().getStringExtra("SLID"));
        }
        startActivity(intent);
    }

    public boolean isWordInList(String SLID) {
        db.openDatabaseRead();
        boolean ret = (db.handleQuery("SELECT * FROM listcontainsword WHERE Word_WID = " + word.getWID() + " AND StudyList_SLID = " + SLID + ";").getCount() > 0);
        db.closeDatabase();
        return ret;
    }

    public void updateAddlistButton() {
        viewAddlist.setBackgroundResource(isWordInList(getIntent().getStringExtra("SLID"))?
                R.drawable.button_listadded:R.drawable.button_listadd);
    }


    public void addToList(View v) {
        if (getIntent().hasExtra("SLID")) {
            String SLID = getIntent().getStringExtra("SLID");
            if (isWordInList(SLID)) {
                db.openDatabase();
                db.delete("listcontainsword", "StudyList_SLID", SLID, "Word_WID", word.getWID());
                db.closeDatabase();
            } else {
                ContentValues values = new ContentValues();
                values.put("StudyList_SLID", SLID);
                values.put("Word_WID", word.getWID());
                db.openDatabase();
                db.insert("listcontainsword", values);
                db.closeDatabase();
            }
            updateAddlistButton();
        } else {
            AddToList dialog = new AddToList(this, db, word);
            dialog.showDialog(this);
        }
    }

    public void openDrawKanji(View v) {
        Intent intent = new Intent(this, DrawKanji.class);
        intent.putExtra("symbol", word.getKanjiInWord(writingIndex).get(0) + "");
        intent.putExtra("WID", word.getWID());
        intent.putExtra("writingIndex", writingIndex);
        startActivity(intent);
    }
}