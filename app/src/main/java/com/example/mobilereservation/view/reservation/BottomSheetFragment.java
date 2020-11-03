package com.example.mobilereservation.view.reservation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.serachAdapter.EquipmentSearchAdapter;
import com.example.mobilereservation.adapters.serachAdapter.FacilitySearchAdapter;
import com.example.mobilereservation.databinding.FragmentBottomsSheetBinding;
import com.example.mobilereservation.model.Equipment;
import com.example.mobilereservation.model.Facility;
import com.example.mobilereservation.model.Request;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.equipment;
import com.example.mobilereservation.network.apiService.facility;
import com.example.mobilereservation.network.apiService.request;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private Button buttonOK;

    private static final String CATEGORY = "category"; // WHAT KEY TO KNOW WHAT CATEGORY TO WORK
    private static final String START = "start"; // TO GET DATA BETWEEN DATES
    private static final String END = "end";
    private static final String SELECTED = "selected";
    private String category = "", start = "", end = "";
    private String[] selected;

    private FragmentBottomsSheetBinding fragmentBottomsSheetBinding; // CALL LAYOUT
    private FacilitySearchAdapter facilityAdapterSearch; // ADAPTER FOR SEARCH FACILITY IN LIST
    private EquipmentSearchAdapter equipmentSearchAdapter; // ADAPTER FOR SEARCH EQUIPMENT IN LIST

    private List<Request> requestSet = new ArrayList<>();
    private ArrayList<Facility> filteredFacilities = new ArrayList<>(); // VARIABLE THAT HOLDS FILTERED LIST DATA
    private ArrayList<Equipment> filteredEquipments = new ArrayList<>(); // VARIABLE THAT HOLDS FILTERED LIST DATA

    public BottomSheetFragment() {}

    public static BottomSheetFragment newInstance(String category, String[] selected, String start, String end) {
        BottomSheetFragment frag = new BottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        args.putString(START, start);
        args.putString(END, end);
        args.putStringArray(SELECTED, selected);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(CATEGORY);
            start = getArguments().getString(START);
            end = getArguments().getString(END);
            selected = getArguments().getStringArray(SELECTED);
        }
        ReservationAsyncTask reservationAsyncTask = new ReservationAsyncTask(start, end);
        reservationAsyncTask.execute();

        if(category.equals("facility")){
            FacilityAsyncTask facilityAsyncTask = new FacilityAsyncTask();
            facilityAsyncTask.execute();
        }
        else{
            EquipmentAsyncTask equipmentAsyncTask = new EquipmentAsyncTask();
            equipmentAsyncTask.execute();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBottomsSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottoms_sheet, container, false);
        View root = fragmentBottomsSheetBinding.getRoot();

        buttonOK = (Button) root.findViewById(R.id.reservation_ok);
        buttonOK.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sender", "Broadcasting message");
                if(validateFacility() && category.equals("facility")){
                    Intent intent = new Intent("send-facility-data");
                    intent.putExtra("facility", getSelectFacility());
                    LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
                }
                if(validateEquiopment() && category.equals("equipment")){
                    Intent intent = new Intent("send-equipment-data");
                    intent.putExtra("equipment", (Serializable) getSelectedEquipments());
                    LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
                }
            }
        }));

        fragmentBottomsSheetBinding.reservationSearch.setActivated(false);
        fragmentBottomsSheetBinding.reservationSearch.setQueryHint("Search");
        fragmentBottomsSheetBinding.reservationSearch.onActionViewExpanded();
        fragmentBottomsSheetBinding.reservationSearch.setIconified(false);
        fragmentBottomsSheetBinding.reservationSearch.clearFocus();
        fragmentBottomsSheetBinding.reservationSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(category.equals("facility") && filteredFacilities.size() > 0){
                    facilityAdapterSearch.getFilter().filter(newText);
                }
                else if(category.equals("equipment") && filteredEquipments.size() > 0){
                    equipmentSearchAdapter.getFilter().filter(newText);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Search is empty", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int height = (int) (getResources().getDisplayMetrics().heightPixels * .85);//85% of screen height
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    private class ReservationAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        private String start, end;

        ReservationAsyncTask(String start, String end){
            this.start = start;
            this.end = end;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            start = start.replace(" ","T").concat(":00.000Z");// Format start for mongodb
            end = end.replace(" ","T").concat(":00.000Z");;// Format end for mongodb
            request api = ApiClient.getClient(getActivity().getApplicationContext()).create(request.class);
            // "yyyy-MM-ddT00:00:00.000Z" must look like this format MONGODB does not follow any format but this
            DisposableSingleObserver<List<Request>> error = api.getReservedchedule(start, end)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Request>>() {
                        @Override
                        public void onSuccess(List<Request> requests) {
                            requestSet = requests;
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
            progressDialog = ProgressDialog.show(getContext(), "Processing", "Validating Request");
        }
    }


    private class FacilityAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            facility api = ApiClient.getClient(getActivity().getApplicationContext()).create(facility.class);
            DisposableSingleObserver<List<Facility>> error = api.getAvailableFacilities()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Facility>>() {
                        @Override
                        public void onSuccess(List<Facility> facilities) {

                            List<Facility> facilityTemp = new ArrayList<>();
                            for(int i = 0; i < requestSet.size(); i++){
                                for (int x = 0; x < facilities.size(); x++) {
                                    if(!requestSet.get(i).getFacility().equals(facilities.get(x).getFacility_id())){
                                        facilityTemp.add(new Facility(facilities.get(x).getFacility_id(), facilities.get(x).getCategory(), facilities.get(x).getStatus(), facilities.get(x).getDescription(), false));
                                    }
                                }
                                facilities = facilityTemp;
                                facilityTemp = new ArrayList<>();
                            }
                            for (int i = 0; i < facilities.size(); i++) {
                                boolean checked = false;
                                if(selected != null && facilities.get(i).getFacility_id().equals(selected[0])){
                                    checked = true;
                                }
                                filteredFacilities.add(new Facility(facilities.get(i).getFacility_id(), facilities.get(i).getCategory(), facilities.get(i).getStatus(), facilities.get(i).getDescription(), checked));
                            }

                            facilityAdapterSearch = new FacilitySearchAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), filteredFacilities, true);
                            fragmentBottomsSheetBinding.reservationList.setAdapter(facilityAdapterSearch);
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
            progressDialog = ProgressDialog.show(getContext(), "Processing", "Fetching Facilities");
        }
    }

    private class EquipmentAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            equipment api = ApiClient.getClient(getActivity().getApplicationContext()).create(equipment.class);
            DisposableSingleObserver<List<Equipment>> error = api.getAvailableEquipments()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Equipment>>() {
                        @Override
                        public void onSuccess(List<Equipment> equipments) {
                            ArrayList<Equipment> equipmentTemp = new ArrayList<>();

                            for(int i = 0; i < requestSet.size(); i++){ // Get size of request
                                for(int y = 0; y < requestSet.get(i).getEquipment().getEquipment_id().size(); y++){ // Get size of Equipment array of request
                                    for(int x = 0; x < equipments.size(); x++){  // Filter equipment
                                        if(!requestSet.get(i).getEquipment().getEquipment_id().get(y).equals(equipments.get(x).getEquipment_id())){
                                            equipmentTemp.add(new Equipment(
                                                    equipments.get(x).getEquipment_id(),
                                                    equipments.get(x).getStatus(),
                                                    equipments.get(x).getCategory(),
                                                    equipments.get(x).getBrand(),
                                                    equipments.get(x).getModel_no(),
                                                    equipments.get(x).getType(),
                                                    equipments.get(x).getDescription(),
                                                    false)
                                            );
                                        }
                                    }
                                    equipments = equipmentTemp;
                                    equipmentTemp = new ArrayList<>();
                                }
                            }

                            for (int i = 0; i < equipments.size(); i++) {
                                boolean checked = false;
                                if(selected != null){
                                    for(int x = 0; x < selected.length; x++){
                                        checked = false;
                                        if(equipments.get(i).getEquipment_id().equals(selected[x])){
                                            checked = true;
                                            break;
                                        }
                                    }
                                }
                                filteredEquipments.add(new Equipment(
                                        equipments.get(i).getEquipment_id(),
                                        equipments.get(i).getStatus(),
                                        equipments.get(i).getCategory(),
                                        equipments.get(i).getBrand(),
                                        equipments.get(i).getModel_no(),
                                        equipments.get(i).getType(),
                                        equipments.get(i).getDescription(),
                                        checked)
                                );
                            }
                            equipmentSearchAdapter = new EquipmentSearchAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), filteredEquipments, true);
                            fragmentBottomsSheetBinding.reservationList.setAdapter(equipmentSearchAdapter);
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
            progressDialog = ProgressDialog.show(getContext(), "Processing", "Fetching Equipments");
        }
    }

    public boolean validateFacility(){
        boolean flag = false;
        for(int i = 0, ctr = 0; i < filteredFacilities.size(); i ++){// ALLOWS NO SELECTED FACILITY
            if(ctr == 0){
                flag = true;
            }
            if (filteredFacilities.get(i).getChecked().equals(true)) {// ALLOWS ONE SELECTED FACILITY
                ctr++;
                flag = true;
            }
            if(ctr > 1){// FLAGS MORE THAN ONE SELECTED FACILITY
                flag = false;
                ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Invalid Input", "Please select 1 facility only ");
                errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                break;
            }
        }
        return flag;
    }

    public String getSelectFacility(){
        String facilityID = "";
        for(int i = 0, ctr = 0; i < filteredFacilities.size(); i ++){
            if (filteredFacilities.get(i).getChecked().equals(true)) {
                facilityID = filteredFacilities.get(i).getFacility_id();
            }
        }
        return facilityID;
    }

    public boolean validateEquiopment(){
        boolean flag = false;
        for(int i = 0, ctr = 0; i < filteredEquipments.size(); i ++){
            if (filteredEquipments.get(i).getChecked().equals(true)) {
                ctr++;
                flag = true;
                if(ctr > 5){
                    flag = false;
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Invalid Input", "Please select 5 equipments only ");
                    errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                    break;
                }
            }
        }
        return flag;
    }

    public List<Equipment> getSelectedEquipments(){
        List<Equipment> selectedEquipment = new ArrayList<>();
        for(int i = 0, ctr = 0; i < filteredEquipments.size(); i ++){
            if (filteredEquipments.get(i).getChecked().equals(true)) {
                selectedEquipment.add(filteredEquipments.get(i));
            }
        }
        return selectedEquipment;
    }
}
