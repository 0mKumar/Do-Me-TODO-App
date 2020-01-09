package com.oapps.woc.todoapp;

import android.text.format.DateUtils;
import android.util.Log;

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
            Log.d("Utils", "Printing date relative");
            return DateUtils.getRelativeTimeSpanString(date.getTime(), now.getTime(), DateUtils.DAY_IN_MILLIS);
        } else {
            Log.d("Utils", "Printing date absolute");
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());
            return sdf.format(date);
        }
    }

    static Date getCalenderDayForDate(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
}
