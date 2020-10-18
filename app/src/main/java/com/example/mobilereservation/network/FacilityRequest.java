package com.example.mobilereservation.network;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.example.mobilereservation.model.Facility;
import com.example.mobilereservation.network.apiService.facility;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FacilityRequest {

    private List<Facility> facilities;

    public FacilityRequest(final Context context, final FragmentManager fragmentManager){
        facility api = ApiClient.getClient(context).create(facility.class);
        DisposableSingleObserver<List<Facility>> error = api.getFacilities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Facility>>() {
                    @Override
                    public void onSuccess(List<Facility> facilities) {
                        if(0 > facilities.size()){
                            return;
                        }
                        setFacility(facilities);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", e.getMessage());
                        errorDialogFragment.show(fragmentManager, "dialog_equipment");
                    }
                });
    }

    private void setFacility(List<Facility> facilities){
        this.facilities = facilities;
    }

    public List<Facility> getFacility(){
        return this.facilities;
    }
}
