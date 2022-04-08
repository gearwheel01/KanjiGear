package com.example.kanjigear.views.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.views.components.KanjiView;
import com.example.kanjigear.views.components.RecyclerAdapterKanji;
import com.example.kanjigear.views.components.SentenceView;
import com.example.kanjigear.views.components.WordView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Dictionary extends AppCompatActivity {

    private RecyclerView viewList;
    private TabLayout viewTab;
    private EditText viewSearch;
    private Button viewAdd;

    private ArrayList<Kanji> kanji;
    private ArrayList<Word> words;
    private ArrayList<Sentence> sentences;

    private DictElementSearch searchThread;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        viewList = findViewById(R.id.wordsList);
        viewSearch = findViewById(R.id.search);
        viewTab = findViewById(R.id.dictionaryTab);
        viewAdd = findViewById(R.id.dictionaryAddSentence);

        kanji = new ArrayList<>();
        words = new ArrayList<>();
        sentences = new ArrayList<>();
        searchThread = null;

        viewList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewList.setItemAnimator(new DefaultItemAnimator());
        selectTabUpdate("words");

        viewSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                startSearch();
            }
        });

        viewTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTabUpdate((String)tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void selectTabUpdate(String tab) {
        type = tab;
        viewAdd.setVisibility(View.INVISIBLE);
        if (type.equals("sentences")) {
            viewAdd.setVisibility(View.VISIBLE);
        }
        startSearch();
    }

    public void setAdapter() {
        if (type.equals("words")) {
            RecyclerAdapterWord adapter = new RecyclerAdapterWord(this, words);
            viewList.setAdapter(adapter);
        }
        if (type.equals("kanji")) {
            RecyclerAdapterKanji adapter = new RecyclerAdapterKanji(this, kanji);
            viewList.setAdapter(adapter);
        }
        if (type.equals("sentences")) {
            RecyclerAdapterSentence adapter = new RecyclerAdapterSentence(this, sentences);
            viewList.setAdapter(adapter);
        }
    }

    public void openWord(String WID) {
        Intent intent = new Intent(this, WordView.class);
        intent.putExtra("WID", WID);
        if (getIntent().hasExtra("SLID")) {
            intent.putExtra("SLID", getIntent().getStringExtra("SLID"));
        }
        startActivity(intent);
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

    public void openAddNewSentence(View v) {
        Intent intent = new Intent(this, AddNewSentence.class);
        if (getIntent().hasExtra("SLID")) {
            intent.putExtra("SLID", getIntent().getStringExtra("SLID"));
        }
        startActivity(intent);
    }

    public void startSearch() {
        if (searchThread != null) {
            searchThread.interrupt();
        }
        searchThread = new DictElementSearch(this, String.valueOf(viewSearch.getText()), type);
        searchThread.start();
    }

    public void setKanji(ArrayList<Kanji> kanji) {
        this.kanji = kanji;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public void setSentences(ArrayList<Sentence> sentences) {
        this.sentences = sentences;
    }

    public final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            searchThread = null;
            setAdapter();
        }
    };

}