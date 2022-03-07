package com.example.kanjigear.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import com.example.kanjigear.dataModels.*;

import java.util.ArrayList;

public class DatabaseAccess {

    private DatabaseOpenHelper db;

    public DatabaseAccess(Context context) {
        db = new DatabaseOpenHelper(context);
    }

    @SuppressLint("Range")
    public ArrayList<StudyList> getStudyLists() {
        db.openDatabase();
        Cursor c = db.handleQuery("SELECT * FROM studylist;");
        ArrayList<StudyList> ret = new ArrayList<StudyList>();

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            String SLID = c.getString(c.getColumnIndex("SLID"));
            String name = c.getString(c.getColumnIndex("name"));
            ret.add(new StudyList(SLID, name));
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
}
