package com.oapps.woc.todoapp.DB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.List;

public class ToDoViewModel extends AndroidViewModel {
    public ToDoRepository repository;
    private LiveData<List<TaskData>> allTasks;
    private LiveData<List<TaskData>> starredTasks;
    private LiveData<List<TaskData>> tasksToday;
//    private LiveData<List<TaskData>> tasksPlanned;


    public ToDoViewModel(@NonNull Application application) {
        super(application);
        repository = new ToDoRepository(application);
        allTasks = repository.getAllTasks();
        starredTasks = repository.getStarredTasks();
        // today
        Calendar date = Calendar.getInstance();
        // reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        // next day
        date.add(Calendar.DAY_OF_MONTH, 1);
        tasksToday = repository.getTasksToday(date.getTime().getTime());
    }

    public LiveData<List<TaskData>> getAllTasks() {
        return allTasks;
    }

    public LiveData<List<TaskData>> getTasksToday() {
        return tasksToday;
    }

    public LiveData<List<TaskData>> getStarredTasks() {
        return starredTasks;
    }
}
