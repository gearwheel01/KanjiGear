package com.example.kanjigear.views.lesson;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.example.kanjigear.dataModels.Kanji;
import com.example.kanjigear.dataModels.LearnElement;
import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.dataModels.StudyList;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.db.DatabaseContentLoader;
import com.example.kanjigear.db.DatabaseModelLoader;
import com.example.kanjigear.db.DatabaseOpenHelper;
import com.example.kanjigear.views.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class LessonBuilder {

    private DatabaseOpenHelper db;
    private Context context;

    private ArrayList<StudyList> lists;
    private ArrayList<LearnElement> elementsNew;
    private ArrayList<LearnElement> elementsRevise;

    private ArrayList<LearnElement> elementsLesson;

    private final int NUMBER_ELEMENTS_NEW = 3;
    private final int NUMBER_ELEMENTS_REVISE = 7;
    private final int EARLY_LEARNING = 30;

    private final String TASK_ID_SELFCORRECTION = "S";
    private final String TASK_ID_DRAW = "D";

    private final String TASK_ID_TASK_SEP = ",";

    private final String TASK_ID_SENTENCE = "SID";
    private final String TASK_ID_WORD = "WID";
    private final String TASK_ID_KANJI = "symbol";
    private final String TASK_ID_INFO_SEP = "-";


    public LessonBuilder(Context context) {
        db = new DatabaseOpenHelper(context);
        this.context = context;
    }

    public String buildLesson() {
        getElements();
        elementsLesson = new ArrayList<>();
        addElementsToLesson(elementsRevise, NUMBER_ELEMENTS_REVISE);
        addElementsToLesson(elementsNew, NUMBER_ELEMENTS_NEW);
        Collections.shuffle(elementsLesson);

        logList(elementsLesson);

        String lessonString = "";
        for (int i = 0; i < elementsLesson.size(); i += 1) {
            LearnElement element = elementsLesson.get(i);
            if (element.getLearningProgress() <= EARLY_LEARNING) {
                lessonString = addTaskToLesson(lessonString, elementsLesson.get(i), TASK_ID_SELFCORRECTION);
            }
            else {
                lessonString = addTaskToLesson(lessonString, elementsLesson.get(i), TASK_ID_DRAW);
            }
        }
        for (int i = 0; i < elementsLesson.size(); i += 1) {
            LearnElement element = elementsLesson.get(i);
            if (element.getLearningProgress() <= EARLY_LEARNING) {
                lessonString = addTaskToLesson(lessonString, elementsLesson.get(i), TASK_ID_DRAW);
            }
            else {
                lessonString = addTaskToLesson(lessonString, elementsLesson.get(i), TASK_ID_SELFCORRECTION);
            }
        }
        Log.d("lesson", lessonString);

        return lessonString;
    }

    public Intent getLessonIntentFromString(String lessonString, String originalLesson) {
        Intent intent;

        if (lessonString.length() > 1) {
            String task = getTokens(lessonString, TASK_ID_TASK_SEP).get(0);
            ArrayList<String> taskDetails = getTokens(task, TASK_ID_INFO_SEP);

            String type = taskDetails.get(0);
            String element = taskDetails.get(2);
            String id = taskDetails.get(3);

            String newLesson = "";
            int afterFirstTask = lessonString.indexOf(TASK_ID_TASK_SEP) + 1;
            if (afterFirstTask > 0) {
                newLesson = lessonString.substring(afterFirstTask);
            }

            Log.d("lesson", "remaining lesson: " + newLesson);

            if (type.equals(TASK_ID_DRAW)) {
                intent = new Intent(context, DrawKanji.class);
            }
            else {
                intent = new Intent(context, SelfCorrection.class);
            }
            intent.putExtra(element, id);
            intent.putExtra("originalLesson", originalLesson);
            intent.putExtra("lesson", newLesson);
            if (element.equals(TASK_ID_WORD)) {
                intent.putExtra("writingIndex", Integer.parseInt(taskDetails.get(4)));
            }
        }
        else {
            intent = null;
            rescheduleAllElements(originalLesson);
        }

        return  intent;
    }

    public ArrayList<String> getTokens(String s, String seperator) {
        ArrayList<String> tokens = new ArrayList<>();
        StringTokenizer t = new StringTokenizer(s, seperator);
        while (t.hasMoreElements()) {
            tokens.add(t.nextToken());
        }
        return tokens;
    }

    public String addTaskToLesson(String currentLesson, LearnElement element, String type) {
        String addTask = "";
        if (!currentLesson.equals("")) {
            addTask += TASK_ID_TASK_SEP;
        }
        addTask += type + TASK_ID_INFO_SEP + element.getInList() + TASK_ID_INFO_SEP + elementToTaskString(element);

        return currentLesson + addTask;
    }

    private String elementToTaskString(LearnElement element) {
        String ret = "";
        if (element.getClass().getSimpleName().equals("Kanji")) {
            Kanji k = (Kanji)element;
            ret += TASK_ID_KANJI + TASK_ID_INFO_SEP + k.getSymbol();
        }
        else if (element.getClass().getSimpleName().equals("Word")) {
            Word w = (Word)element;
            ret += TASK_ID_WORD + TASK_ID_INFO_SEP + w.getWID() + TASK_ID_INFO_SEP + w.getWritingIndex();
        }
        else if (element.getClass().getSimpleName().equals("Sentence")) {
            Sentence s = (Sentence)element;
            ret += TASK_ID_SENTENCE + TASK_ID_INFO_SEP + s.getSID();
        }
        return ret;
    }

    private void addElementsToLesson(ArrayList<LearnElement> list, int elements) {
        for (int i = 0; i < elements; i += 1) {
            if (i < list.size()) {
                elementsLesson.add(list.get(i));
            }
        }
    }

    public void getActiveStudyLists() {
        db.openDatabaseRead();
        Cursor c = db.handleQuery("SELECT * FROM studylist WHERE isActive = 1;");
        lists = new DatabaseModelLoader().getStudyListsFromCursor(c);
        db.closeDatabase();
    }

    public void getElements() {
        getActiveStudyLists();
        elementsRevise = new ArrayList<>();
        elementsNew = new ArrayList<>();

        for (int i = 0; i < lists.size(); i += 1) {
            DatabaseContentLoader loader = new DatabaseContentLoader();
            sortElementsNewRevise(loader.sentenceListToElementList(loader.getSentencesInList(db, lists.get(i).getSLID())));
            sortElementsNewRevise(loader.wordListToElementList(loader.getWordsInList(db, lists.get(i).getSLID())));
            sortElementsNewRevise(loader.kanjiListToElementList(loader.getKanjiInList(db, lists.get(i).getSLID())));
        }

        Collections.sort(elementsRevise);
        Collections.shuffle(elementsNew);
    }

    public void sortElementsNewRevise(ArrayList<LearnElement> s) {
        for (int i = 0; i < s.size(); i += 1) {
            if (s.get(i).getNextTestDate() == 0) {
                elementsNew.add(s.get(i));
            }
            else {
                elementsRevise.add(s.get(i));
            }
        }
    }

    public void rescheduleAllElements(String lesson) {
        ArrayList<LearnElement> elements = getElementsInLesson(lesson);
        ProgressManager progress = new ProgressManager(context);
        for (int i = 0; i < elements.size(); i += 1) {
            progress.scheduleNextRevision(elements.get(i));
        }
    }


    public void logList(ArrayList<LearnElement> list) {
        Log.d("builder","log List");
        for (int i = 0; i < list.size(); i += 1) {
            Log.d("builder",list.get(i).getNextTestDate() + " (" + list.get(i).toString() + ")");
        }
    }

    public ArrayList<LearnElement> getElementsInLesson(String lesson) {
        ArrayList<LearnElement> elements = new ArrayList<>();
        ArrayList<String> addedKanji = new ArrayList<>();
        ArrayList<String> addedWords = new ArrayList<>();
        ArrayList<String> addedSentences = new ArrayList<>();

        DatabaseContentLoader loader = new DatabaseContentLoader();
        ArrayList<String> tasks = getTokens(lesson, TASK_ID_TASK_SEP);
        for (int i = 0; i < tasks.size(); i += 1) {
            ArrayList<String> details = getTokens(tasks.get(i), TASK_ID_INFO_SEP);
            String SLID = details.get(1);
            String type = details.get(2);
            String id = details.get(3);
            if ( (type.equals(TASK_ID_KANJI)) && (!addedKanji.contains(id)) ) {
                Kanji k = loader.getKanji(db, id);
                k.setInList(SLID);
                elements.add(k);
                addedKanji.add(id);
            }
            if ( (type.equals(TASK_ID_WORD)) && (!addedWords.contains(id)) ) {
                Word w = loader.getWord(db, id);
                w.setInList(SLID);
                elements.add(w);
                addedWords.add(id);
            }
            if ( (type.equals(TASK_ID_SENTENCE)) && (!addedSentences.contains(id)) ) {
                Sentence s = loader.getSentence(db, id);
                s.setInList(SLID);
                elements.add(s);
                addedSentences.add(id);
            }
        }

        return elements;
    }

    public String getTASK_ID_SELFCORRECTION() {
        return TASK_ID_SELFCORRECTION;
    }

    public String getTASK_ID_DRAW() {
        return TASK_ID_DRAW;
    }
}
