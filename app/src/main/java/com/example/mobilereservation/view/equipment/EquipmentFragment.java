package com.example.mobilereservation.view.equipment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.expandableList.EquipmentExpandableListAdapter;
import com.example.mobilereservation.databinding.FragmentEquipmentBinding;
import com.example.mobilereservation.model.Equipment;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.equipment;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;

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

public class EquipmentFragment extends Fragment implements SearchView.OnQueryTextListener, SearchView.OnClickListener{

    private FragmentEquipmentBinding fragmentEquipmentBinding;

    //// EXPANDABLE VARIABLES
    private ExpandableListAdapter expandableListAdapter; // ADAPTER FOR THE EXPANDABLE
    private List<String> expandableListTitle; // THE TITLE OF THE GROUP
    private HashMap<String, List<Equipment>> expandableListDetail; // DATA LIST OF THE GROUP

    private ArrayList<List<Equipment>> originalEquipment = new ArrayList<>();// does not change all ways same value
    private ArrayList<List<Equipment>> filteredEquipment = new ArrayList<>();// has filtered value
    private List<Equipment> equips = new ArrayList<>();// A TEMP VARIABLE TO ALLOCATE SORTED VALUES

    public EquipmentFragment() {}

    public static EquipmentFragment newInstance() {
        return new EquipmentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentEquipmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_equipment, container, false);
        View root = fragmentEquipmentBinding.getRoot();

        EquipmentAsyncTask asyncTask = new EquipmentAsyncTask();
        asyncTask.execute();

        fragmentEquipmentBinding.equipmentSearch.setActivated(true);
        fragmentEquipmentBinding.equipmentSearch.setQueryHint("Search");
        fragmentEquipmentBinding.equipmentSearch.onActionViewExpanded();
        fragmentEquipmentBinding.equipmentSearch.setIconified(false);
        fragmentEquipmentBinding.equipmentSearch.clearFocus();
        fragmentEquipmentBinding.equipmentSearch.setOnQueryTextListener(this);
        fragmentEquipmentBinding.equipmentSearch.setOnClickListener(this);

        return root;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        filterRequest(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterRequest(newText);
        return false;
    }

    @Override
    public void onClick(View v) {

    }


    private class EquipmentAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            equipment api = ApiClient.getClient(getActivity().getApplicationContext()).create(equipment.class);
            DisposableSingleObserver<List<Equipment>> error = api.getEquipments()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Equipment>>() {
                        @Override
                        public void onSuccess(List<Equipment> equipments) {

                            for(int i = 0; i < equipments.size(); i++){
                                equips.add(new Equipment(
                                        equipments.get(i).getEquipment_id(),
                                        equipments.get(i).getStatus(),
                                        equipments.get(i).getCategory(),
                                        equipments.get(i).getBrand(),
                                        equipments.get(i).getModel_no(),
                                        equipments.get(i).getType(),
                                        equipments.get(i).getDescription(),
                                        null)
                                );
                                if(i == equipments.size()-1){
                                    saveSeparatedEquipment();
                                }
                                else if(!equipments.get(i).getType().equals(equipments.get(i+1).getType())) {
                                    saveSeparatedEquipment();
                                }
                            }
                            setValueExpandableList(originalEquipment);
                        }
                        @Override
                        public void onError(Throwable e) {
                            ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", e.getMessage());
                            errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(), "Processing", "Fetching for Equipments");
        }
    }

    private void saveSeparatedEquipment(){
        originalEquipment.add(equips);
        equips = new ArrayList<>();
    }

    private void filterRequest(String query){
        filteredEquipment = new ArrayList<>();
        for(int i = 0; i < originalEquipment.size(); i++){
            equips = new ArrayList<>();
            for(int x = 0; x < originalEquipment.get(i).size(); x++){
                if(originalEquipment.get(i).get(x).getType().toLowerCase().contains(query.toLowerCase()) || // originalEquipment get specific item in list then toLowerCase then check if contains
                   originalEquipment.get(i).get(x).getEquipment_id().toLowerCase().contains(query.toLowerCase())){
                    equips.add(new Equipment(
                            originalEquipment.get(i).get(x).getEquipment_id(),
                            originalEquipment.get(i).get(x).getStatus(),
                            originalEquipment.get(i).get(x).getCategory(),
                            originalEquipment.get(i).get(x).getBrand(),
                            originalEquipment.get(i).get(x).getModel_no(),
                            originalEquipment.get(i).get(x).getType(),
                            originalEquipment.get(i).get(x).getDescription(),
                            null));
                    filteredEquipment.add(equips);
                }
            }
        }
        if(query.length() > 2){
            setValueExpandableList(filteredEquipment);
        }
        else{
            setValueExpandableList(originalEquipment);
        }
    }

    private void setValueExpandableList(ArrayList<List<Equipment>> expandableRequestData) {
        final HashMap<String, List<Equipment>> expandalbleList = new HashMap<>();
        for(int i = 0; i < expandableRequestData.size(); i++){
            expandalbleList.put(expandableRequestData.get(i).get(0).getType().toUpperCase(), expandableRequestData.get(i));
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
        fragmentEquipmentBinding.equipmentExpandableListView.setAdapter(expandableListAdapter);
    }
}