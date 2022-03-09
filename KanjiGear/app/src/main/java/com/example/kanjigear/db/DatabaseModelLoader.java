package com.example.kanjigear.db;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.StudyList;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.dataModels.WordTranslation;

import java.util.ArrayList;

public class DatabaseModelLoader {

    @SuppressLint("Range")
    public ArrayList<Word> getWordsFromCursor(Cursor c) {
        ArrayList<Word> words = new ArrayList<>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            String WID = c.getString(c.getColumnIndex("WID"));
            String word = c.getString(c.getColumnIndex("word"));
            String grade = c.getString(c.getColumnIndex("grade"));
            int learningProgress = c.getInt(c.getColumnIndex("learningProgress"));
            String pronunciation = c.getString(c.getColumnIndex("pronunciation"));
            String romaji = c.getString(c.getColumnIndex("romaji"));
            words.add(new Word(WID, word, grade, learningProgress, pronunciation, romaji));
            c.moveToNext();
        }
        c.close();
        return words;
    }

    @SuppressLint("Range")
    public ArrayList<WordTranslation> getWordTranslationsFromCursor(Cursor c) {
        ArrayList<WordTranslation> translations = new ArrayList<>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            String WMID = c.getString(c.getColumnIndex("WMID"));
            String translation = c.getString(c.getColumnIndex("meaning"));
            String language = c.getString(c.getColumnIndex("language"));
            translations.add(new WordTranslation(WMID, translation, language));
            c.moveToNext();
        }
        c.close();
        return translations;
    }

    @SuppressLint("Range")
    public ArrayList<StudyList> getStudyListsFromCursor(Cursor c) {
        ArrayList<StudyList> lists = new ArrayList<>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            String name = c.getString(c.getColumnIndex("name"));
            String SLID = c.getString(c.getColumnIndex("SLID"));
            lists.add(new StudyList(SLID,name));
            c.moveToNext();
        }
        c.close();
        return lists;
    }

    @SuppressLint("Range")
    public ArrayList<Kanji> getKanjiFromCursor(Cursor c) {
        ArrayList<Kanji> kanji = new ArrayList<>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            String symbol = c.getString(c.getColumnIndex("symbol"));
            String grade = c.getString(c.getColumnIndex("grade"));
            int learningProgress = c.getInt(c.getColumnIndex("learningProgress"));
            kanji.add(new Kanji(symbol,grade, learningProgress));
            c.moveToNext();
        }
        c.close();
        return kanji;
    }
}
