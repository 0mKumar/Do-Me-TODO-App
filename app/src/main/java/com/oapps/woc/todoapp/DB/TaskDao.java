package com.oapps.woc.todoapp.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(TaskData data);

    @Update
    void update(TaskData date);

    @Query("SELECT * FROM tasks_table WHERE due_date >= :todayStart AND due_date < :tonightEnd AND completed = 0 ORDER BY due_date ASC")
    LiveData<List<TaskData>> getIncompleteTasksBetweenDate(long todayStart, long tonightEnd);

    @Query("SELECT * FROM tasks_table WHERE due_date < :time AND completed = 0 ORDER BY due_date ASC")
    LiveData<List<TaskData>> getIncompleteTasksBeforeDate(long time);

    @Query("SELECT * FROM tasks_table WHERE task_id = :id")
    LiveData<TaskData> getTaskById(int id);

    @Query("SELECT * FROM tasks_table WHERE completed = 1 AND due_date > :after ORDER BY due_date DESC")
    LiveData<List<TaskData>> getCompletedTasksAfter(long after);

    @Query("SELECT * FROM tasks_table WHERE starred = 1")
    LiveData<List<TaskData>> getStarredTasks();

    @Query("SELECT * FROM tasks_table")
    LiveData<List<TaskData>> getAllTasks();

    // all tasks, today, pending, starred
    @Query("SELECT count(*) AS all_tasks, " +
            "sum(case when due_date >= :todayStart AND due_date < :todayMidNight AND completed = 0 then 1 else 0 end) AS tasks_today, " +
            "sum(case when due_date < :todayStart AND completed = 0 then 1 else 0 end) AS pending_tasks, " +
            "sum(case when starred = 1 then 1 else 0 end) AS starred_tasks FROM tasks_table")
    LiveData<CountData> getCountsInTasks(long todayStart, long todayMidNight);

    @Query("SELECT * FROM tasks_table WHERE task_id = :id")
    TaskData getTaskByIdAsync(int id);
}
