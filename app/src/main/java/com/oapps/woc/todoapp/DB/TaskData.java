package com.oapps.woc.todoapp.DB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "tasks_table")
@TypeConverters(DateConverter.class)
public class TaskData {
    @PrimaryKey(autoGenerate = true)
    public int task_id;
    public String title;
    @ColumnInfo(name = "due_date")
    public Date dueDate;
    @ColumnInfo(name = "reminder")
    public Date reminderDate;
    boolean completed = false;
}
