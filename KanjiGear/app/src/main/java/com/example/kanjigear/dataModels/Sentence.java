package com.example.kanjigear.dataModels;

import java.util.ArrayList;

public class Sentence extends LearnElement {

    private String SID;
    private String text;
    private int learningProgress;
    private ArrayList<SentenceMeaning> meanings;
    private ArrayList<Word> words;
    private int nextTestDate;

    private String notkanjichars=" あいうえおかきくけこがぎぐげごさしすせそざじずぜぞたちつてとだぢづでどなにぬねのはひふへほばびぶべぼぱぴぷぺぽまみむめもやゆよらりるれろわをんっゃょゅぁぃぅぇぉゖゕ"
            + "アイウエオカキクケコガギグゲゴサシスセソザジズゼゾタチツテトダヂヅデドナニヌネノハヒフヘホバビブベボパピプペポマミムメモヤユヨラリルレロワヲンーャョュァィゥェォヵヶッ"
            +"abcdefghaijklmnopqrstuvwxvzöäüABCDEFGHIJKLMNOPQRSTUVWXYZÖÄÜ1234567890<>|-_+.:,;。．.・／１２３４５６７８９０";

    public Sentence(String SID, String text, int learningProgress) {
        this.SID = SID;
        this.text = text;
        this.learningProgress = learningProgress;
        meanings = new ArrayList<>();
        words = new ArrayList<>();
    }

    public String getSID() {
        return SID;
    }

    public String getText() {
        return text;
    }

    public int getLearningProgress() {
        return learningProgress;
    }

    public ArrayList<SentenceMeaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(ArrayList<SentenceMeaning> meanings) {
        this.meanings = meanings;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public Word getWord(String WID) {
        for (int i = 0; i < words.size(); i += 1) {
            if (words.get(i).getWID().equals(WID)) {
                return words.get(i);
            }
        }
        return null;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    // returns word after param (null if last)
    public Word getNextWord(String WID) {
        for (int i = 0; i < words.size(); i += 1) {
            if (WID.equals(words.get(i).getWID())) {
                if (i == words.size() - 1) {
                    return null;
                }
                return words.get(i + 1);
            }
        }
        return null;
    }

    public boolean isLastWord(String WID) {
        if (words.size() > 0) {
            return words.get(words.size() - 1).getWID().equals(WID);
        }
        return false;
    }

    public String getKanjiHiddenWriting(char untilKanji) {
        if (untilKanji == ' ') {
            return text;
        }
        boolean hide = false;
        String ret = "";
        for (int i = 0; i < text.length(); i += 1) {
            char c = text.charAt(i);
            if (notkanjichars.contains(c + "")) {
                ret += c;
            } else {
                if (hide) {
                    ret += "〇";
                } else {
                    if (c == untilKanji) {
                        hide = true;
                        ret += "〇";
                    } else {
                        ret += c;
                    }
                }
            }
        }
        return ret;
    }

    public String getWordReadingString() {
        String ret = "";
        for (int i = 0; i < words.size(); i += 1) {
            ret += words.get(i).getWordReadings().get(0);
            if (i < words.size() - 1) {
                ret += ", ";
            }
        }
        return ret;
    }

    public int getNextTestDate() {
        return nextTestDate;
    }

    public void setNextTestDate(int nextTestDate) {
        this.nextTestDate = nextTestDate;
    }

    public String toString() {
        return text;
    }
}
