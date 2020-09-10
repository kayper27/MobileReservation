package com.example.mobilereservation.ui.facilities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FacilityViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public FacilityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Facility fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}