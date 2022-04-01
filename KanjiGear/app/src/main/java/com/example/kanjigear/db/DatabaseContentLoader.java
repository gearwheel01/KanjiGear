package com.example.kanjigear.db;

import android.database.Cursor;

import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.dataModels.Stroke;
import com.example.kanjigear.dataModels.StudyList;
import com.example.kanjigear.dataModels.Word;

import java.lang.reflect.Array;
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

    public ArrayList<Sentence> addDetailsToSentences(DatabaseOpenHelper db, ArrayList<Sentence> sentences) {
        Cursor c;
        for (int i = 0; i < sentences.size(); i += 1) {
            c = db.handleQuery("SELECT * FROM sentencemeaning WHERE Sentence_SID = " + sentences.get(i).getSID());
            sentences.get(i).setMeanings(new DatabaseModelLoader().getSentenceMeaningsFromCursor(c));
        }
        return sentences;
    }

    public Kanji getKanji(DatabaseOpenHelper db, String symbol) {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT * FROM kanji WHERE symbol = '" + symbol + "';");
        Kanji kanji = addDetailsToKanji(db, new DatabaseModelLoader().getKanjiFromCursor(c)).get(0);
        db.closeDatabase();
        return kanji;
    }

    public Word getWord(DatabaseOpenHelper db, String WID) {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT * FROM word WHERE WID = '" + WID + "';");
        Word word = new DatabaseModelLoader().getWordsFromCursor(c).get(0);
        word.setWordWritings(new DatabaseModelLoader().getWordWritingsFromCursor(db.handleQuery("SELECT * FROM wordwriting WHERE Word_WID = " + WID + ";")));
        word.setWordReadings(new DatabaseModelLoader().getWordReadingsFromCursor(db.handleQuery("SELECT * FROM wordreading WHERE Word_WID = " + WID + ";")));

        c = db.handleQuery("SELECT * FROM wordmeaning WHERE Word_WID = '" + word.getWID() + "';");
        word.setTranslations(new DatabaseModelLoader().getWordMeaningsFromCursor(c));

        db.closeDatabase();
        return word;
    }

    public Sentence getSentence(DatabaseOpenHelper db, String SID) {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT * FROM sentence WHERE SID = " + SID + ";");
        Sentence sentence = addDetailsToSentences(db, new DatabaseModelLoader().getSentencesFromCursor(c)).get(0);
        c = db.handleQuery("SELECT w.*, sw.writingindex FROM word w, sentencecontainsword sw WHERE w.WID = sw.Word_WID AND sw.Sentence_SID = " + sentence.getSID() + ";");
        sentence.setWords(addDetailsToWords(db, new DatabaseModelLoader().getWordsInSentenceFromCursor(c)));
        db.closeDatabase();
        return sentence;
    }

    public ArrayList<Kanji> addDetailsToKanji(DatabaseOpenHelper db, ArrayList<Kanji> kanji) {
        Cursor c;
        for (int i = 0; i < kanji.size(); i += 1) {
            c = db.handleQuery("SELECT * FROM kanjimeaning WHERE Kanji_symbol = '" + kanji.get(i).getSymbol() + "';");
            kanji.get(i).setMeanings(new DatabaseModelLoader().getKanjiMeaningsFromCursor(c));
            c = db.handleQuery("SELECT * FROM kanjireading WHERE Kanji_symbol = '" + kanji.get(i).getSymbol() + "';");
            kanji.get(i).setReadings(new DatabaseModelLoader().getReadingsFromCursor(c));
        }
        return kanji;
    }

    public ArrayList<StudyList> getStudyLists(DatabaseOpenHelper db) {
        db.openDatabase();
        Cursor c = db.handleQuery("SELECT * FROM studylist;");
        ArrayList<StudyList> studyLists = new DatabaseModelLoader().getStudyListsFromCursor(c);
        db.closeDatabase();
        return studyLists;
    }

    public ArrayList<Kanji> getKanjiInList(DatabaseOpenHelper db, String SLID) {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT k.* FROM kanji k,listcontainskanji l WHERE k.symbol=l.Kanji_symbol AND l.StudyList_SLID=" + SLID + ";");
        ArrayList<Kanji> kanji = new DatabaseModelLoader().getKanjiFromCursor(c);
        db.closeDatabase();
        return kanji;
    }

    public ArrayList<Word> getWordsInList(DatabaseOpenHelper db, String SLID) {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT w.* FROM word w,listcontainsword l WHERE w.WID=l.Word_WID AND l.StudyList_SLID=" + SLID + ";");
        ArrayList<Word> words = new DatabaseContentLoader().addDetailsToWords(db, new DatabaseModelLoader().getWordsFromCursor(c));
        db.closeDatabase();
        return words;
    }

    public ArrayList<Sentence> getSentencesInList(DatabaseOpenHelper db, String SLID) {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT s.* FROM sentence s,listcontainssentence l WHERE s.SID=l.Sentence_SID AND l.StudyList_SLID=" + SLID + ";");
        ArrayList<Sentence> sentences = new DatabaseModelLoader().getSentencesFromCursor(c);
        db.closeDatabase();
        return  sentences;
    }

    public ArrayList<Stroke> getStrokes(DatabaseOpenHelper db, Kanji kanji) {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT * FROM stroke WHERE Kanji_symbol = '" + kanji.getSymbol() + "';");
        ArrayList<Stroke> strokes = new DatabaseModelLoader().getStrokesFromCursor(c);
        for (int i = 0; i < strokes.size(); i += 1) {
            c = db.handleQuery("SELECT * FROM bezier WHERE SID = " + strokes.get(i).getSID());
            strokes.get(i).setBeziers(new DatabaseModelLoader().getBeziersFromCursor(c));
        }
        db.closeDatabase();
        return strokes;
    }

}
