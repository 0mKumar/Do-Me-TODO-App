package com.oapps.woc.todoapp.UI;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oapps.woc.todoapp.R;

public class MainCategoryListViewHolder extends RecyclerView.ViewHolder {
    TextView textView, countPrimary;
    ImageView imageView;

    public MainCategoryListViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textView);
        imageView = itemView.findViewById(R.id.imageView);
        countPrimary = itemView.findViewById(R.id.countPrimary);
    }
}
