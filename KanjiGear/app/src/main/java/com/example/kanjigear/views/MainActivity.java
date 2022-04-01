package com.example.kanjigear.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kanjigear.About;
import com.example.kanjigear.R;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.example.kanjigear.db.LoadDatabaseAsync;
import com.example.kanjigear.views.components.KanjiView;
import com.example.kanjigear.views.dictionary.Dictionary;
import com.example.kanjigear.views.lesson.DrawKanji;
import com.example.kanjigear.views.lesson.SelfCorrection;
import com.example.kanjigear.views.studyLists.StudyLists;

public class MainActivity extends AppCompatActivity {

    private static DatabaseOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseOpenHelper(this.getApplicationContext());

        if (!dbHelper.checkDb()) {
            LoadDatabaseAsync task = new LoadDatabaseAsync(MainActivity.this);
            task.execute();
        }

    }

    public void openQuickLesson(View v) {
        Intent intent = new Intent(this, SelfCorrection.class);
        //intent.putExtra("symbol","姆");
        //intent.putExtra("WID", "4196");
        //intent.putExtra("writingIndex", "0");
        intent.putExtra("SID","1");
        startActivity(intent);
    }

    public void openStudyLists(View v) {
        Intent intent = new Intent(this, StudyLists.class);
        startActivity(intent);
    }

    public void openDictionary(View v) {
        Intent intent = new Intent(this, Dictionary.class);
        startActivity(intent);
    }

    public void openSettings(View v) {
        Intent intent = new Intent(this, Dictionary.class);
        startActivity(intent);
    }

    public void openAbout(View v) {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }
}