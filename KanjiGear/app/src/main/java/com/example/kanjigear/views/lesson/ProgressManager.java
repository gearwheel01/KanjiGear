package com.example.kanjigear.views.lesson;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.LearnElement;
import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseContentLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;

import java.util.ArrayList;

public class ProgressManager {

    private DatabaseOpenHelper db;

    private final int PROGRESS_INCREMENT_GOOD = 5;
    private final int PROGRESS_INCREMENT_BAD = -15;

    private final int REV_MIN_UNTIL = 35;
    private final int REV_MED_UNTIL = 70;

    private final long REVISION_MIN = 60 * 60;
    private final long REVISION_MEDIUM = 60 * 60 * 24 * 3;
    private final long REVISION_MAX = 60 * 60 * 24 * 60;

    public ProgressManager(Context context) {
        db = new DatabaseOpenHelper(context);
    }

    public void updateElementProgress(LearnElement element, boolean goodAnswer) {
        int updatedProgress = element.getLearningProgress() + (goodAnswer ? PROGRESS_INCREMENT_GOOD : PROGRESS_INCREMENT_BAD);
        if (updatedProgress < 0) {
            updatedProgress = 0;
        }
        if (updatedProgress > 100) {
            updatedProgress = 100;
        }

        String tableName = "kanji";
        String idName = "symbol";
        String id = "";
        ContentValues values = new ContentValues();
        values.put("learningProgress", updatedProgress);

        if (element.getClass().getSimpleName().equals("Kanji")) {
            Kanji k = (Kanji) element;
            id = k.getSymbol();
        }
        if (element.getClass().getSimpleName().equals("Word")) {
            Word w = (Word) element;
            tableName = "word";
            idName = "WID";
            id = w.getWID();

            ArrayList<Character> kanji = w.getKanjiInWord(w.getWritingIndex());
            for (int i = 0; i < kanji.size(); i += 1) {
                Kanji k = new DatabaseContentLoader().getKanji(db, kanji.get(i) + "");
                updateElementProgress(k, goodAnswer);
            }
        }
        if (element.getClass().getSimpleName().equals("Sentence")) {
            Sentence s = (Sentence) element;
            tableName = "sentence";
            idName = "SID";
            id = s.getSID();

            ArrayList<Word> words = s.getWords();
            for (int i = 0; i < words.size(); i += 1) {
                updateElementProgress(words.get(i), goodAnswer);
            }
        }

        db.openDatabase();
        Log.d("db", "update " + element.toString() + " to progress " + updatedProgress);
        db.update(tableName, values, idName, id);
        db.closeDatabase();
    }

    public void scheduleNextRevision(LearnElement element) {
        long nextTestDate = (System.currentTimeMillis() / 1000);
        double percentage = ((double)(element.getLearningProgress()) / REV_MIN_UNTIL);
        if (element.getLearningProgress() <= REV_MIN_UNTIL) {
            nextTestDate += REVISION_MIN * percentage;
        }
        else if (element.getLearningProgress() <= REV_MED_UNTIL) {
            percentage = ( (double)(element.getLearningProgress() - REV_MIN_UNTIL) / (REV_MED_UNTIL - REV_MIN_UNTIL) );
            nextTestDate += REVISION_MEDIUM * percentage;
        }
        else {
            percentage = ( (double)(element.getLearningProgress() - REV_MED_UNTIL) / (100 - REV_MED_UNTIL) );
            nextTestDate += REVISION_MAX * percentage;
        }

        long difference = nextTestDate - (System.currentTimeMillis() / 1000);
        Log.d("progress", "schedule " + element.toString() + "(progress "+ element.getLearningProgress() + ") to " + nextTestDate + "(" + difference + "s / " + difference / 60 + "m / " + difference / 60 / 60 + "h / " + difference / 60 / 60 / 24 + "d)");
        updateSchedule(element, nextTestDate);
    }

    public void updateSchedule(LearnElement element, long nextTestDate) {
        ContentValues values = new ContentValues();
        values.put("nextTestDate", nextTestDate);
        String tableName = "listcontainskanji";
        String idName = "Kanji_symbol";
        String id = "";

        if (element.getClass().getSimpleName().equals("Kanji")) {
            Kanji k = (Kanji) element;
            id = k.getSymbol();
        }
        if (element.getClass().getSimpleName().equals("Word")) {
            Word w = (Word) element;
            tableName = "listcontainsword";
            idName = "Word_WID";
            id = w.getWID();
        }
        if (element.getClass().getSimpleName().equals("Sentence")) {
            Sentence s = (Sentence) element;
            tableName = "listcontainssentence";
            idName = "Sentence_SID";
            id = s.getSID();
        }

        db.openDatabase();
        db.update(tableName, values, idName, id, "StudyList_SLID", element.getInList());
        db.closeDatabase();
    }

}
