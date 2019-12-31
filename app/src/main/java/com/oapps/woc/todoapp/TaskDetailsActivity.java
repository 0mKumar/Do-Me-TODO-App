package com.oapps.woc.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.oapps.woc.todoapp.DB.TaskData;
import com.oapps.woc.todoapp.DB.ToDoViewModel;

public class TaskDetailsActivity extends AppCompatActivity {

    private ToDoViewModel todoViewModel;
    private TaskData taskData;
    EditText taskEdit;
    ImageView starImageView;
    ImageView completedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        taskEdit = findViewById(R.id.take_task_edit);
        starImageView = findViewById(R.id.image_view_star);
        completedButton = findViewById(R.id.completed_button);

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
