package com.oapps.woc.todoapp.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oapps.woc.todoapp.DB.TaskData;
import com.oapps.woc.todoapp.R;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksViewHolder> {
    private List<TaskData> mDataset;
    private Context context;

    public TasksAdapter(Context context) {
        this.context = context;
    }

    public void setDataset(List<TaskData> data) {
        mDataset = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item_layout, parent, false);

        TasksViewHolder vh = new TasksViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
        holder.title.setText(mDataset.get(position).title);
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }
}
