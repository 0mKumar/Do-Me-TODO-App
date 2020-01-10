package com.oapps.woc.todoapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;

import com.oapps.woc.todoapp.DB.TaskData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
    static CharSequence getDateFormatted(Date now, Date date) {
        now = getCalenderDayForDate(now);
        date = getCalenderDayForDate(date);
        long diff = date.getTime() - now.getTime();
        Log.d("Utils", "" + diff);
        if (diff > -24 * 60 * 60 * 1000L && diff < 2 * 24 * 60 * 60 * 1000L) {
            return DateUtils.getRelativeTimeSpanString(date.getTime(), now.getTime(), DateUtils.DAY_IN_MILLIS);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());
            return sdf.format(date);
        }
    }

    static CharSequence getTimeFormatted(Date now, Date date) {
//        long diff = date.getTime() - now.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(date);
    }

    public static Date getCalenderDayForDate(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static void setOrUpdateAlarm(Context context, AlarmManager alarmManager, TaskData task) {
        if (task.reminderDate.before(Calendar.getInstance().getTime())) {
            return;
        }
        Intent intent = new Intent(context, MyEventAlarmReceiver.class);
        intent.putExtra("task_id", task.task_id);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, task.task_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(alarmIntent);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//                task.reminderDate.getTime(), 24 * 60 * 60 * 1000, alarmIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, task.reminderDate.getTime(), alarmIntent);
    }
}
