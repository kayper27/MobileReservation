package com.example.mobilereservation.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "DatePickerFragment";

    final Calendar c = Calendar.getInstance();

    private DatePickerDialog datePickerDialog;
    private EditText dateTime;
    private String startData;

    public DatePickerFragment(EditText dateTime, String startData) {
        this.dateTime = dateTime;
        this.startData = startData;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Set the current date as the default date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        c.add(Calendar.DAY_OF_MONTH, 30); // adds 30 days advance reservation
        datePickerDialog = new DatePickerDialog(getActivity(), DatePickerFragment.this, year, month, day);

        if (!startData.isEmpty()) {
            datePickerDialog.getDatePicker().setMinDate(convertStartAt(startData).getTime() - 10000);// sets min date to startAt
        } else {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());// sets only day for today only
        }
        
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());// sets selection to 30 days advance reservation
        // Return a new instance of DatePickerDialog
        return datePickerDialog;
    }

    // called when a date has been selected
    public void onDateSet(DatePicker view, int year, int month, int day) {
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
//        boolean isSunday = c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY; // for disabling for sundays
        String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(c.getTime());
        dateTime.setText(selectedDate);
    }


    public Date convertStartAt(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = new Date();
        try {
            //formatting the dateString to convert it into a Date
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


}

