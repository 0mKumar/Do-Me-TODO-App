package com.oapps.woc.todoapp.DB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ToDoViewModel extends AndroidViewModel {
    public ToDoRepository repository;
    private LiveData<List<TaskData>> allTasks;

    public ToDoViewModel(@NonNull Application application) {
        super(application);
        repository = new ToDoRepository(application);
        allTasks = repository.getAllTasks();
    }
}
