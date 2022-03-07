package com.example.kanjigear.views.studyLists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.StudyList;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseOpenHelper;

import java.util.ArrayList;

public class StudyLists extends AppCompatActivity {

    private DatabaseOpenHelper db;
    private RecyclerView recyclerView;
    private ArrayList<StudyList> studyLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_lists);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        db = new DatabaseOpenHelper(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        setAdapter();
    }

    public void setAdapter() {
        studyLists = getStudyLists();
        recyclerAdapterStudyList adapter = new recyclerAdapterStudyList(this, studyLists);
        recyclerView.setAdapter(adapter);
    }

    public void newList(View v) {
        String newId = createNewList();
        openListDetails(newId);
    }

    public void openListDetails(String id) {
        Intent intent = new Intent(this, StudyListDetails.class);
        intent.putExtra("listId", id);
        startActivity(intent);
    }



    // Database access
    @SuppressLint("Range")
    public ArrayList<StudyList> getStudyLists() {
        db.openDatabase();
        Cursor c = db.handleQuery("SELECT * FROM studylist;");
        ArrayList<StudyList> ret = new ArrayList<StudyList>();

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            String name = c.getString(c.getColumnIndex("name"));
            String SLID = c.getString(c.getColumnIndex("SLID"));
            ret.add(new StudyList(SLID,name));
            c.moveToNext();
        }
        c.close();
        db.closeDatabase();
        return ret;
    }

    @SuppressLint("Range")
    public ArrayList<Word> getWordsInList(String SLID) {
        db.openDatabase();
        Cursor c = db.handleQuery("SELECT w.* FROM word w,listcontainsword l WHERE w.WID=l.Word_WID AND l.StudyList_SLID=" + SLID + ";");
        ArrayList<Word> ret = new ArrayList<Word>();

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            String WID = c.getString(c.getColumnIndex("WID"));
            String word = c.getString(c.getColumnIndex("word"));
            String grade = c.getString(c.getColumnIndex("grade"));
            int learningProgress = c.getInt(c.getColumnIndex("learningprogress"));
            String pronunciation = c.getString(c.getColumnIndex("pronunciation"));
            ret.add(new Word(WID, word, grade, learningProgress, pronunciation));
            c.moveToNext();
        }
        c.close();
        db.closeDatabase();
        return ret;
    }

    // returns id of new list
    public String createNewList() {
        db.openDatabase();
        String newName = "list";
        int i = 0;
        boolean exists;
        do {
            exists = db.handleQuery("SELECT * FROM studylist WHERE name = '" + newName + "';").getCount() > 0;
            if (exists) {
                i += 1;
                newName = "list" + i;
            }
        } while (exists);
        ContentValues values = new ContentValues();
        values.put("name",newName);
        db.insert("studylist",values);
        Cursor c = db.handleQuery("SELECT SLID FROM studylist WHERE name = '" + newName + "';");
        c.moveToFirst();
        db.closeDatabase();
        return c.getString(0);
    }
}