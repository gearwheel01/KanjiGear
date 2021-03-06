package com.example.kanjigear.dataModels;

import android.util.Log;

import java.util.ArrayList;

public class Sentence extends LearnElement {

    private String SID;
    private String text;
    private int learningProgress;
    private ArrayList<SentenceMeaning> meanings;
    private ArrayList<Word> words;
    private int nextTestDate;

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

    public void setText(String text) {this.text = text;}

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

            Character.UnicodeBlock symbolClass = Character.UnicodeBlock.of(c);
            Log.d("word", "char: " + c + ", class: " + symbolClass);

            if (!symbolClass.equals(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)) {
                ret += c;
            } else {
                if (hide) {
                    ret += "???";
                } else {
                    if (c == untilKanji) {
                        hide = true;
                        ret += "???";
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

    public ArrayList<Integer> getKanjiIndexesInSentence() {
        ArrayList<Integer> kanji = new ArrayList<>();
        for (int i = 0; i < text.length(); i += 1) {
            char symbol = text.charAt(i);
            Character.UnicodeBlock symbolClass = Character.UnicodeBlock.of(symbol);
            Log.d("word", "char: " + symbol + ", class: " + symbolClass);

            if ( (!kanji.contains(symbol)) && (symbolClass.equals(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)) ) {
                kanji.add(i);
            }
        }
        return kanji;
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
