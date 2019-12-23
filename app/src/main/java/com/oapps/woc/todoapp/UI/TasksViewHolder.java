package com.oapps.woc.todoapp.UI;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oapps.woc.todoapp.R;

public class TasksViewHolder extends RecyclerView.ViewHolder {
    TextView title, subTitle;
    ImageView radio, star;

    public TasksViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        subTitle = itemView.findViewById(R.id.subtitle);
        radio = itemView.findViewById(R.id.radio);
        star = itemView.findViewById(R.id.star);
    }
}
