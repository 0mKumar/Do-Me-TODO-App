package com.oapps.woc.todoapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.oapps.woc.todoapp.DB.TaskData;
import com.oapps.woc.todoapp.DB.ToDoViewModel;
import com.oapps.woc.todoapp.UI.DatePickerFragment;
import com.oapps.woc.todoapp.UI.TasksAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TasksActivity extends AppCompatActivity {

    final int requestCodeForVoiceInput = 101;
    LinearLayout bottomTakeTask;
    EditText taskEditText;
    FloatingActionButton fab;
    private ToDoViewModel todoViewModel;
    RecyclerView recyclerView;
    TasksAdapter adapter;

    static final String TODAY = "Today";
    static final String IMPORTANT = "Important";
    static final String PENDING = "Pending";
    static final String ALL_TASKS = "All Tasks";

    private String title;
    Calendar reminderDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent incomingIntent = getIntent();
        title = incomingIntent.getStringExtra("title");
        if (title == null) title = TODAY;
        toolbar.setTitle(title);

        todoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ToDoViewModel.class);

        recyclerView = findViewById(R.id.tasks_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TasksAdapter(this, todoViewModel.repository);
        recyclerView.setAdapter(adapter);

        int color = R.color.background_tasks;
        switch (title) {
//            default:
//            case TODAY:
//                color = R.color.background_today;
//                break;
//            case IMPORTANT:
//                color = R.color.background_important;
//                break;
//            case ALL_TASKS:
//                color = R.color.background_tasks;
//                break;
            case PENDING:
                color = R.color.background_pending;
                break;
        }
        CoordinatorLayout layout = findViewById(R.id.tasks_layout);
        layout.setBackgroundColor(getResources().getColor(color));

        LiveData<List<TaskData>> titleTaskList;
        switch (title) {
            default:
            case TODAY:
                titleTaskList = todoViewModel.getTasksToday();
                break;
            case IMPORTANT:
                titleTaskList = todoViewModel.getStarredTasks();
                break;
            case ALL_TASKS:
                titleTaskList = todoViewModel.getAllTasks();
                break;
            case PENDING:
                titleTaskList = todoViewModel.getPendingTasks();
                break;
        }
        titleTaskList.observe(this, taskData -> {
            Log.d("MyToDo", taskData.size() + " is curr size");
            adapter.setDataset(taskData);
        });

        ImageView voiceInput = findViewById(R.id.voice_input);
        voiceInput.setOnClickListener(view -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Add Task");
            try {
                startActivityForResult(intent, requestCodeForVoiceInput);
            } catch (ActivityNotFoundException a) {

            }
        });


        Chip dueDateChip = findViewById(R.id.due_date);
        dueDateChip.setOnClickListener(view -> {
            Calendar calendar = (Calendar) dueDateChip.getTag();
            DialogFragment dialog = new DatePickerFragment(TasksActivity.this, calendar, (datePicker, y, m, d) -> {
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
                dueDateChip.setText(String.format("Due %s", Utils.getDateFormatted(now, c.getTime())));
                dueDateChip.setTag(c);
                dueDateChip.setCloseIconVisible(true);
            });
            dialog.show(getSupportFragmentManager(), "datePicker");
        });
        dueDateChip.setOnCloseIconClickListener(view -> {
            dueDateChip.setCloseIconVisible(false);
            dueDateChip.setText("Due date");
            dueDateChip.setTag(null);
        });

        Chip reminderChip = findViewById(R.id.reminder);
        reminderChip.setOnClickListener(view -> {
            final Calendar currentDate = reminderChip.getTag() == null ? Calendar.getInstance() : (Calendar) reminderChip.getTag();
            reminderDate = Calendar.getInstance();
            new DatePickerDialog(this, (view12, year, monthOfYear, dayOfMonth) -> {
                reminderDate.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(TasksActivity.this, (view1, hourOfDay, minute) -> {
                    reminderDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    reminderDate.set(Calendar.MINUTE, minute);
                    Date now = Calendar.getInstance().getTime();
                    reminderChip.setText(String.format("Reminds %s %s",
                            Utils.getDateFormatted(now, reminderDate.getTime()),
                            Utils.getTimeFormatted(now, reminderDate.getTime())));
                    reminderChip.setTag(reminderDate);
                    reminderChip.setCloseIconVisible(true);
                }, currentDate.get(Calendar.HOUR_OF_DAY), 0, false).show();
            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
        });

        reminderChip.setOnCloseIconClickListener(v -> {
            reminderChip.setCloseIconVisible(false);
            reminderChip.setText("Set Reminder");
            reminderChip.setTag(null);
        });

        bottomTakeTask = findViewById(R.id.bottom_task_layout);
        fab = findViewById(R.id.fab);
        taskEditText = findViewById(R.id.take_task_edit);

        taskEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    voiceInput.setVisibility(View.VISIBLE);
                } else if (voiceInput.getVisibility() == View.VISIBLE) {
                    voiceInput.setVisibility(View.GONE);
                }
            }
        });

        fab.setOnClickListener(view -> {
            fab.hide();
            if (TODAY.equals(title)) {
                Calendar c = Calendar.getInstance();
                Date now = Utils.getCalenderDayForDate(c.getTime());
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
//                c.add(Calendar.DAY_OF_MONTH, 1);
                dueDateChip.setTag(c);
                dueDateChip.setText(String.format("Due %s", Utils.getDateFormatted(now, c.getTime())));
                dueDateChip.setCloseIconVisible(true);
            }
            bottomTakeTask.setVisibility(LinearLayout.VISIBLE);
            taskEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(taskEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        ImageView addTaskButton = findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(view -> {
            TaskData data = new TaskData();
            data.title = taskEditText.getText().toString();
            Calendar c = (Calendar) dueDateChip.getTag();
            if (c != null) {
                data.dueDate = c.getTime();
            }
            if (IMPORTANT.equals(title)) {
                data.starred = true;
            }
            taskEditText.setText("");
            todoViewModel.repository.insertTask(data);
            Snackbar.make(recyclerView, "Task added", Snackbar.LENGTH_SHORT).show();
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (bottomTakeTask.getVisibility() == View.VISIBLE) {
            bottomTakeTask.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(taskEditText.getWindowToken(), 0);
            }
            fab.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
//        bottomTakeTask.setVisibility(View.GONE);
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case requestCodeForVoiceInput:
                if (data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result != null && result.size() > 0) {
                        String voiceInput = result.get(0);
                        if (voiceInput.length() > 0) {
                            voiceInput = voiceInput.substring(0, 1).toUpperCase() + voiceInput.substring(1);
                        }
                        taskEditText.setText(voiceInput);
                    }
                }
                taskEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(taskEditText, InputMethodManager.SHOW_IMPLICIT);
                }
                taskEditText.setSelection(taskEditText.getText().length());
                break;
        }
    }

}
