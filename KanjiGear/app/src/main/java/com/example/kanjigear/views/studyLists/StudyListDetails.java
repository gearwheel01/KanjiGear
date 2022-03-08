package com.example.kanjigear.views.studyLists;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.StudyList;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.google.android.material.textfield.TextInputEditText;

public class StudyListDetails extends AppCompatActivity {

    private DatabaseOpenHelper db;
    private StudyList list;
    private EditText listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_list_details);
        listName = findViewById(R.id.studyListName);

        Intent intent = getIntent();
        db = new DatabaseOpenHelper(getApplicationContext());
        getStudyList(intent.getStringExtra("listId"));

        listName.addTextChangedListener(new TextWatcher() {
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
    }

    public void getStudyList(String SLID) {
        db.openDatabase();
        Cursor c = db.handleQuery("SELECT name FROM studylist WHERE SLID = '" + SLID + "';");
        c.moveToFirst();
        list = new StudyList(SLID, c.getString(0));
        db.closeDatabase();
        listName.setText(list.getName());
    }

    public void deleteList(View v) {
        db.openDatabase();
        db.delete("studylist","SLID",list.getSLID());
        db.closeDatabase();
        finish();
    }

    public void updateName() {
        db.openDatabase();
        ContentValues values = new ContentValues();
        values.put("name", String.valueOf(listName.getText()));
        db.update("studylist",values, "SLID", list.getSLID());
        db.closeDatabase();
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateName();
    }
}