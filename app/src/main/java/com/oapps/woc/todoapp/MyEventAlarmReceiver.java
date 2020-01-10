package com.oapps.woc.todoapp;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.oapps.woc.todoapp.DB.TaskDao;
import com.oapps.woc.todoapp.DB.TaskData;
import com.oapps.woc.todoapp.DB.ToDoRepository;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class MyEventAlarmReceiver extends BroadcastReceiver {
    String TAG = "MyEventAlarmReceiver";
    Application application;

    public MyEventAlarmReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Received intent");
        int taskID = intent.getIntExtra("task_id", -1);
        application = (Application) context.getApplicationContext();
        if (application != null && taskID >= 0) {
            ToDoRepository repository = new ToDoRepository(application);
            TaskData data = null;
            try {
                data = new getTaskByIdAsync(repository.mTaskDao).execute(taskID).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            if (data != null) {
                Log.d(TAG, "onReceive: got task data");
                Intent startTaskIntent = new Intent(context, TaskDetailsActivity.class);
                startTaskIntent.putExtra("task_id", taskID);
                startTaskIntent.putExtra("trial", "lets see");
                createNotification(context, startTaskIntent, data);
            }
        }
    }

    private static class getTaskByIdAsync extends AsyncTask<Integer, Void, TaskData> {
        TaskDao taskDao;

        public getTaskByIdAsync(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected TaskData doInBackground(Integer... ids) {
            return taskDao.getTaskByIdAsync(ids[0]);
        }

        @Override
        protected void onPostExecute(TaskData students) {
            super.onPostExecute(students);
        }
    }

    public void createNotification(Context context, Intent intent, TaskData task) {
        Log.d(TAG, "createNotification: creating notification");
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) {
            Toast.makeText(context, "Notification Manager unresolved, Quitting Notification", Toast.LENGTH_LONG).show();
            return;
        }
        PendingIntent p = PendingIntent.getActivity(context, task.task_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String channel_id = "do_me_" + task.task_id;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), channel_id);
//        builder.setTicker(ticker);
        builder.setContentTitle("Reminder");

        builder.setContentText("Your task " + task.title + " is due " + Utils.getDateFormatted(Calendar.getInstance().getTime(), task.dueDate) + ".");

        builder.setSmallIcon(R.drawable.ic_notifications_24dp);
        builder.setContentIntent(p);
        Notification n = builder.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channel_id,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH
            );
            nm.createNotificationChannel(channel);
            builder.setChannelId(channel_id);
        }
        nm.notify(task.task_id, n);
    }
}
