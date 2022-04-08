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

public class RecyclerAdapterWordWriting extends RecyclerView.Adapter<RecyclerAdapterWordWriting.wordViewHolder> {

    private ArrayList<Word> words;
    private AddNewSentence context = null;
    private boolean add;

    public RecyclerAdapterWordWriting(AddNewSentence context, ArrayList<Word> words, boolean add) {
        this.context = context;
        this.words = words;
        this.add = add;
    }

    public class wordViewHolder extends RecyclerView.ViewHolder {
        private TextView word;
        private Button add;
        private Button up;
        private Button down;

        public wordViewHolder(final View view) {
            super(view);
            word = view.findViewById(R.id.itemCompWordWritingWriting);
            add = view.findViewById(R.id.itemCompWordWritingButton);
            up = view.findViewById(R.id.itemCompWordWritingUp);
            down = view.findViewById(R.id.itemCompWordWritingDown);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterWordWriting.wordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comp_wordwriting, parent, false);
        return new wordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterWordWriting.wordViewHolder holder, int position) {
        Word w = words.get(position);

        if (w.getWordWritings().size() > 0) {
            holder.word.setText(w.getWordWritings().get(0) + "(" + w.getWordReadings().get(0) + ")");
        }
        if (add) {
            holder.add.setBackgroundResource(R.drawable.button_additem);
            holder.up.setVisibility(View.INVISIBLE);
            holder.down.setVisibility(View.INVISIBLE);
            holder.add.setOnClickListener(l -> {
                context.addWord(w);
            });
        }
        else {
            holder.add.setBackgroundResource(R.drawable.button_removeitem);
            holder.add.setOnClickListener(l -> {
                context.removeWord(w);
            });

            if (words.indexOf(w) > 0) {
                holder.up.setOnClickListener(l -> {
                    context.moveWordUp(w);
                });
            }
            else {
                holder.up.setVisibility(View.INVISIBLE);
            }

            if (words.indexOf(w) < words.size() - 1) {
                holder.down.setOnClickListener(l -> {
                    context.moveWordDown(w);
                });
            }
            else {
                holder.down.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return words.size();
    }
}
