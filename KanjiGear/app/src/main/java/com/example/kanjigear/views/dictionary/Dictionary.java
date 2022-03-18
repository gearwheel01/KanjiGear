package com.example.kanjigear.views.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.dataModels.WordMeaning;
import com.example.kanjigear.db.DatabaseModelLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.example.kanjigear.db.DictWordSearch;
import com.example.kanjigear.db.LoadDatabaseAsync;
import com.example.kanjigear.views.MainActivity;
import com.example.kanjigear.views.components.WordView;

import java.util.ArrayList;
import java.util.Locale;

public class Dictionary extends AppCompatActivity {

    private RecyclerView list;
    private ArrayList<Word> words;
    private EditText search;
    private DictWordSearch searchThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        list = findViewById(R.id.wordsList);
        search = findViewById(R.id.search);
        words = new ArrayList<>();
        searchThread = null;

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
                if (searchThread != null) {
                    searchThread.interrupt();
                }
                startSearch();
            }
        });
    }

    public void setAdapter() {
        RecyclerAdapterWord adapter = new RecyclerAdapterWord(this, words);
        list.setAdapter(adapter);
    }

    public void openWord(String WID) {
        Intent intent = new Intent(this, WordView.class);
        intent.putExtra("WID", WID);
        startActivity(intent);
    }

    public void startSearch() {
        searchThread = new DictWordSearch(this, String.valueOf(search.getText()));
        searchThread.start();
    }

    public void setWords(ArrayList<Word> w) {
        words = w;
    }

    public final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            searchThread = null;
            setAdapter();
        }
    };

}