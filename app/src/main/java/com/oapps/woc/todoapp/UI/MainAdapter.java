package com.oapps.woc.todoapp.UI;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.oapps.woc.todoapp.R;
import com.oapps.woc.todoapp.TasksActivity;

import java.util.ArrayList;
import java.util.Locale;


public class MainAdapter extends RecyclerView.Adapter<MainCategoryListViewHolder> {
    private ArrayList<TextWithDrawableData> mDataset;
    private Activity context;

    public MainAdapter(Activity context, ArrayList<TextWithDrawableData> myDataset) {
        mDataset = myDataset;
        this.context = context;
    }

    @Override
    public MainCategoryListViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        switch (viewType) {
            default:
            case 1:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_line_list_item, parent, false);

                MainCategoryListViewHolder vh = new MainCategoryListViewHolder(v);
                return vh;
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return 1;
    }

    @Override
    public void onBindViewHolder(MainCategoryListViewHolder holder, final int position) {
        holder.textView.setText(mDataset.get(position).text);
        holder.imageView.setImageResource(mDataset.get(position).drawable);
        holder.imageView.setColorFilter(context.getResources().getColor(mDataset.get(position).color));
        holder.countPrimary.setText(String.format(Locale.US, "%d", mDataset.get(position).countPrimary));
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, TasksActivity.class);
            intent.putExtra("title", mDataset.get(position).text);
//            Bundle options = ActivityOptionsCompat.makeScaleUpAnimation(
//                    holder.itemView, 0, 0,
//                    holder.itemView.getWidth(),
//                    holder.itemView.getHeight()).toBundle();
//            context.startActivity(intent, options);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
