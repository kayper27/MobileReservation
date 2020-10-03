package com.example.mobilereservation.ui.equipment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.expandableList.EquipmentExpandableListAdapter;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.equipment;
import com.example.mobilereservation.network.model.Equipment;

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

public class EquipmentFragment extends Fragment {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<Equipment>> expandableListDetail;

    private ArrayList<List<Equipment>> equipmentSeparated = new ArrayList<>();
    private List<Equipment> equips = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_equipment, container, false);
        expandableListView = root.findViewById(R.id.equipmentExpandableListView);

        equipment api = ApiClient.getClient(getActivity().getApplicationContext()).create(equipment.class);
        DisposableSingleObserver<List<Equipment>> error = api.getEquipments()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Equipment>>() {
                    @Override
                    public void onSuccess(List<Equipment> equipments) {
                        final HashMap<String, List<Equipment>> expandalbleList = new HashMap<>();
                        if(0 > equipments.size()){
                            return;
                        }

                        for(int i = 0; i < equipments.size(); i++){
                            equips.add(new Equipment(
                                    equipments.get(i).getEquipment_id(),
                                    equipments.get(i).getStatus(),
                                    equipments.get(i).getCategory(),
                                    equipments.get(i).getBrand(),
                                    equipments.get(i).getModel_no(),
                                    equipments.get(i).getType(),
                                    equipments.get(i).getDescription())
                            );
                            if(i == equipments.size()-1){
                                saveSeparatedEquipment();
                            }
                            else if(!equipments.get(i).getType().equals(equipments.get(i+1).getType())) {
                                saveSeparatedEquipment();
                            }
                        }
                        for(int i = 0; i < equipmentSeparated.size(); i++){
                            expandalbleList.put(equipmentSeparated.get(i).get(0).getType().toUpperCase(), equipmentSeparated.get(i));
                        }

                        HashMap arrangedEquipment = new LinkedHashMap();
                        TreeMap<String, List<Equipment>> map = new TreeMap<>(expandalbleList);
                        Set set2 = map.entrySet();
                        Iterator iterator2 = set2.iterator();
                        while(iterator2.hasNext()) {
                            Map.Entry me2 = (Map.Entry)iterator2.next();
                            arrangedEquipment.put(me2.getKey().toString(), (List<Equipment>)me2.getValue());
                        }

                        expandableListDetail = arrangedEquipment;
                        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                        expandableListAdapter = new EquipmentExpandableListAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), expandableListTitle, expandableListDetail);
                        expandableListView.setAdapter(expandableListAdapter);
                    }
                    @Override
                    public void onError(Throwable e) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage(e.getMessage());
                        alertDialog.show();
                        Log.d(String.valueOf(getActivity().getApplicationContext()), "Error in fetching Equipment "+e.getMessage());
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

    private void saveSeparatedEquipment(){
        equipmentSeparated.add(equips);
        equips = new ArrayList<>();
    }
}