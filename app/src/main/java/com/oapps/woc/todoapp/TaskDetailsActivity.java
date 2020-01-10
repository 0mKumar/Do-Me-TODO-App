package com.oapps.woc.todoapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
    TextView dueTextView, reminderTextView;
    EditText addNoteEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        taskEdit = findViewById(R.id.take_task_edit);
        starImageView = findViewById(R.id.image_view_star);
        completedButton = findViewById(R.id.completed_button);
        dueTextView = findViewById(R.id.tv_due_date);
        addNoteEdit = findViewById(R.id.edit_add_note);
        reminderTextView = findViewById(R.id.tv_reminder_date);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent incomingIntent = getIntent();
        int task_id = incomingIntent.getIntExtra("task_id", -1);
        Log.d("TaskDetailsActivity", "onCreate: task id = " + task_id);
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
                if (task.reminderDate != null) {
                    Date now = Calendar.getInstance().getTime();
                    reminderTextView.setText(String.format("Reminds %s %s",
                            Utils.getDateFormatted(now, task.reminderDate),
                            Utils.getTimeFormatted(now, task.reminderDate)));
                } else {
                    reminderTextView.setText("Set Reminder");
                }
                if (task.note != null) {
                    addNoteEdit.setText(task.note);
                }
                tasklive.removeObservers(owner);
            });
        }
        starImageView.setOnClickListener(view -> {
            taskData.starred = !taskData.starred;
            starImageView.setImageResource(taskData.starred ? R.drawable.ic_star_24dp : R.drawable.ic_star_border_24dp);
        });
        addNoteEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                taskData.note = s.toString();
            }
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
            Date now = Utils.getCalenderDayForDate(c.getTime());
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

    public void viewOnReminderClicked(View v) {
        Calendar currentDate = Calendar.getInstance();
        if (taskData.reminderDate != null) {
            currentDate.setTime(taskData.reminderDate);
        }
        Calendar reminderDate = Calendar.getInstance();
        new DatePickerDialog(this, (view12, year, monthOfYear, dayOfMonth) -> {
            reminderDate.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(TaskDetailsActivity.this, (view1, hourOfDay, minute) -> {
                reminderDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                reminderDate.set(Calendar.MINUTE, minute);
                Date now = Calendar.getInstance().getTime();
                reminderTextView.setText(String.format("Reminds %s %s",
                        Utils.getDateFormatted(now, reminderDate.getTime()),
                        Utils.getTimeFormatted(now, reminderDate.getTime())));
                taskData.reminderDate = reminderDate.getTime();
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    public void deleteClicked(View view) {
//        AlertDialog alert = new AlertDialog.Builder(TaskDetailsActivity.this)
//                .setTitle("Delete task")
//                .setMessage("Are you sure you want to delete this task?")
//                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//
//                })
//                .setNegativeButton(android.R.string.no, null).create();
        todoViewModel.repository.deleteTask(taskData);
        finish();
    }
}
