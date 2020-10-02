package com.example.mobilereservation.adapters.expandableList;

import android.content.Context;

import com.example.mobilereservation.network.model.Equipment;

import java.util.HashMap;
import java.util.List;

public class EquipmentExpandableListDataPump {

    private static Context context;

    EquipmentExpandableListDataPump(Context context){
        this.context = context;
    }

    public static HashMap<String, List<Equipment>> getData() {
        final HashMap<String, List<Equipment>> expandableListDetail = new HashMap<String, List<Equipment>>();



        return expandableListDetail;
    }
}
