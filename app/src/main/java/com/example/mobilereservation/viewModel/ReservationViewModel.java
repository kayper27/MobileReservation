package com.example.mobilereservation.viewModel;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.mobilereservation.BR;
import com.example.mobilereservation.model.Equips;
import com.example.mobilereservation.model.Request;

import java.util.Calendar;
import java.util.List;

public class ReservationViewModel extends BaseObservable {

    private Context context;
    private Request request;
    private Equips equipment;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private List<String> equipment_id;
    private List<String> equipment_status;

    @Bindable
    private String toastMessage = null;

    public ReservationViewModel(Context context){
        this.context = context;
        request = new Request(
                "",
                "",
                "",
                "",
                new Equips(equipment_id, equipment_status)
        );
    }

    public void setRequestStartAt(String requestStart) {
        request.setStartAt(requestStart);
        notifyPropertyChanged(BR.requestStartAt);
    }

    public void setRequestEndAt(String requestEnd) {
        request.setStartAt(requestEnd);
        notifyPropertyChanged(BR.requestEndAt);
    }

    @Bindable
    public String getRequestStartAt() {
        return request.getStartAt();
    }

    @Bindable
    public String getRequestEndAt() {
        return request.getEndAt();
    }


    public void setDateTime(){

        // Get Current Date
        final Calendar date = Calendar.getInstance();
        mYear = date.get(Calendar.YEAR);
        mMonth = date.get(Calendar.MONTH);
        mDay = date.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        setRequestStartAt(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        // Get Current Time
//        final Calendar time = Calendar.getInstance();
//        mHour = time.get(Calendar.HOUR_OF_DAY);
//        mMinute = time.get(Calendar.MINUTE);
//
//        // Launch Time Picker Dialog
//        TimePickerDialog timePickerDialog;
//        timePickerDialog = new TimePickerDialog(context,
//                new TimePickerDialog.OnTimeSetListener() {
//
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//
//                        txtTime.setText(hourOfDay + ":" + minute);
//                    }
//                }, mHour, mMinute, false);
//        timePickerDialog.show();
    }

    public void onClickeTest() {
        setToastMessage("Set Date");
    }

    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }
}