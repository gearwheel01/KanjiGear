package com.example.kanjigear.db;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.util.Log;

import com.example.kanjigear.dataModels.*;

import java.util.ArrayList;

public class DatabaseModelLoader {

    @SuppressLint("Range")
    public ArrayList<Word> getWordsFromCursor(Cursor c) {
        ArrayList<Word> words = new ArrayList<>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            String WID = c.getString(c.getColumnIndex("WID"));
            int learningProgress = c.getInt(c.getColumnIndex("learningProgress"));
            int frequency = c.getInt(c.getColumnIndex("frequency"));
            words.add(new Word(WID, learningProgress, frequency));
            c.moveToNext();
        }
        c.close();
        return words;
    }

    @SuppressLint("Range")
    public ArrayList<String> getWordReadingsFromCursor(Cursor c) {
        ArrayList<String> readings = new ArrayList<>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            readings.add(c.getString(c.getColumnIndex("reading")));
            c.moveToNext();
        }
        c.close();
        return readings;
    }

    @SuppressLint("Range")
    public ArrayList<String> getWordWritingsFromCursor(Cursor c) {
        ArrayList<String> writings = new ArrayList<>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            writings.add(c.getString(c.getColumnIndex("writing")));
            c.moveToNext();
        }
        c.close();
        return writings;
    }

    @SuppressLint("Range")
    public ArrayList<WordMeaning> getWordMeaningsFromCursor(Cursor c) {
        ArrayList<WordMeaning> translations = new ArrayList<>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            String WMID = c.getString(c.getColumnIndex("WMID"));
            String translation = c.getString(c.getColumnIndex("meaning"));
            String language = c.getString(c.getColumnIndex("language"));
            translations.add(new WordMeaning(WMID, translation, language));
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
            boolean active = (c.getInt(c.getColumnIndex("isActive")) == 1);
            lists.add(new StudyList(SLID,name, active));
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
            int grade = c.getInt(c.getColumnIndex("grade"));
            int jlpt = c.getInt(c.getColumnIndex("jlpt"));
            int frequency = c.getInt(c.getColumnIndex("frequency"));
            int learningProgress = c.getInt(c.getColumnIndex("learningProgress"));
            kanji.add(new Kanji(symbol,grade, jlpt, frequency, learningProgress));
            c.moveToNext();
        }
        c.close();
        return kanji;
    }

    @SuppressLint("Range")
    public ArrayList<Stroke> getStrokesFromCursor(Cursor c) {
        ArrayList<Stroke> strokes = new ArrayList<>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            String SID = c.getString(c.getColumnIndex("SID"));
            int number = c.getInt(c.getColumnIndex("number"));
            String symbol = c.getString(c.getColumnIndex("Kanji_symbol"));
            String component = c.getString(c.getColumnIndex("component"));
            float mx = c.getFloat(c.getColumnIndex("mx"));
            float my = c.getFloat(c.getColumnIndex("my"));
            strokes.add(new Stroke(SID, number, symbol, component, mx, my));
            c.moveToNext();
        }
        c.close();
        return strokes;
    }

    @SuppressLint("Range")
    public ArrayList<Bezier> getBeziersFromCursor(Cursor c) {
        ArrayList<Bezier> beziers = new ArrayList<>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            int relative = c.getInt(c.getColumnIndex("relative"));
            float x1 = c.getFloat(c.getColumnIndex("x1"));
            float y1 = c.getFloat(c.getColumnIndex("y1"));
            float x2 = c.getFloat(c.getColumnIndex("x2"));
            float y2 = c.getFloat(c.getColumnIndex("y2"));
            float x = c.getFloat(c.getColumnIndex("x"));
            float y = c.getFloat(c.getColumnIndex("y"));
            boolean rel = relative == 1;
            beziers.add(new Bezier(rel, x1, y1, x2, y2, x, y));
            c.moveToNext();
        }
        c.close();
        return beziers;
    }

    @SuppressLint("Range")
    public ArrayList<KanjiMeaning> getKanjiMeaningsFromCursor(Cursor c) {
        ArrayList<KanjiMeaning> meanings = new ArrayList<>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            int KMID = c.getInt(c.getColumnIndex("KMID"));
            String meaning = c.getString(c.getColumnIndex("meaning"));
            String language = c.getString(c.getColumnIndex("language"));
            meanings.add(new KanjiMeaning(KMID,meaning, language));
            c.moveToNext();
        }
        c.close();
        return meanings;
    }

    @SuppressLint("Range")
    public ArrayList<SentenceMeaning> getSentenceMeaningsFromCursor(Cursor c) {
        ArrayList<SentenceMeaning> meanings = new ArrayList<>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            String meaning = c.getString(c.getColumnIndex("meaning"));
            String language = c.getString(c.getColumnIndex("language"));
            meanings.add(new SentenceMeaning(language, meaning));
            c.moveToNext();
        }
        c.close();
        return meanings;
    }

    @SuppressLint("Range")
    public ArrayList<Reading> getReadingsFromCursor(Cursor c) {
        ArrayList<Reading> readings = new ArrayList<>();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i += 1) {
            int RID = c.getInt(c.getColumnIndex("KRID"));
            String type = c.getString(c.getColumnIndex("type"));
            String reading = c.getString(c.getColumnIndex("reading"));
            readings.add(new Reading(RID,type, reading));
            c.moveToNext();
        }
        c.close();
        return readings;
    }

    @SuppressLint("Range")
    public ArrayList<Sentence> getSentencesFromCursor(Cursor c) {
        ArrayList<Sentence> sentences = new ArrayList<>();
        while (c.moveToNext()) {
            String SID = c.getString(c.getColumnIndex("SID"));
            String text = c.getString(c.getColumnIndex("text"));
            int learningProgress = c.getInt(c.getColumnIndex("learningProgress"));
            sentences.add(new Sentence(SID, text, learningProgress));
        }
        c.close();
        return sentences;
    }

}
