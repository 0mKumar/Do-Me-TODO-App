package com.oapps.woc.todoapp.DB;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.oapps.woc.todoapp.Utils;

import java.util.List;

public class ToDoRepository {
    public TaskDao mTaskDao;
    private AlarmManager alarmManager;
    private Context context;
    public ToDoRepository(Application application) {
        context = application.getApplicationContext();
        ToDoRoomDatabase db = ToDoRoomDatabase.getDatabase(application);
        alarmManager = (AlarmManager) application.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mTaskDao = db.taskDao();
    }

    public void insertTask(TaskData task) {
        if (task.reminderDate != null) {
            // set reminder
            Utils.setOrUpdateAlarm(context, alarmManager, task);
        }
        ToDoRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.insert(task);
        });
    }

    public void deleteTask(TaskData task) {
        if (task.reminderDate != null) {
            // set or update reminder
            Utils.removeAlarm(context, alarmManager, task);
        }
        ToDoRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.delete(task);
        });
    }

    public void updateTask(TaskData task) {
        if (task.reminderDate != null) {
            // set or update reminder
            Utils.setOrUpdateAlarm(context, alarmManager, task);
        }
        ToDoRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.update(task);
        });
    }

    public LiveData<List<TaskData>> getAllTasks() {
        return mTaskDao.getAllTasks();
    }

    public LiveData<List<TaskData>> getTasksToday(long timeMidNight) {
        return mTaskDao.getIncompleteTasksBetweenDate(timeMidNight - 24 * 60 * 60 * 1000, timeMidNight);
    }

    public LiveData<List<TaskData>> getStarredTasks() {
        return mTaskDao.getStarredTasks();
    }

    public LiveData<TaskData> getTaskById(int id) {
        return mTaskDao.getTaskById(id);
    }

    public LiveData<List<TaskData>> getPendingTasks(long time) {
        return mTaskDao.getIncompleteTasksBeforeDate(time);
    }

    public LiveData<CountData> getTasksCounts(long timeMidNight) {
        return mTaskDao.getCountsInTasks(timeMidNight - 24 * 60 * 60 * 1000, timeMidNight);
    }
}
