package com.example.mobilereservation.util;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.example.mobilereservation.view.dialog.ErrorDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "TimePickerDialog";

    final Calendar c = Calendar.getInstance();

    private EditText dateTime;
    private String startData;

    public TimePickerFragment(EditText dateTime, String startData) {
        this.dateTime = dateTime;
        this.startData = startData;
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
        boolean flag = true;

        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        String selectedTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(c.getTime());
        Log.d(TAG, "onDateSet: " + selectedTime);

        if(startData.length() != 16){ // START TIME SELECTION
            if(7 >= append(hour, minute) || append(hour, minute) >= 20.30) { // IF SELECTED VALUE IS GREATER THAN EQUAL 7:00 AM OR SELECTED VALUE GREATER THAN EQUAL 8:30 PM
                flag = false;
                ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Invalid input", "Time allowed is only between 7:00 AM - 8:30 PM");
                errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
            }
        }
        else { // END TIME SELECTION
            int convertHour = 0, convertMinute = 0;
            startData = startData.substring(11, 16);
            convertHour = Integer.parseInt(startData.substring(0, 2));
            convertMinute = Integer.parseInt(startData.substring(3, 5));
            if(append(convertHour, convertMinute) >= append(hour, minute)) {  // IF STARTDATA IS GREATER THAN EQUAL TO SELECTED DATA
                flag = false;
                ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Invalid input", "Time allowed is only between "+startData+" - 8:30 PM");
                errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
            }
        }

        if(flag){// ONLY ALLOW DATA IF TIME IS VALID
            dateTime.setText(dateTime.getText() + " " + selectedTime);
        }
    }

    public double append(int hour, int minute) {
        return Double.parseDouble(new StringBuilder().append(hour + ".").append(minute).toString());
    }
}
