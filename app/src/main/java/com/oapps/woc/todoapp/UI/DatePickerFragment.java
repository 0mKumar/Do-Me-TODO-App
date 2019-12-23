package com.oapps.woc.todoapp.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    private Context context;
    private DatePickerDialog.OnDateSetListener listener;
    private Calendar c;

    public DatePickerFragment(Context context, Calendar calender, DatePickerDialog.OnDateSetListener listener) {
        this.context = context;
        this.listener = listener;
        this.c = calender;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        if (c == null) c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(context, listener, year, month, day);
    }
}