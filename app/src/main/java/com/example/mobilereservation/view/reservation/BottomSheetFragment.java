package com.example.mobilereservation.view.reservation;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private Button buttonOK;

    private static final String CATEGORY = "category";
    private static final String START = "start";
    private static final String END = "end";
    private String category = "", start = "", end = "";

    private FragmentBottomsSheetBinding fragmentBottomsSheetBinding;
    private FacilitySearchAdapter facilityAdapterSearch;
    private EquipmentSearchAdapter equipmentSearchAdapter;

    private List<Request> requestSet = new ArrayList<>();
    private ArrayList<Facility> filteredFacilities = new ArrayList<>();
    private ArrayList<Equipment> equipmentSet = new ArrayList<>();

    public BottomSheetFragment() {}

    public static BottomSheetFragment newInstance(String category, String start, String end) {
        BottomSheetFragment frag = new BottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        args.putString(START, start);
        args.putString(END, end);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(CATEGORY);
            start = getArguments().getString(START);
            end = getArguments().getString(START);
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
                if(validateFacility() && category.equals("facility")){
                    System.out.println("|TEST| valid "+getSelectFacility());
                }
                if(validateEquiopment() && category.equals("equipment")){
                    System.out.println("|TEST| valid "+getSelectedEquipments());
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
                else if(category.equals("equipment") && equipmentSet.size() > 0){
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
            request api = ApiClient.getClient(getActivity().getApplicationContext()).create(request.class);
            DisposableSingleObserver<List<Request>> error = api.getReservedchedule("2020-10-08T11:00:00.000Z", "2020-11-04T18:30:00.000Z") /// NOT CHANGED YET CHANGE
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
                            errorDialogFragment.show(getFragmentManager(), "dialog_error");
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
                            if(0 > facilities.size()){
                                return;
                            }
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
                                filteredFacilities.add(new Facility(facilities.get(i).getFacility_id(), facilities.get(i).getCategory(), facilities.get(i).getStatus(), facilities.get(i).getDescription(), false));
                            }

                            facilityAdapterSearch = new FacilitySearchAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), filteredFacilities, true);
                            fragmentBottomsSheetBinding.reservationList.setAdapter(facilityAdapterSearch);
                        }

                        @Override
                        public void onError(Throwable e) {
                            ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", e.getMessage());
                            errorDialogFragment.show(getFragmentManager(), "dialog_error");
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
                            final HashMap<String, List<Equipment>> expandalbleList = new HashMap<>();
                            if(0 > equipments.size()){
                                return;
                            }
                            for (int i = 0; i < equipments.size(); i++) {
                                equipmentSet.add(new Equipment(
                                        equipments.get(i).getEquipment_id(),
                                        equipments.get(i).getStatus(),
                                        equipments.get(i).getCategory(),
                                        equipments.get(i).getBrand(),
                                        equipments.get(i).getModel_no(),
                                        equipments.get(i).getType(),
                                        equipments.get(i).getDescription(),
                                        false)
                                );
                            }
                            equipmentSearchAdapter = new EquipmentSearchAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), equipmentSet, true);
                            fragmentBottomsSheetBinding.reservationList.setAdapter(equipmentSearchAdapter);
                        }
                        @Override
                        public void onError(Throwable e) {
                            ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", e.getMessage());
                            errorDialogFragment.show(getFragmentManager(), "dialog_error");
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
        for(int i = 0, ctr = 0; i < filteredFacilities.size(); i ++){
            if (filteredFacilities.get(i).getChecked().equals(true)) {
                ctr++;
                flag = true;
                if(ctr > 1){
                    flag = false;
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Invalid Input", "Please select 1 facility only ");
                    errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                    break;
                }
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
        for(int i = 0, ctr = 0; i < equipmentSet.size(); i ++){
            if (equipmentSet.get(i).getChecked().equals(true)) {
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
        for(int i = 0, ctr = 0; i < equipmentSet.size(); i ++){
            if (equipmentSet.get(i).getChecked().equals(true)) {
                selectedEquipment.add(equipmentSet.get(i));
            }
        }
        return selectedEquipment;
    }
}
