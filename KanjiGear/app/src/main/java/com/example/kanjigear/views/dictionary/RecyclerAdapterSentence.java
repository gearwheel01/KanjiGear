package com.example.kanjigear.views.dictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Sentence;
import com.example.kanjigear.views.components.KanjiView;
import com.example.kanjigear.views.components.WordView;

import java.util.ArrayList;

public class RecyclerAdapterSentence extends RecyclerView.Adapter<RecyclerAdapterSentence.sentenceViewHolder> {

    private ArrayList<Sentence> sentences;
    private WordView context = null;

    public RecyclerAdapterSentence(WordView context, ArrayList<Sentence> sentences) {
        this.context = context;
        this.sentences = sentences;
    }

    public class sentenceViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        private Button bg;

        public sentenceViewHolder(final View view) {
            super(view);
            text = view.findViewById(R.id.itemCompSentence);
            bg = view.findViewById(R.id.itemCompSentenceBG);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterSentence.sentenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comp_sentence, parent, false);
        return new sentenceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterSentence.sentenceViewHolder holder, int position) {
        Sentence s = sentences.get(position);
        holder.text.setText(s.getText());
        holder.bg.setOnClickListener(l -> {
            context.openSentence(s);
        });
    }

    @Override
    public int getItemCount() {
        return sentences.size();
    }
}
