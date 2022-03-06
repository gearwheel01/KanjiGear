package com.example.kanjigear;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.util.Log;

import com.example.kanjigear.dataModels.*;

public class DatabaseAccess {

    private DatabaseOpenHelper db;

    public DatabaseAccess(DatabaseOpenHelper databaseb) {
        db = databaseb;
    }

    @SuppressLint("Range")
    public StudyList[] getStudyLists() {
        Cursor c = db.handleQuery("SELECT * FROM studylist;");
        StudyList[] ret = new StudyList[c.getCount()];

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            String SLID = c.getString(c.getColumnIndex("SLID"));
            String name = c.getString(c.getColumnIndex("name"));
            ret[i] = new StudyList(SLID, name);
            c.moveToNext();
        }
        c.close();
        return ret;
    }

    @SuppressLint("Range")
    public Word[] getWordsInList(String SLID) {
        Cursor c = db.handleQuery("SELECT w.* FROM word w,listcontainsword l WHERE w.WID=l.Word_WID AND l.StudyList_SLID=" + SLID + ";");
        Word[] ret = new Word[c.getCount()];

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            String WID = c.getString(c.getColumnIndex("WID"));
            String word = c.getString(c.getColumnIndex("word"));
            String grade = c.getString(c.getColumnIndex("grade"));
            int learningProgress = c.getInt(c.getColumnIndex("learningprogress"));
            String pronunciation = c.getString(c.getColumnIndex("pronunciation"));
            ret[i] = new Word(WID, word, grade, learningProgress, pronunciation);
            c.moveToNext();
        }
        c.close();
        return ret;
    }
}
