package com.example.mobilereservation.viewModel;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.mobilereservation.BR;
import com.example.mobilereservation.model.Equips;
import com.example.mobilereservation.model.Request;

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

    public String getToastMessage() {
        return toastMessage;
    }

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

    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }
}