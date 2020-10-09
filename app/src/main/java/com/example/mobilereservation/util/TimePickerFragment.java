package com.example.mobilereservation.util;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "TimePickerDialog";
    final Calendar c = Calendar.getInstance();
    private final EditText dateTime;

    public TimePickerFragment(EditText dateTime){
        this.dateTime = dateTime;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Set the current date as the default date
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar validateTime = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        String selectedTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(c.getTime());
        System.out.println("|TEST| Validet time" + selectedTime);
        Log.d(TAG, "onDateSet: " + selectedTime);
        dateTime.setText(dateTime.getText()+" "+selectedTime);
    }
}
