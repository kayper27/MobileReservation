package com.example.mobilereservation.ui.equipment;

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
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class EquipmentFragment extends Fragment {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<Equipment>> expandableListDetail;
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

                        ArrayList<List<Equipment>> equipmentSort = new ArrayList<>();
                        List<Equipment> equips = new ArrayList<>();

                        equips.add(new Equipment("projector0","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 0"));
                        equips.add(new Equipment("projector1","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 1"));
                        equips.add(new Equipment("projector2","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 2"));
                        equips.add(new Equipment("projector3","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 3"));
                        equips.add(new Equipment("projector4","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 4"));
                        equipmentSort.add(equips);
                        equips = new ArrayList<>();
                        equips.add(new Equipment("computer0","Equipment", "Available", "MSI", "x0000", "Computer", "This is computer data hard data 0"));
                        equips.add(new Equipment("computer1","Equipment", "Available", "MSI", "x0000", "Computer", "This is computer data hard data 1"));
                        equips.add(new Equipment("computer2","Equipment", "Available", "MSI", "x0000", "Computer", "This is computer data hard data 2"));
                        equips.add(new Equipment("computer3","Equipment", "Available", "MSI", "x0000", "Computer", "This is computer data hard data 3"));
                        equips.add(new Equipment("computer4","Equipment", "Available", "MSI", "x0000", "Computer", "This is computer data hard data 4"));
                        equipmentSort.add(equips);

                        System.out.println("TEST "+equipmentSort.size());
                        System.out.println("TEST "+equipmentSort.get(0).size());
                        System.out.println("TEST "+equipmentSort.get(1).size());
                        for(int i = 0; i < equipmentSort.size(); i++){
                            expandalbleList.put(equipmentSort.get(i).get(0).getType().toUpperCase(), equipmentSort.get(i));
                            for(int x = 0; x < equipmentSort.get(i).size(); x++){
                                System.out.println("TEST Data |"+ i +"| "+ x +" "+equipmentSort.get(i).get(x).getType());
                            }
                        }


                        System.out.println("TEST END");

                        expandableListDetail =  expandalbleList;
                        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                        expandableListAdapter = new EquipmentExpandableListAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), expandableListTitle, expandableListDetail);
                        expandableListView.setAdapter(expandableListAdapter);
                    }
                    @Override
                    public void onError(Throwable e) {
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
}