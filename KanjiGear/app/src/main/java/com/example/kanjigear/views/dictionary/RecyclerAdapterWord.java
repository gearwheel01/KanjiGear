package com.example.kanjigear.views.dictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Word;
import com.example.kanjigear.views.components.KanjiView;
import com.example.kanjigear.views.components.SentenceView;
import com.example.kanjigear.views.studyLists.StudyListDetails;

import java.util.ArrayList;

public class RecyclerAdapterWord extends RecyclerView.Adapter<RecyclerAdapterWord.wordViewHolder> {

    private ArrayList<Word> words;
    private Dictionary contextDictionary = null;
    private KanjiView contextKanji = null;
    private SentenceView contextSentence = null;
    private StudyListDetails contextList = null;

    public RecyclerAdapterWord(Dictionary contextDictionary, ArrayList<Word> words) {
        this.contextDictionary = contextDictionary;
        this.words = words;
    }

    public RecyclerAdapterWord(KanjiView contextKanji, ArrayList<Word> words) {
        this.contextKanji = contextKanji;
        this.words = words;
    }

    public RecyclerAdapterWord(SentenceView contextSentence, ArrayList<Word> words) {
        this.contextSentence = contextSentence;
        this.words = words;
    }

    public RecyclerAdapterWord(StudyListDetails contextList, ArrayList<Word> words) {
        this.contextList = contextList;
        this.words = words;
    }

    public class wordViewHolder extends RecyclerView.ViewHolder {
        private TextView word;
        private TextView translation;
        private Button bg;

        public wordViewHolder(final View view) {
            super(view);
            word = view.findViewById(R.id.word);
            translation = view.findViewById(R.id.translation);
            bg = view.findViewById(R.id.itemCompKanji);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterWord.wordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comp_word, parent, false);
        return new wordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterWord.wordViewHolder holder, int position) {
        Word w = words.get(position);
        if (w.getWordWritings().size() > 0) {
            int writingIndex = 0;
            if (w.getWritingIndex() > -1) {
                writingIndex = w.getWritingIndex();
            }
            holder.word.setText(w.getWordWritings().get(writingIndex) + "(" + w.getWordReadings().get(0) + ")");
        } else {
            holder.word.setText(w.getWordReadings().get(0));
        }
        holder.translation.setText(words.get(position).getTranslationString(""));
        holder.bg.setOnClickListener(l -> {
            if (contextDictionary != null) {
                contextDictionary.openWord(w.getWID());
            }
            if (contextKanji != null) {
                contextKanji.openWord(w.getWID());
            }
            if (contextSentence != null) {
                contextSentence.openWord(w);
            }
            if (contextList != null) {
                contextList.openWord(w);
            }
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }
}
