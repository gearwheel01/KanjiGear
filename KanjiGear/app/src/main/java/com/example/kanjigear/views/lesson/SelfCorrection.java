package com.example.kanjigear.views.lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.LearnElement;
import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseContentLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;

public class SelfCorrection extends AppCompatActivity {

    private TextView viewContent;
    private TextView viewAnswerWriting;
    private TextView viewAnswerMeaning;
    private Button viewShow;

    private DatabaseOpenHelper db;
    private Kanji kanji;
    private Word word;
    private int writingIndex;
    private Sentence sentence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_correction);

        loadResources();

        Intent intent = getIntent();
        String intentSymbol = null;
        if (intent.hasExtra("symbol")) {
            intentSymbol = intent.getStringExtra("symbol");
        }
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

        loadContent(intentSymbol, intentWID, intentWIndex, intentSID);
        viewAnswerMeaning.setVisibility(View.INVISIBLE);
        viewAnswerWriting.setVisibility(View.INVISIBLE);
        setContentText();
    }

    private void loadResources() {
        viewContent = findViewById(R.id.selfCorrectionContent);
        viewAnswerWriting = findViewById(R.id.selfCorrectionAnswerWriting);
        viewAnswerMeaning = findViewById(R.id.selfCorrectionAnswerMeaning);
        viewShow = findViewById(R.id.selfCorrectionShowAnswer);
        db = new DatabaseOpenHelper(getApplicationContext());
    }

    private void loadContent(String symbol, String WID, int writingIndex, String SID) {
        DatabaseContentLoader loadHelper = new DatabaseContentLoader();

        if (symbol != null) {
            kanji = loadHelper.getKanji(db, symbol);
        }
        if (WID != null) {
            word = loadHelper.getWord(db, WID);
            word.setWritingIndex(writingIndex);
            this.writingIndex = writingIndex;
        }
        if (SID != null) {
            sentence = new DatabaseContentLoader().getSentence(db, SID);
        }
    }

    public void setContentText() {
        if (kanji != null) {
            viewContent.setText(kanji.getSymbol());
        }
        if (word != null) {
            viewContent.setText(word.getWordWritings().get(writingIndex));
        }
        if (sentence != null) {
            viewContent.setText(sentence.getText());
        }
    }

    public void showAnswer(View v) {
        if (kanji != null) {
            viewAnswerWriting.setText(kanji.getReadingsString("",", "));
            viewAnswerMeaning.setText(kanji.getMeaningsString(""));
        }
        if (word != null) {
            viewAnswerWriting.setText(word.getWordReadings().get(0));
            viewAnswerMeaning.setText(word.getTranslationString(""));
        }
        if (sentence != null) {
            viewAnswerWriting.setText((sentence.getWordReadingString()));
            viewAnswerMeaning.setText(sentence.getMeanings().get(0).getMeaning());
        }

        viewShow.setVisibility(View.INVISIBLE);
        viewAnswerMeaning.setVisibility(View.VISIBLE);
        viewAnswerWriting.setVisibility(View.VISIBLE);
    }

    public void clickedRight(View v) {
        openNextTask(true);
    }

    public void clickedWrong(View v) {
        openNextTask(false);
    }

    public LearnElement getElement() {
        if (sentence != null) {
            return sentence;
        }
        if (word != null) {
            return word;
        }
        if (kanji != null) {
            return kanji;
        }
        return null;
    }

    public void openNextTask(boolean right) {
        LessonBuilder builder = new LessonBuilder(this);
        Intent intent = getIntent();
        String lesson = intent.getStringExtra("lesson");
        String originalLesson = intent.getStringExtra("originalLesson");

        ProgressManager progress = new ProgressManager(this);
        progress.updateElementProgress(getElement(), right);

        if (!right) {
            lesson = builder.addTaskToLesson(lesson, getElement(), builder.getTASK_ID_SELFCORRECTION());
        }

        Intent nextTask = builder.getLessonIntentFromString(lesson, originalLesson);
        if (nextTask != null) {
            startActivity(nextTask);
        }

        finish();
    }


}