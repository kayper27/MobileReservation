package com.example.mobilereservation.ui.request;

import com.example.mobilereservation.network.model.RequestModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestExpandableListDataPump {
    public static HashMap<String, List<RequestModel>> getData() {

        HashMap<String, List<RequestModel>> expandableListDetail = new HashMap<String, List<RequestModel>>();
        String[] equipmentPending =  {"Equip01"};
        ArrayList<RequestModel> pending = new ArrayList<>();
        pending.add(new RequestModel("ID001","Pending", "0000000001", "2020-09-02T12:00:00.000Z", "2020-09-02T14:00:00.000Z", "R000", equipmentPending));

        String[] equpmentAccpeted =  {"Equip00"};
        ArrayList<RequestModel> accepted = new ArrayList<>();
        accepted.add(new RequestModel("ID000","Accepted", "0000000000", "2020-09-02T12:00:00.000Z", "2020-09-02T14:00:00.000Z", "R001", equpmentAccpeted));

        expandableListDetail.put("PENDING", pending);
        expandableListDetail.put("ACCEPTED", accepted);
        return expandableListDetail;
    }
}
