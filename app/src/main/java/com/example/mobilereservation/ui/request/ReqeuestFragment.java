package com.example.mobilereservation.ui.request;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.expandableList.RequestExpandableListAdapter;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.request;
import com.example.mobilereservation.network.model.Equipment;
import com.example.mobilereservation.network.model.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ReqeuestFragment extends Fragment {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<Request>> expandableListDetail;

    private ArrayList<List<Request>> requestSeparated = new ArrayList<>();
    private List<Request> reqst = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reqeuest, container, false);

        expandableListView = root.findViewById(R.id.requestExpandableListView);
        request api = ApiClient.getClient(getActivity().getApplicationContext()).create(request.class);
        DisposableSingleObserver<List<Request>> error = api.getUserRequest("2015105910")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Request>>() {
                    @Override
                    public void onSuccess(List<Request> requests) {
                        final HashMap<String, List<Request>> expandalbleList = new HashMap<>();

//                        System.out.println("|Test| "+ requests.get(0).getEquipments()[0][0]);

                        for(int i = 0; i < requests.size(); i++){
                            reqst.add(new Request(
                                    requests.get(i).getRequest_id(),
                                    requests.get(i).getStatus(),
                                    requests.get(i).getUsername(),
                                    requests.get(i).getStartAt(),
                                    requests.get(i).getEndAt(),
                                    requests.get(i).getFacility(),
                                    requests.get(i).getEquipments()));

                            if(i == requests.size()-1){
                                saveSeparatedRequest();
                            }
                            else if(!requests.get(i).getStatus().equals(requests.get(i+1).getStatus())) {
                                saveSeparatedRequest();
                            }
                        }
                        for(int i = 0; i < requestSeparated.size(); i++){
                            expandalbleList.put(requestSeparated.get(i).get(0).getStatus().toUpperCase(), requestSeparated.get(i));
                        }

                        HashMap arrangedRequest = new LinkedHashMap();
                        TreeMap<String, List<Request>> map = new TreeMap<>(expandalbleList);
                        Set set2 = map.entrySet();
                        Iterator iterator2 = set2.iterator();
                        while(iterator2.hasNext()) {
                            Map.Entry me2 = (Map.Entry)iterator2.next();
                            arrangedRequest.put(me2.getKey().toString(), (List<Equipment>)me2.getValue());
                        }

                        expandableListDetail = arrangedRequest;
                        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                        expandableListAdapter = new RequestExpandableListAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), expandableListTitle, expandableListDetail);
                        expandableListView.setAdapter(expandableListAdapter);
                        expandableListView.expandGroup(0);
                        expandableListView.expandGroup(1);
                    }
                    @Override
                    public void onError(Throwable e) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Error");
                        System.out.println("|Test| "+ e);
                        System.out.println("|Test| "+ e.getMessage());
                        alertDialog.setMessage(e.getMessage());
                        alertDialog.show();
                        Log.d(String.valueOf(getActivity().getApplicationContext()), "Error in fetching Request "+e.getMessage());
                    }
                });



        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getActivity().getApplicationContext(), expandableListTitle.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getActivity().getApplicationContext(), expandableListTitle.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        return root;
    }

    private void saveSeparatedRequest(){
        requestSeparated.add(reqst);
        reqst = new ArrayList<>();
    }
}