package com.example.kanjigear.views.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.Kanji;

import java.util.ArrayList;

public class RecyclerAdapterKanji extends RecyclerView.Adapter<RecyclerAdapterKanji.kanjiViewHolder> {

    private ArrayList<Kanji> kanji;
    private WordView context;

    public RecyclerAdapterKanji(WordView context, ArrayList<Kanji> kanji) {
        this.context = context;
        this.kanji = kanji;
    }

    public class kanjiViewHolder extends RecyclerView.ViewHolder {
        private Button kanji;

        public kanjiViewHolder(final View view) {
            super(view);
            kanji = view.findViewById(R.id.itemCompKanji);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterKanji.kanjiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comp_kanji, parent, false);
        return new kanjiViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterKanji.kanjiViewHolder holder, int position) {
        holder.kanji.setText(kanji.get(position).getSymbol());
        holder.kanji.setOnClickListener(l -> {
            context.openKanji(kanji.get(position).getSymbol());
        });
    }

    @Override
    public int getItemCount() {
        return kanji.size();
    }
}
