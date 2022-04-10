package com.example.kanjigear.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kanjigear.R;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.example.kanjigear.db.LoadDatabaseAsync;
import com.example.kanjigear.views.dictionary.Dictionary;
import com.example.kanjigear.views.lesson.LessonBuilder;
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
        LessonBuilder builder = new LessonBuilder(this);
        String lesson = builder.buildLesson();
        Intent intent = builder.getLessonIntentFromString(lesson, lesson);
        if (intent != null) {
            startActivity(intent);
        }
    }

    public void openStudyLists(View v) {
        Intent intent = new Intent(this, StudyLists.class);
        startActivity(intent);
    }

    public void openDictionary(View v) {
        Intent intent = new Intent(this, Dictionary.class);
        startActivity(intent);
    }

    public void openStatistics(View v) {
        Intent intent = new Intent(this, Statistics.class);
        startActivity(intent);
    }

    public void openAbout(View v) {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }
}