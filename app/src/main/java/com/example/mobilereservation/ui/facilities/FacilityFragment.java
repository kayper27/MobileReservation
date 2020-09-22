package com.example.mobilereservation.ui.facilities;

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

import java.util.ArrayList;

public class FacilityFragment extends Fragment {

    private static final String URL = "http://192.168.85.194:3000/";
    private FragmentFacilityBinding facilityBinding;
    private FacilitySearchAdapter adapterSearch;

    ArrayList<FacilityModel> facilityModel = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        facilityBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_facility, container, false);

        facilityModel.add(new FacilityModel("R00","Facility","Available", "This is data is hard coded 0"));
        facilityModel.add(new FacilityModel("R01","Facility","Available", "This is data is hard coded 1"));
        facilityModel.add(new FacilityModel("R02","Facility","Available", "This is data is hard coded 2"));
        facilityModel.add(new FacilityModel("R03","Facility","Available", "This is data is hard coded 3"));
        facilityModel.add(new FacilityModel("R04","Facility","Available", "This is data is hard coded 4"));
        facilityModel.add(new FacilityModel("R05","Facility","Available", "This is data is hard coded 5"));

        adapterSearch = new FacilitySearchAdapter(getActivity().getApplicationContext(), facilityModel);
        facilityBinding.facilityList.setAdapter(adapterSearch);

        facilityBinding.facilitySearch.setActivated(true);
        facilityBinding.facilitySearch.setQueryHint("Ex. R100");
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