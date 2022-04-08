package com.example.kanjigear.views.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.dataModels.SentenceMeaning;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseOpenHelper;

import java.util.ArrayList;

public class AddNewSentence extends AppCompatActivity {

    private EditText viewText;
    private EditText viewMeaning;
    private EditText viewSearch;
    private RecyclerView viewListAdd;
    private RecyclerView viewListRemove;
    private Button viewSave;

    private DatabaseOpenHelper db;
    private AddSentenceWordSearch searchThread;

    private Sentence sentence;
    private ArrayList<Word> addWords;
    private ArrayList<Word> addedWords;

    private final String DEFAULT_LANG = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_sentence);
        loadResources();

        newSentence();
    }

    public void loadResources() {
        viewText = findViewById(R.id.addSentenceText);
        viewMeaning = findViewById(R.id.addSentenceMeaning);
        viewSearch = findViewById(R.id.addSentenceSearchWord);
        viewListAdd = findViewById(R.id.addSentenceListAdd);
        viewListAdd.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewListAdd.setItemAnimator(new DefaultItemAnimator());
        viewListRemove = findViewById(R.id.addSentenceListRemove);
        viewListRemove.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewListRemove.setItemAnimator(new DefaultItemAnimator());
        viewSave = findViewById(R.id.addSentenceSave);

        viewText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateText();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewMeaning.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateMeaning();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        db = new DatabaseOpenHelper(getApplicationContext());
    }

    public void setAddWords(ArrayList<Word> words) {
        addWords = words;
    }

    public void addWord(Word w) {
        addedWords.add(w);
        addWords.remove(w);
        setAdapterAdded();
        setAdapterAdd();
    }

    public void moveWordUp(Word w) {
        int i = addedWords.indexOf(w);
        Word w1 = addedWords.get(i - 1);
        addedWords.set(i - 1, w);
        addedWords.set(i, w1);
        setAdapterAdded();
    }

    public void moveWordDown(Word w) {
        int i = addedWords.indexOf(w);
        Word w1 = addedWords.get(i + 1);
        addedWords.set(i + 1, w);
        addedWords.set(i, w1);
        setAdapterAdded();
    }


    public void removeWord(Word w) {
        addedWords.remove(w);
        setAdapterAdded();
    }

    public void newSentence() {
        ArrayList<SentenceMeaning> meanings = new ArrayList<>();
        meanings.add(new SentenceMeaning(DEFAULT_LANG, ""));
        sentence = new Sentence("","",0);
        sentence.setMeanings(meanings);
        addWords = new ArrayList<>();
        addedWords = new ArrayList<>();
        viewText.setText("");
        viewMeaning.setText("");
        viewSearch.setText("");
        updateSaveButton();
        setAdapterAdded();
        setAdapterAdd();
    }

    public void updateText() {
        sentence.setText(String.valueOf(viewText.getText()));
        updateSaveButton();
        startSearch();
    }

    public void updateMeaning() {
        ArrayList<SentenceMeaning> meanings = new ArrayList<>();
        meanings.add(new SentenceMeaning(DEFAULT_LANG, String.valueOf(viewMeaning.getText())));
        sentence.setMeanings(meanings);
        updateSaveButton();
        startSearch();
    }

    public void updateSearch() {
        startSearch();
    }

    public void startSearch() {
        if (searchThread != null) {
            searchThread.interrupt();
        }
        searchThread = new AddSentenceWordSearch(this, String.valueOf(viewSearch.getText()), sentence.getText());
        searchThread.start();
    }

    public void updateSaveButton() {
        if (canSave()) {
            viewSave.setBackgroundResource(R.color.button_green);
            viewSave.setEnabled(true);
        }
        else {
            viewSave.setBackgroundResource(R.color.button_gray);
            viewSave.setEnabled(false);
        }
    }

    public boolean canSave() {
        return ( (!sentence.getText().equals("")) && (!sentence.getMeanings().get(0).getMeaning().equals("")) );
    }

    public void save(View v) {
        saveSentence();
        String SID = getSID();
        saveMeaning(SID);
        saveWordsInSentence(SID);
        if (getIntent().hasExtra("SLID")) {
            addSentenceToList(SID, getIntent().getStringExtra("SLID"));
        }
        newSentence();
    }

    public String getSID() {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT SID FROM sentence WHERE text = '" + sentence.getText() + "';");
        c.moveToLast();
        String SID = c.getString(0);
        db.closeDatabase();
        return SID;
    }

    public void saveSentence() {
        ContentValues values = new ContentValues();
        values.put("text", sentence.getText());
        values.put("learningProgress", 0);
        db.openDatabase();
        db.insert("sentence", values);
        db.closeDatabase();
    }

    public void saveMeaning(String SID) {
        ContentValues values = new ContentValues();
        values.put("meaning", sentence.getMeanings().get(0).getMeaning());
        values.put("language", sentence.getMeanings().get(0).getLanguage());
        values.put("Sentence_SID", SID);
        db.openDatabase();
        db.insert("sentencemeaning", values);
        db.closeDatabase();
    }

    public void saveWordsInSentence(String SID) {
        for (int i = 0; i < addedWords.size(); i += 1) {
            ContentValues values = new ContentValues();
            values.put("Sentence_SID", SID);
            values.put("Word_WID", addedWords.get(i).getWID());
            values.put("writingindex", addedWords.get(i).getWritingIndex());
            db.openDatabase();
            db.insert("sentencecontainsword", values);
            db.closeDatabase();
        }
    }

    public void addSentenceToList(String SID, String SLID) {
        ContentValues values = new ContentValues();
        values.put("StudyList_SLID", SLID);
        values.put("Sentence_SID", SID);
        values.put("nextTestDate", 0);
        db.openDatabase();
        db.insert("listcontainssentence", values);
        db.closeDatabase();
    }

    public void setAdapterAdd() {
        RecyclerAdapterWordWriting adapter = new RecyclerAdapterWordWriting(this, addWords, true);
        viewListAdd.setAdapter(adapter);
    }

    public void setAdapterAdded() {
        RecyclerAdapterWordWriting adapter = new RecyclerAdapterWordWriting(this, addedWords, false);
        viewListRemove.setAdapter(adapter);
    }

    public final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            searchThread = null;
            setAdapterAdd();
        }
    };
}