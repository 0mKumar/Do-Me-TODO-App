package com.oapps.woc.todoapp.DB;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ToDoRepository {
    private TaskDao mTaskDao;

    public ToDoRepository(Application application) {
        ToDoRoomDatabase db = ToDoRoomDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
    }

    public void insertTask(TaskData task) {
        ToDoRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.insert(task);
        });
    }

    public LiveData<List<TaskData>> getAllTasks() {
        return mTaskDao.getAllTasks();
    }

    public LiveData<List<TaskData>> getTasksToday(long timeMidNight) {
        return mTaskDao.getIncompleteTasksBeforeDate(timeMidNight);
    }

    public LiveData<List<TaskData>> getStarredTasks() {
        return mTaskDao.getStarredTasks();
    }
    public void updateTask(TaskData task) {
        ToDoRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.update(task);
        });
    }
}
