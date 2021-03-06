package com.oapps.woc.todoapp.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oapps.woc.todoapp.DB.TaskData;
import com.oapps.woc.todoapp.DB.ToDoRepository;
import com.oapps.woc.todoapp.R;
import com.oapps.woc.todoapp.TaskDetailsActivity;
import com.oapps.woc.todoapp.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksViewHolder> {
    private List<TaskData> mDataset;
    private Context context;
    private ToDoRepository repository;

    public TasksAdapter(Context context, ToDoRepository repo) {
        this.context = context;
        repository = repo;
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
        TaskData data = mDataset.get(position);
        holder.title.setText(mDataset.get(position).title);
        if (data.completed) {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.card_done));
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.card.setCardBackgroundColor(Color.WHITE);
            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.subTitle.setVisibility(View.VISIBLE);
        holder.star.setImageResource(data.starred ? R.drawable.ic_star_24dp : R.drawable.ic_star_border_24dp);
        holder.radio.setImageResource(data.completed ? R.drawable.ic_radio_button_checked_24dp : R.drawable.ic_radio_button_unchecked_24dp);
        holder.radio.getDrawable().setTint(context.getResources().getColor(data.completed ? R.color.colorPrimary : R.color.star_grey));
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ImageSpan sp;
        if (data.dueDate != null) {
            Date now = Utils.getCalenderDayForDate(Calendar.getInstance().getTime());
            int color = data.dueDate.before(now) ? R.color.due_date_pending : R.color.due_date;
            sp = new ImageSpan(context, R.drawable.ic_today_24dp);
            ssb.append(" ");
            ssb.setSpan(sp, ssb.length() - 1, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //put the image to the prepared space
            CharSequence dueString = DateUtils.getRelativeTimeSpanString(data.dueDate.getTime(), now.getTime(), DateUtils.DAY_IN_MILLIS);
            ssb.append(" Due ");
            ssb.append(dueString);
            ssb.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), ssb.length() - (dueString.length() + 4), ssb.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            ssb.append(" ");
        }

//        ssb.append("\u2022  ");
//        sp = new ImageSpan(context, R.drawable.ic_dot_circle_24dp);
//        sp.getDrawable().setTint(context.getResources().getColor(R.color.colorPrimary));
//        ssb.setSpan(sp, ssb.length() - 1, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //put the image to the prepared space
//        ssb.append(" Tasks  ");

        if (ssb.length() == 0) {
            holder.subTitle.setVisibility(View.GONE);
        } else {
            holder.subTitle.setVisibility(View.VISIBLE);
            holder.subTitle.setText(ssb, TextView.BufferType.SPANNABLE);
        }
        holder.itemView.setOnClickListener(view -> {
            Log.d("TodoMy", "Starting details activity");
            Intent intent = new Intent(context, TaskDetailsActivity.class);
            intent.putExtra("task_id", mDataset.get(position).task_id);
            context.startActivity(intent);
        });
        holder.star.setOnClickListener((view) -> {
            data.starred = !data.starred;
            repository.updateTask(data);
        });
        holder.radio.setOnClickListener(view -> {
            data.completed = !data.completed;
            repository.updateTask(data);
        });
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }
}
