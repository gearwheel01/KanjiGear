package com.example.kanjigear.views.studyLists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.StudyList;
import com.example.kanjigear.db.DatabaseAccess;

import java.util.ArrayList;

public class StudyLists extends AppCompatActivity {

    private DatabaseAccess dbAccess;
    private RecyclerView recyclerView;
    private ArrayList<StudyList> studyLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_lists);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        dbAccess = new DatabaseAccess(getApplicationContext());
        setAdapter();
    }

    public void setAdapter() {
        studyLists = dbAccess.getStudyLists();
        recyclerAdapterStudyList adapter = new recyclerAdapterStudyList(studyLists);
        recyclerView.setAdapter(adapter);
    }

    public void newList(View v) {
        openListDetails("45");
    }

    public void clickList(View v) {
        Button b = (Button) v;
        openListDetails(b.getText() + "");
    }

    public void openListDetails(String name) {
        Intent intent = new Intent(this, StudyListDetails.class);
        intent.putExtra("listName", name);
        startActivity(intent);
    }
}