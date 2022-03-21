package com.example.kanjigear.alerts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.dataModels.StudyList;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseContentLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.example.kanjigear.views.components.KanjiView;
import com.example.kanjigear.views.components.SentenceView;
import com.example.kanjigear.views.components.WordView;

import java.util.ArrayList;

public class AddToList {

    private AlertDialog dialog;
    private ArrayList<StudyList> lists;
    private boolean[] originalCheckedItems;
    private boolean[] checkedItems;
    private DatabaseOpenHelper db;

    private Word word = null;
    private Kanji kanji = null;
    private Sentence sentence = null;

    private WordView contextWord = null;
    private KanjiView contextKanji = null;
    private SentenceView contextSentence = null;

    public AddToList(WordView context, DatabaseOpenHelper db, Word w) {
        this.contextWord = context;
        this.db = db;
        this.word = w;
    }
    public AddToList(KanjiView context, DatabaseOpenHelper db, Kanji k) {
        this.contextKanji = context;
        this.db = db;
        this.kanji = k;
    }
    public AddToList(SentenceView context, DatabaseOpenHelper db, Sentence s) {
        this.contextSentence = context;
        this.db = db;
        this.sentence = s;
    }

    public void loadLists() {
        db.openDatabaseRead();
        lists = new DatabaseContentLoader().getStudyLists(db);
        db.closeDatabase();
    }


    public void showDialog(Context context) {
        loadLists();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select lists");

        checkedItems = getCheckedItems();
        originalCheckedItems = new boolean[lists.size()];
        for (int i = 0; i < lists.size(); i += 1) {
            originalCheckedItems[i] = checkedItems[i];
        }

        builder.setMultiChoiceItems(listsToNameArray(), checkedItems, (dialog, which, isChecked) -> {
            checkedItems[which] = isChecked;
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            updateLists();
        });
        builder.setNegativeButton("Cancel", null);

        dialog = builder.create();
        dialog.show();
    }

    public String[] listsToNameArray() {
        String[] ret = new String[lists.size()];
        for (int i = 0; i < lists.size(); i += 1) {
            ret[i] = lists.get(i).getName();
        }
        return ret;
    }

    public boolean[] getCheckedItems() {
        boolean[] ret = new boolean[lists.size()];
        db.openDatabaseRead();
        for (int i = 0; i < lists.size(); i += 1) {
            Cursor c = null;
            if (word != null) {
                c = db.handleQuery("SELECT * FROM listcontainsword WHERE StudyList_SLID = " + lists.get(i).getSLID() + " AND Word_WID = " + word.getWID() + ";");
            } else if (kanji != null) {
                c = db.handleQuery("SELECT * FROM listcontainskanji WHERE StudyList_SLID = " + lists.get(i).getSLID() + " AND Kanji_symbol = '" + kanji.getSymbol() + "';");
            } else if (sentence != null) {
                c = db.handleQuery("SELECT * FROM listcontainssentence WHERE StudyList_SLID = " + lists.get(i).getSLID() + " AND Sentence_SID = " + sentence.getSID() + ";");
            }
            ret[i] = (c != null ? (c.getCount() > 0) : false);
        }
        db.closeDatabase();
        return ret;
    }


    public void updateLists() {
        db.openDatabase();
        for (int i = 0; i < lists.size(); i += 1) {
            if (originalCheckedItems[i] != checkedItems[i]) {
                ContentValues values = new ContentValues();
                String tableName = "";
                String idName = "";
                String id = "";
                values.put("StudyList_SLID", lists.get(i).getSLID());

                if (word != null) {
                    values.put("Word_WID", word.getWID());
                    tableName = "listcontainsword";
                    idName = "Word_WID";
                    id = word.getWID();
                }
                if (kanji != null) {
                    values.put("Kanji_symbol", kanji.getSymbol());
                    tableName = "listcontainskanji";
                    idName = "Kanji_symbol";
                    id = kanji.getSymbol();
                }
                if (sentence != null) {
                    values.put("Sentence_SID", sentence.getSID());
                    tableName = "listcontainssentence";
                    idName = "Sentence_SID";
                    id = sentence.getSID();
                }

                if (checkedItems[i]) {
                    Log.d("db", "put " + i);
                    db.insert(tableName, values);
                } else {
                    Log.d("db", "remove " + i);
                    db.delete(tableName, "StudyList_SLID", lists.get(i).getSLID(), idName, id);
                }
            }
        }
        db.closeDatabase();
    }
}
