package com.example.mobilereservation.adapters.expandableList;

import android.content.Context;
import android.util.Log;

import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.equipment;
import com.example.mobilereservation.network.model.Equipment;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class EquipmentExpandableListDataPump {

    private static Context context;

    EquipmentExpandableListDataPump(Context context){
        this.context = context;
    }

    public static HashMap<String, List<Equipment>> getData() {
        final HashMap<String, List<Equipment>> expandableListDetail = new HashMap<String, List<Equipment>>();

        equipment api = ApiClient.getClient(context).create(equipment.class);
        DisposableSingleObserver<List<Equipment>> error = api.getEquipments()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Equipment>>() {
                    @Override
                    public void onSuccess(List<Equipment> equipments) {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(String.valueOf(context), "Error in fetching Equipment "+e.getMessage());
                    }
                });

        return expandableListDetail;
    }
}
