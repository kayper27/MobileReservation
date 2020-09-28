package com.example.mobilereservation.ui.facilities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.FragmentFacilityBinding;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.ApiService;
import com.example.mobilereservation.network.model.FacilityModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FacilityFragment extends Fragment {

    private FragmentFacilityBinding facilityBinding;
    private FacilitySearchAdapter adapterSearch;

    ArrayList<FacilityModel> facilityModel = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        facilityBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_facility, container, false);

        ApiService api = ApiClient.getClient(getContext().getApplicationContext()).create(ApiService.class);
        api.getfacilities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<FacilityModel>>() {
                    @Override
                    public void onSuccess(List<FacilityModel> facilities) {
                        for(int i = 0; i < facilities.size(); i++){
                            facilityModel.add(new FacilityModel(facilities.get(i).getFacility_id(),facilities.get(i).getType(),facilities.get(i).getStatus(), facilities.get(i).getDescription()));
                        }
                        adapterSearch = new FacilitySearchAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), facilityModel);
                        facilityBinding.facilityList.setAdapter(adapterSearch);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Error");
                        System.out.println("Test "+e);
                        System.out.println("Test "+e.getMessage());
                        alertDialog.setMessage(e.getMessage());
                        alertDialog.show();
                    }
                });
//        facilityModel.add(new FacilityModel("R00","Facility","Available", "This is data is hard coded 0"));
//        facilityModel.add(new FacilityModel("R01","Facility","Available", "This is data is hard coded 1"));
//        facilityModel.add(new FacilityModel("R02","Facility","Available", "This is data is hard coded 2"));
//        facilityModel.add(new FacilityModel("R03","Facility","Available", "This is data is hard coded 3"));
//        facilityModel.add(new FacilityModel("R04","Facility","Available", "This is data is hard coded 4"));
//        facilityModel.add(new FacilityModel("R05","Facility","Available", "This is data is hard coded 5"));



        facilityBinding.facilitySearch.setActivated(true);
        facilityBinding.facilitySearch.setQueryHint("Search Facility");
        facilityBinding.facilitySearch.onActionViewExpanded();
        facilityBinding.facilitySearch.setIconified(false);
        facilityBinding.facilitySearch.clearFocus();
        facilityBinding.facilitySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterSearch.getFilter().filter(newText);
                return false;
            }
        });
        return facilityBinding.getRoot();
    }
}