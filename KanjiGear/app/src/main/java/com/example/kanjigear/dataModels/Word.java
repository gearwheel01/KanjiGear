package com.example.kanjigear.dataModels;

import android.util.Log;

import java.util.ArrayList;

public class Word {

    private String WID;
    private int learningProgress;
    private int frequency;

    private String notkanjichars=" あいうえおかきくけこがぎぐげごさしすせそざじずぜぞたちつてとだぢづでどなにぬねのはひふへほばびぶべぼぱぴぷぺぽまみむめもやゆよらりるれろわをんっゃょゅぁぃぅぇぉゖゕ"
            + "アイウエオカキクケコガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨラリルレロワヲンーャョュァィゥェォヵヶッ"
            +"abcdefghaijklmnopqrstuvwxvzöäüABCDEFGHIJKLMNOPQRSTUVWXYZÖÄÜ1234567890<>|-_+.:,;。．.・／１２３４５６７８９０";

    private ArrayList<String> wordWritings;
    private ArrayList<String> wordReadings;
    private ArrayList<WordMeaning> wordTranslations;

    public Word(String WID, int learningProgress, int frequency)
    {
        this.WID = WID;
        this.learningProgress = learningProgress;
        this.frequency = frequency;

        wordTranslations = new ArrayList<>();
        wordReadings = new ArrayList<>();
        wordWritings = new ArrayList<>();
    }

    public String getWID() {
        return WID;
    }

    public int getLearningProgress() {
        return learningProgress;
    }

    public void setTranslations(ArrayList<WordMeaning> t) {wordTranslations = t;}

    public ArrayList<WordMeaning> getWordTranslations() {return wordTranslations;}

    // empty string for no lang criteria
    public String getTranslationString(String lang) {
        String ret = "";
        for (int i = 0; i < wordTranslations.size(); i += 1) {
            if ( (lang.equals("")) || (lang.equals(wordTranslations.get(i).getLanguage())) ) {
                if (!ret.equals("")) {
                    ret += ", ";
                }
                ret += wordTranslations.get(i).getMeaning();
            }
        }
        return ret;
    }

    public ArrayList<String> getWordWritings() {
        return wordWritings;
    }

    public void setWordWritings(ArrayList<String> wordWritings) {
        this.wordWritings = wordWritings;
    }

    public ArrayList<String> getWordReadings() {
        return wordReadings;
    }

    public void setWordReadings(ArrayList<String> wordReadings) {
        this.wordReadings = wordReadings;
    }

    public int getFrequency() {
        return frequency;
    }

    public ArrayList<Character> getKanjiInWord(int writingIndex) {
        ArrayList<Character> kanji = new ArrayList<>();
        String writing = wordWritings.get(writingIndex);
        for (int c = 0; c < writing.length(); c += 1) {
            char symbol = writing.charAt(c);
            if ( (!notkanjichars.contains(symbol + "")) && (!kanji.contains(symbol)) ) {
                kanji.add(symbol);
            }
        }
        return kanji;
    }


    // returns kanji after param (' ' if last)
    public char getNextKanji(int writingIndex, char symbol) {
        ArrayList<Character> kanji = getKanjiInWord(writingIndex);
        for (int i = 0; i < kanji.size(); i += 1) {
            if (symbol == kanji.get(i)) {
                if (i == kanji.size() - 1) {
                    return ' ';
                }
                return kanji.get(i + 1);
            }
        }
        return ' ';
    }

    public boolean isLastKanji(int writingIndex, char symbol) {
        Log.d("word","call method last with " + symbol);
        ArrayList<Character> kanji = getKanjiInWord(writingIndex);
        if (kanji.size() > 0) {
            Log.d("word","last: " + kanji.get(kanji.size() - 1));
            return (kanji.get(kanji.size() - 1) == symbol);
        }
        return false;
    }

    public String getKanjiHiddenWriting(int writingIndex, char untilKanji) {
        if (untilKanji == ' ') {
            return wordWritings.get(writingIndex);
        }
        boolean hide = false;
        String ret = "";
        String writing = wordWritings.get(writingIndex);
        for (int i = 0; i < writing.length(); i += 1) {
            char c = writing.charAt(i);
            if (notkanjichars.contains(c + "")) {
                ret += c;
            } else {
                if (hide) {
                    ret += "_";
                } else {
                    if (c == untilKanji) {
                        hide = true;
                        ret += "_";
                    } else {
                        ret += c;
                    }
                }
            }
        }
        return ret;
    }
}
