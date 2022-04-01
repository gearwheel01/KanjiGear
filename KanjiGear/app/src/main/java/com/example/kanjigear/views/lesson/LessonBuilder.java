package com.example.kanjigear.views.lesson;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.kanjigear.dataModels.StudyList;
import com.example.kanjigear.db.DatabaseModelLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;

import java.util.ArrayList;

public class LessonBuilder {

    private DatabaseOpenHelper db;

    public LessonBuilder(Context context) {
        db = new DatabaseOpenHelper(context);
    }

    public String buildLesson() {
        String lessonString = "";
        return lessonString;
    }

    public Intent getLessonIntentFromString(String lessonString, Context context) {
        Intent intent = new Intent(context, DrawKanji.class);
        return  intent;
    }



    public ArrayList<StudyList> getActiveStudyLists() {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT * FROM studylist WHERE isActive = 1;");
        ArrayList<StudyList> lists = new DatabaseModelLoader().getStudyListsFromCursor(c);
        db.closeDatabase();
        return lists;
    }



}
