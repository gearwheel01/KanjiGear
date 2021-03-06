package com.example.kanjigear.views.studyLists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.StudyList;

import java.util.ArrayList;

public class RecyclerAdapterStudyList extends RecyclerView.Adapter<RecyclerAdapterStudyList.studyListViewHolder> {

    private ArrayList<StudyList> studyLists;
    private StudyLists context;

    public RecyclerAdapterStudyList(StudyLists context, ArrayList<StudyList> lists) {
        this.context = context;
        studyLists = lists;
    }

    public class studyListViewHolder extends RecyclerView.ViewHolder {
        private Button name;

        public studyListViewHolder(final View view) {
            super(view);
            name = view.findViewById(R.id.itemCompKanji);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapterStudyList.studyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_studylist, parent, false);
        return new studyListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterStudyList.studyListViewHolder holder, int position) {
        holder.name.setBackgroundResource(studyLists.get(position).isActive()? R.color.teal_200: R.color.teal_700);
        holder.name.setText(studyLists.get(position).getName());
        holder.name.setOnClickListener(l -> {
            context.openListDetails(studyLists.get(position).getSLID());
        });
    }

    @Override
    public int getItemCount() {
        return studyLists.size();
    }
}
