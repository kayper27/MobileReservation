package com.example.mobilereservation.adapters.expandableList;

import com.example.mobilereservation.network.model.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestExpandableListDataPump {
    public static HashMap<String, List<Request>> getData() {

        HashMap<String, List<Request>> expandableListDetail = new HashMap<String, List<Request>>();
        String[] equipmentPending =  {"Equip01"};
        ArrayList<Request> pending = new ArrayList<>();
        pending.add(new Request("ID001","Pending", "0000000001", "2020-09-02T12:00:00.000Z", "2020-09-02T14:00:00.000Z", "R000", equipmentPending));

        String[] equpmentAccpeted =  {"Equip00"};
        ArrayList<Request> accepted = new ArrayList<>();
        accepted.add(new Request("ID000","Accepted", "0000000000", "2020-09-02T12:00:00.000Z", "2020-09-02T14:00:00.000Z", "R001", equpmentAccpeted));

        expandableListDetail.put("PENDING", pending);
        expandableListDetail.put("ACCEPTED", accepted);
        return expandableListDetail;
    }
}
