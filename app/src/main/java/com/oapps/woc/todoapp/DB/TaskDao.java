package com.oapps.woc.todoapp.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(TaskData data);

    @Update
    void update(TaskData date);

    @Query("SELECT * FROM tasks_table WHERE due_date < :time AND completed = 0 ORDER BY due_date ASC")
    List<TaskData> getIncompleteTasksBeforeDate(long time);

    @Query("SELECT * FROM tasks_table WHERE task_id = :id")
    TaskData getTaskById(int id);

    @Query("SELECT * FROM tasks_table WHERE completed = 1 AND due_date > :after ORDER BY due_date DESC")
    List<TaskData> getCompletedTasksAfter(long after);
}
