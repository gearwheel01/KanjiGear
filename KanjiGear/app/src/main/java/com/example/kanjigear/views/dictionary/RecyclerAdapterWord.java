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

import java.util.ArrayList;

public class RecyclerAdapterWord extends RecyclerView.Adapter<RecyclerAdapterWord.wordViewHolder> {

    private ArrayList<Word> words;
    private Dictionary context;

    public RecyclerAdapterWord(Dictionary context, ArrayList<Word> words) {
        this.context = context;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dict_word, parent, false);
        return new wordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterWord.wordViewHolder holder, int position) {
        holder.word.setText(words.get(position).getWordWritings().get(0) + "(" + words.get(position).getWordReadings().get(0) + ")");
        holder.translation.setText(words.get(position).getTranslationString(""));
        holder.bg.setOnClickListener(l -> {
            context.openWord(words.get(position).getWID());
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }
}
