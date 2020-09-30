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

//        ArrayList<Equipment> projector = new ArrayList<>();
//        projector.add(new Equipment("projector0","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 0"));
//        projector.add(new Equipment("projector1","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 1"));
//        projector.add(new Equipment("projector2","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 2"));
//        projector.add(new Equipment("projector3","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 3"));
//        projector.add(new Equipment("projector4","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 4"));
//
//        ArrayList<Equipment> computer = new ArrayList<>();
//        computer.add(new Equipment("computer0","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 0"));
//        computer.add(new Equipment("computer1","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 1"));
//        computer.add(new Equipment("computer2","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 2"));
//        computer.add(new Equipment("computer3","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 3"));
//        computer.add(new Equipment("computer4","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 4"));
//
//        ArrayList<Equipment> mouse = new ArrayList<>();
//        mouse.add(new Equipment("mouse0","Equipment", "Available", "Logitech", "x0000", "Projector", "This is mouse data hard data 0"));
//        mouse.add(new Equipment("mouse1","Equipment", "Available", "Logitech", "x0000", "Projector", "This is mouse data hard data 1"));
//        mouse.add(new Equipment("mouse2","Equipment", "Available", "Logitech", "x0000", "Projector", "This is mouse data hard data 2"));
//        mouse.add(new Equipment("mouse3","Equipment", "Available", "Logitech", "x0000", "Projector", "This is mouse data hard data 3"));
//        mouse.add(new Equipment("mouse4","Equipment", "Available", "Logitech", "x0000", "Projector", "This is mouse data hard data 4"));
//
//        expandableListDetail.put("PROJECTORS", projector);
//        expandableListDetail.put("COMPUTERS", computer);
//        expandableListDetail.put("MOUSES", mouse);
        return expandableListDetail;
    }
}
