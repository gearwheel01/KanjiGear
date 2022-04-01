package com.example.kanjigear.views.lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.dataModels.Stroke;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseContentLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.example.kanjigear.views.components.KanjiGraphicView;

import java.util.ArrayList;

public class DrawKanji extends AppCompatActivity {

    private TextView viewContent;
    private TextView viewHint;
    private Button viewDone;
    private KanjiGraphicView viewKanji;

    private Kanji kanji;
    private Word word;
    private int writingIndex;
    private Sentence sentence;
    private ArrayList<Stroke> strokes;

    private DatabaseOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_kanji);
        loadResources();

        Intent intent = getIntent();
        String intentWID = null;
        int intentWIndex = 0;
        String intentSID = null;
        if (intent.hasExtra("WID")) {
            intentWID = intent.getStringExtra("WID");
            intentWIndex = intent.getIntExtra("writingIndex",0);
        }
        if (intent.hasExtra("SID")) {
            intentSID = intent.getStringExtra("SID");
        }

        openDraw(intent.getStringExtra("symbol"), intentWID, intentWIndex, intentSID);
    }

    public void getContent(String symbol, String WID, int writingIndex, String SID) {
        DatabaseContentLoader loadHelper = new DatabaseContentLoader();

        if (WID != null) {
            word = new DatabaseContentLoader().getWord(db, WID);
            this.writingIndex = writingIndex;
        }
        if (SID != null) {
            sentence = new DatabaseContentLoader().getSentence(db, SID);
            word.setSentenceWritingIndex(sentence.getWord(word.getWID()).getSentenceWritingIndex());
            this.writingIndex = word.getSentenceWritingIndex();
        }

        kanji = loadHelper.getKanji(db, symbol);
        strokes = loadHelper.getStrokes(db, kanji);
    }

    public void loadResources() {
        viewContent = findViewById(R.id.drawKanjiContent);
        viewHint = findViewById(R.id.drawKanjiHint);
        viewDone = findViewById(R.id.drawKanjiDone);
        viewKanji = findViewById(R.id.drawKanjiKanji);
        db = new DatabaseOpenHelper(getApplicationContext());
    }

    public void setViewTexts() {
        if (hasNextDraw()) {
            viewDone.setText("next");
        } else {
            if (hasNextTask()) {
                viewDone.setText("continue");
            } else {
                viewDone.setText("again");
            }
        }
        viewHint.setText("");
        if (word != null) {
            viewHint.setText(word.getWordReadings().get(0));
        }
        setViewContentHidden();
    }


    public void setViewContentHidden() {
        if (sentence != null) {
            viewContent.setText(sentence.getKanjiHiddenWriting(kanji.getSymbol().charAt(0)));
        }
        else if (word != null) {
            viewContent.setText(word.getKanjiHiddenWriting(writingIndex, kanji.getSymbol().charAt(0)));
        }
        else {
            viewContent.setText(kanji.getMeaningsString(""));
        }
    }
    public void setViewContentNotHidden() {
        if (sentence != null) {
            char untilKanji = ' ';
            if (word.isLastKanji(writingIndex, kanji.getSymbol().charAt(0))) {
                if (!sentence.isLastWord(word.getWID())) {
                    Word nextWord = sentence.getNextWord(word.getWID());
                    untilKanji = nextWord.getKanjiInWord(nextWord.getSentenceWritingIndex()).get(0);
                }
            }
            else {
                untilKanji = word.getNextKanji(writingIndex, kanji.getSymbol().charAt(0));
            }
            viewContent.setText(sentence.getKanjiHiddenWriting(untilKanji));
        }
        else if (word != null) {
            viewContent.setText(word.getKanjiHiddenWriting(writingIndex, word.getNextKanji(writingIndex,kanji.getSymbol().charAt(0))));
        }
        else {
            viewContent.setText(kanji.getMeaningsString(""));
        }
    }

    public boolean hasNextDraw() {
        if (sentence != null) {
            return ( (!sentence.isLastWord(word.getWID()) || (!word.isLastKanji(writingIndex, kanji.getSymbol().charAt(0))) ));
        }
        if (word != null) {
            return (!word.isLastKanji(writingIndex, kanji.getSymbol().charAt(0)));
        }
        return false;
    }

    public void finishedDrawing() {
        viewDone.setEnabled(true);
        setViewContentNotHidden();
    }

    public void pressedDone(View v) {
        if (sentence != null) {
            if (hasNextDraw()) {
                if (word.isLastKanji(writingIndex, kanji.getSymbol().charAt(0))) {
                    Word nextWord = sentence.getNextWord(word.getWID());
                    int wi = nextWord.getSentenceWritingIndex();
                    openDraw(nextWord.getKanjiInWord(wi).get(0)+"", nextWord.getWID(), wi, sentence.getSID());
                }
                else {
                    openDraw(word.getNextKanji(writingIndex, kanji.getSymbol().charAt(0))+"", word.getWID(), writingIndex, sentence.getSID());
                }
            }
            else {
                finishTask();
            }
        }
        else if (word != null) {
            if (hasNextDraw()) {
                openDraw(word.getNextKanji(writingIndex, kanji.getSymbol().charAt(0))+"", word.getWID(), writingIndex, null);
            }
            else {
                finishTask();
            }
        }
        else {
            finishTask();
        }
    }

    public void finishTask() {
        if (hasNextTask()) {

        } else {
            if (getDrawCount() > 1) {
                if (sentence != null) {
                    Word firstWord = sentence.getWords().get(0);
                    int wi = firstWord.getSentenceWritingIndex();
                    openDraw(firstWord.getKanjiInWord(wi).get(0)+"", firstWord.getWID(), wi, sentence.getSID());
                }
                else {
                    openDraw(word.getKanjiInWord(writingIndex).get(0)+"", word.getWID(), writingIndex, null);
                }
            }
            else {
                viewKanji.resetDrawing();
                viewDone.setEnabled(false);
                setViewContentHidden();
            }
        }
    }

    public void openDraw(String symbol, String WID, int writingIndex, String SID) {
        getContent(symbol, WID, writingIndex, SID);
        viewKanji.setStrokes(strokes);
        viewKanji.setDrawMode(this);
        viewDone.setEnabled(false);
        setViewTexts();
    }

    public boolean hasNextTask() {
        return false;
    }

    public int getDrawCount() {
        if (sentence != null) {
            ArrayList<Word> words = sentence.getWords();
            int ret = 0;
            for (int i = 0; i < words.size(); i += 1) {
                int wi = words.get(i).getSentenceWritingIndex();
                ret += words.get(i).getKanjiInWord(wi).size();
            }
            return ret;
        }
        if (word != null) {
            return word.getKanjiInWord(writingIndex).size();
        }
        return 1;
    }
}