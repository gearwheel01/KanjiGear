package com.example.kanjigear.views.studyLists;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.dataModels.StudyList;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseContentLoader;
import com.example.kanjigear.db.DatabaseModelLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.example.kanjigear.views.components.KanjiView;
import com.example.kanjigear.views.components.RecyclerAdapterKanji;
import com.example.kanjigear.views.components.SentenceView;
import com.example.kanjigear.views.components.WordView;
import com.example.kanjigear.views.dictionary.Dictionary;
import com.example.kanjigear.views.dictionary.RecyclerAdapterSentence;
import com.example.kanjigear.views.dictionary.RecyclerAdapterWord;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class StudyListDetails extends AppCompatActivity {

    private DatabaseOpenHelper db;
    private StudyList list;

    private EditText viewName;
    private Button viewActivate;
    private TabLayout viewTab;
    private Button viewAdd;
    private RecyclerView viewList;

    private ArrayList<Kanji> kanji = null;
    private ArrayList<Word> words = null;
    private ArrayList<Sentence> sentences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_list_details);

        viewName = findViewById(R.id.listViewName);
        viewActivate = findViewById(R.id.listViewActivate);
        viewTab = findViewById(R.id.listViewTab);
        viewAdd = findViewById(R.id.listViewAdd);
        viewList = findViewById(R.id.listViewList);

        Intent intent = getIntent();
        db = new DatabaseOpenHelper(getApplicationContext());
        getStudyList(intent.getStringExtra("listId"));

        viewList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewList.setItemAnimator(new DefaultItemAnimator());
        updateActiveView();

        setListWords();

        viewName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateName();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals(getResources().getString(R.string.tabKanji))) {
                    setListKanji();
                }
                if (tab.getText().equals(getResources().getString(R.string.tabWords))) {
                    setListWords();
                }
                if (tab.getText().equals(getResources().getString(R.string.tabSentences))) {
                    setListSentences();
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

    public void getStudyList(String SLID) {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT * FROM studylist WHERE SLID = '" + SLID + "';");
        list = new DatabaseModelLoader().getStudyListsFromCursor(c).get(0);
        db.closeDatabase();
        viewName.setText(list.getName());
    }

    public void deleteList(View v) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        delete();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to delete list '" + list.getName() + "'?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void delete() {
        db.openDatabase();
        db.delete("studylist","SLID",list.getSLID());
        db.closeDatabase();
        finish();
    }

    public void updateName() {
        db.openDatabase();
        ContentValues values = new ContentValues();
        values.put("name", String.valueOf(viewName.getText()));
        db.update("studylist",values, "SLID", list.getSLID());
        list.setName(String.valueOf(viewName.getText()));
        db.closeDatabase();
    }

    public void toggleActive(View v) {
        if (list.isActive())    {list.setActive(false);}
        else                    {list.setActive(true);}
        db.openDatabase();
        ContentValues values = new ContentValues();
        values.put("isActive", list.isActive()?1:0);
        db.update("studylist",values, "SLID", list.getSLID());
        db.closeDatabase();
        updateActiveView();
    }

    public void updateActiveView() {
        viewActivate.setBackgroundResource(list.isActive()?R.color.button_green:R.color.button_gray);
        viewActivate.setText(list.isActive()?"deactivate":"activate");
    }


    public void loadKanji() {
        kanji = new DatabaseContentLoader().getKanjiInList(db, list.getSLID());
    }
    public void loadWords() {
        words = new DatabaseContentLoader().getWordsInList(db, list.getSLID());
    }
    public void loadSentences() {
        sentences = new DatabaseContentLoader().getSentencesInList(db, list.getSLID());
    }


    public void setListKanji() {
        if (kanji == null) {
            loadKanji();
        }
        viewList.setAdapter(new RecyclerAdapterKanji(this, kanji));
    }
    public void setListWords() {
        if (words == null) {
            loadWords();
        }
        viewList.setAdapter(new RecyclerAdapterWord(this, words));
    }
    public void setListSentences() {
        if (sentences == null) {
            loadSentences();
        }
        viewList.setAdapter(new RecyclerAdapterSentence(this, sentences));
    }


    public void addItems(View v) {
        Intent intent = new Intent(this, Dictionary.class);
        intent.putExtra("SLID", list.getSLID());
        startActivity(intent);
    }
    public void openWord(Word w) {
        Intent intent = new Intent(this, WordView.class);
        intent.putExtra("WID", w.getWID());
        intent.putExtra("writingIndex", w.getWritingIndex());
        intent.putExtra("SLID", list.getSLID());
        startActivity(intent);
    }
    public void openKanji(String symbol) {
        Intent intent = new Intent(this, KanjiView.class);
        intent.putExtra("symbol", symbol);
        intent.putExtra("SLID", list.getSLID());
        startActivity(intent);
    }
    public void openSentence(Sentence sentence) {
        Intent intent = new Intent(this, SentenceView.class);
        intent.putExtra("SID", sentence.getSID());
        intent.putExtra("SLID", list.getSLID());
        startActivity(intent);
    }
}