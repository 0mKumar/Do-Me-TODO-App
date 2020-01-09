package com.oapps.woc.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.oapps.woc.todoapp.DB.TaskData;
import com.oapps.woc.todoapp.DB.ToDoViewModel;
import com.oapps.woc.todoapp.UI.DatePickerFragment;

import java.util.Calendar;
import java.util.Date;

public class TaskDetailsActivity extends AppCompatActivity {

    private ToDoViewModel todoViewModel;
    private TaskData taskData;
    EditText taskEdit;
    ImageView starImageView;
    ImageView completedButton;
    TextView dueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        taskEdit = findViewById(R.id.take_task_edit);
        starImageView = findViewById(R.id.image_view_star);
        completedButton = findViewById(R.id.completed_button);
        dueTextView = findViewById(R.id.tv_due_date);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent incomingIntent = getIntent();
        int task_id = incomingIntent.getIntExtra("task_id", -1);
        todoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ToDoViewModel.class);

        if (task_id != -1) {
            LiveData<TaskData> tasklive = todoViewModel.getTaskById(task_id);
            LifecycleOwner owner = this;
            tasklive.observe(owner, task -> {
                taskData = task;
                taskEdit.setText(task.title);
                starImageView.setImageResource(task.starred ? R.drawable.ic_star_24dp : R.drawable.ic_star_border_24dp);
                completedButton.setImageResource(taskData.completed ? R.drawable.ic_radio_button_checked_24dp : R.drawable.ic_radio_button_unchecked_24dp);
                completedButton.getDrawable().setTint(getResources().getColor(taskData.completed ? R.color.colorPrimary : R.color.star_grey));
                if (task.dueDate != null) {
                    Date now = Calendar.getInstance().getTime();
                    dueTextView.setText(String.format("Due %s", Utils.getDateFormatted(now, task.dueDate)));
                } else {
                    dueTextView.setText("Set due date");
                }
                tasklive.removeObservers(owner);
            });
        }
        starImageView.setOnClickListener(view -> {
            taskData.starred = !taskData.starred;
            starImageView.setImageResource(taskData.starred ? R.drawable.ic_star_24dp : R.drawable.ic_star_border_24dp);
        });
        taskEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                taskData.title = editable.toString();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        todoViewModel.repository.updateTask(taskData);
    }

    public void viewOnDueDateClicked(View view) {
        Calendar calendar = Calendar.getInstance();
        if (taskData.dueDate != null) {
            calendar.setTime(taskData.dueDate);
        }
        DialogFragment dialog = new DatePickerFragment(TaskDetailsActivity.this, calendar, (datePicker, y, m, d) -> {
            Log.d("MyTodo", d + "/" + m + "/" + y);
            Calendar c = Calendar.getInstance();
            Date now = c.getTime();
            c.set(Calendar.YEAR, y);
            c.set(Calendar.MONTH, m);
            c.set(Calendar.DAY_OF_MONTH, d);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            taskData.dueDate = c.getTime();
            dueTextView.setText(String.format("Due %s", Utils.getDateFormatted(now, c.getTime())));
        });
        dialog.show(getSupportFragmentManager(), "datePicker");
    }


    public void viewOnRepeatClicked(View view) {

    }

    public void viewTaskCompletedButton(View view) {
        taskData.completed = !taskData.completed;
        ImageView iv = (ImageView) view;
        iv.setImageResource(taskData.completed ? R.drawable.ic_radio_button_checked_24dp : R.drawable.ic_radio_button_unchecked_24dp);
        iv.getDrawable().setTint(getResources().getColor(taskData.completed ? R.color.colorPrimary : R.color.star_grey));
    }
}
