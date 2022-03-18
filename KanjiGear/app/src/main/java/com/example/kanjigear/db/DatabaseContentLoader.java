package com.example.kanjigear.db;

import android.database.Cursor;

import com.example.kanjigear.dataModels.Word;

import java.util.ArrayList;

public class DatabaseContentLoader {

    public ArrayList<Word> addDetailsToWords(DatabaseOpenHelper db, ArrayList<Word> words) {
        Cursor c;
        for (int i = 0; i < words.size(); i += 1) {
            c = db.handleQuery("SELECT * FROM wordmeaning WHERE Word_WID = '" + words.get(i).getWID() + "';");
            words.get(i).setTranslations(new DatabaseModelLoader().getWordMeaningsFromCursor(c));

            c = db.handleQuery("SELECT * FROM wordwriting WHERE Word_WID = " + words.get(i).getWID() + ";");
            words.get(i).setWordWritings(new DatabaseModelLoader().getWordWritingsFromCursor(c));

            c = db.handleQuery("SELECT * FROM wordreading WHERE Word_WID = " + words.get(i).getWID() + ";");
            words.get(i).setWordReadings(new DatabaseModelLoader().getWordReadingsFromCursor(c));
        }
        return words;
    }

}
