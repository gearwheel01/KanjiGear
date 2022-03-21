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
import android.view.View;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.StudyList;
import com.example.kanjigear.db.DatabaseContentLoader;
import com.example.kanjigear.db.DatabaseModelLoader;
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
        getStudyLists();
        RecyclerAdapterStudyList adapter = new RecyclerAdapterStudyList(this, studyLists);
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
    public void getStudyLists() {
        studyLists = new DatabaseContentLoader().getStudyLists(db);
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
        values.put("isActive", 0);
        db.insert("studylist",values);
        Cursor c = db.handleQuery("SELECT SLID FROM studylist WHERE name = '" + newName + "';");
        c.moveToFirst();
        db.closeDatabase();
        return c.getString(0);
    }
}