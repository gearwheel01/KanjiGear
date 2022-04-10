package com.example.kanjigear.views.dictionary;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseContentLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;

import java.util.ArrayList;

public class AddSentenceWordSearch extends Thread{

    private AddNewSentence context;
    private DatabaseOpenHelper db;
    private String searchString;
    private String text;

    public AddSentenceWordSearch(AddNewSentence context, String searchString, String text) {
        this.context = context;
        db = new DatabaseOpenHelper(context.getApplicationContext());
        this.searchString = searchString;
        this.text = text;
    }

    @Override
    public void run() {
        ArrayList<Word> words;

        if (searchString.equals("")) {
            words = searchWordsInText();
        }
        else {
            words = searchWords();
        }

        if (!isInterrupted()) {
            context.setAddWords(words);
            context.handler.sendMessage(context.handler.obtainMessage());
        }
    }

    public ArrayList<Word> searchWords() {
        ArrayList<Word> words = new ArrayList<>();
        db.openDatabaseRead();
        ArrayList<Cursor> cl = new ArrayList<>();
        cl.add(db.handleQuery("SELECT w.WID FROM wordwriting ww, word w WHERE w.WID = ww.Word_WID AND ww.writing LIKE '" + searchString + "%' " +
                "ORDER BY length(ww.writing), w.frequency DESC LIMIT 10;"));
        cl.add(db.handleQuery("SELECT w.WID FROM wordreading wr, word w WHERE w.WID = wr.Word_WID AND wr.reading LIKE '" + searchString + "%' " +
                "ORDER BY length(wr.reading), w.frequency DESC LIMIT 10;"));
        cl.add(db.handleQuery("SELECT w.WID FROM wordmeaning m, word w WHERE w.WID = m.Word_WID AND m.meaning LIKE '" + searchString + "%' " +
                "ORDER BY length(m.meaning), w.frequency DESC LIMIT 10;"));

        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < cl.size(); i += 1) {
            while (cl.get(i).moveToNext()) {
                ids.add(cl.get(i).getString(0));
            }
        }
        db.closeDatabase();

        DatabaseContentLoader loader = new DatabaseContentLoader();
        for (int i = 0; i < ids.size(); i += 1) {
            words.add(loader.getWord(db, ids.get(i)));
        }

        return selectWritingIndex(words);
    }

    public ArrayList<Word> searchWordsInText() {
        Log.d("search", "start");
        ArrayList<Word> words = new ArrayList<>();
        Sentence sentence = new Sentence("",text,0);
        ArrayList<Integer> kanji = sentence.getKanjiIndexesInSentence();

        if (kanji.size() > 0) {
            int i = kanji.get(0);
            while (i < text.length()) {
                Log.d("i", i + "");
                int a = i + 1;
                int count;
                db.openDatabaseRead();
                do {
                    String sub = text.substring(i, a);
                    count = db.handleQuery("SELECT * FROM wordwriting WHERE writing LIKE '" + sub + "%' LIMIT 1;").getCount();
                    Log.d("a", sub + " count: " + count);
                    a += 1;
                    if ( (count > 0) && (a > text.length()) ) {
                        a += 1;
                    }

                } while ( (count > 0) && (a <= text.length()) );
                db.closeDatabase();
                a -= 2;
                Log.d("a", "after move back: " + a);
                Log.d("i", "after move back: " + i);
                String sub = text.substring(i, a);
                Log.d("db", "search: " + sub);
                db.openDatabase();
                Cursor c = db.handleQuery("SELECT w.WID FROM wordwriting ww, word w WHERE w.WID = ww.Word_WID AND ww.writing LIKE '" + sub + "%' " +
                        "ORDER BY length(ww.writing), w.frequency DESC LIMIT 3;");
                ArrayList<String> ids = new ArrayList();
                while (c.moveToNext()) {
                    ids.add(c.getString(0));
                    Log.d("add id: ", ids.get(ids.size() - 1) + "");
                }
                db.closeDatabase();

                DatabaseContentLoader loader = new DatabaseContentLoader();
                for (int id = 0; id < ids.size(); id += 1) {
                    words.add(loader.getWord(db, ids.get(id)));
                    Log.d("add", words.get(words.size() - 1).getWordWritings().get(0));
                }

                int lastI = i;
                i = getNextKanjiIndex(kanji, a);
                if (i == lastI) {
                    i += 1;
                }
            }
        }

        Log.d("search", "finished");
        return selectWritingIndex(words);
    }

    public int getNextKanjiIndex(ArrayList<Integer> kanji, int currentIndex) {
        for (int i = 0; i < kanji.size(); i += 1) {
            if (currentIndex <= kanji.get(i)) {
                return kanji.get(i);
            }
        }
        return text.length();
    }

    public ArrayList<Word> selectWritingIndex(ArrayList<Word> words) {
        ArrayList<Word> filteredWords = new ArrayList<>();
        for (int i = 0; i < words.size(); i += 1) {
            Word w = words.get(i);
            ArrayList<String> writings = w.getWordWritings();
            boolean found = false;
            int writingIndex = 0;
            while ( (!found) && (writingIndex < writings.size()) ) {
                ArrayList<Character> kanji = w.getKanjiInWord(writingIndex);
                boolean add = true;
                for (int ki = 0; ki < kanji.size(); ki += 1) {
                    if (!text.contains(kanji.get(ki) + "")) {
                        add = false;
                    }
                }

                if (add) {
                    w.setWritingIndex(writingIndex);
                    filteredWords.add(w);
                    found = true;
                }

                writingIndex += 1;
            }
        }
        return filteredWords;
    }

}
