package com.oapps.woc.todoapp.DB;

import android.app.Application;

public class ToDoRepository {
    private TaskDao mTaskDao;

    ToDoRepository(Application application) {
        ToDoRoomDatabase db = ToDoRoomDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
    }

    void insertTask(TaskData task) {
        ToDoRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.insert(task);
        });
    }

    void updateTask(TaskData task){
        ToDoRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.update(task);
        });
    }
}
