package com.example.kanjigear.views.studyLists;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanjigear.R;
import com.example.kanjigear.dataModels.StudyList;

import java.util.ArrayList;

public class recyclerAdapterStudyList extends RecyclerView.Adapter<recyclerAdapterStudyList.studyListViewHolder> {

    private ArrayList<StudyList> studyLists;

    public recyclerAdapterStudyList(ArrayList<StudyList> lists) {
        studyLists = lists;
    }

    public class studyListViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public studyListViewHolder(final View view) {
            super(view);
            name = view.findViewById(R.id.studyListName);
        }
    }

    @NonNull
    @Override
    public recyclerAdapterStudyList.studyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_list_item, parent, false);
        return new studyListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapterStudyList.studyListViewHolder holder, int position) {
        String name = studyLists.get(position).getName();
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return studyLists.size();
    }
}
