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
        
        if(7 > append(hour, minute) || append(hour, minute) > 20.30) { // IF SELECTED VALUE IS GREATER THAN EQUAL 7:00 AM OR SELECTED VALUE GREATER THAN EQUAL 8:30 PM
            flag = false;
            ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Invalid input", "Time allowed is only between 7:00 AM - 8:30 PM");
            errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
        }
        if(startData.length() == 16){//END TIME SELECTION
            int hourStartData = Integer.parseInt(startData.substring(11, 13));
            int minuteStartData = Integer.parseInt(startData.substring(14, 16));
            int monthStartData = Integer.parseInt(startData.substring(5,7));
            int dayStartData = Integer.parseInt(startData.substring(8,10));
            if(!dateTime.getText().toString().isEmpty()){//IF DATE IS EMPTY THEN DO NOTHING
                int monthEndData = Integer.parseInt(dateTime.getText().toString().substring(5,7));
                int dayEndData = Integer.parseInt(dateTime.getText().toString().substring(8,10));
                // IF the date is the same then you cannot select time beyond time selected after start
                if(append(hourStartData, minuteStartData) >= append(hour, minute) && (monthEndData == monthStartData) && (dayEndData == dayStartData)){
                    flag = false;
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Invalid input", "Time allowed is only between "+startData+"- 8:30 PM");
                    errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                }
            }
        }

        if(flag){// ONLY ALLOW DATA IF TIME IS VALID
            dateTime.setText(dateTime.getText() + " " + selectedTime);
        }
    }

    public double append(int hour, int minute) {
        String fromat = "";
        if(minute < 10){
            fromat = "0";
        }
        return Double.parseDouble(new StringBuilder().append(hour + ".").append(fromat+minute).toString());
    }
}
