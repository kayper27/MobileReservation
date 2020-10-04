package com.example.mobilereservation.ui.facilities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.serachAdapter.FacilitySearchAdapter;
import com.example.mobilereservation.databinding.FragmentFacilityBinding;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.facility;
import com.example.mobilereservation.network.model.Facility;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FacilityFragment extends Fragment {

    private FragmentFacilityBinding facilityBinding;
    private FacilitySearchAdapter adapterSearch;

    ArrayList<Facility> facilitySet = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        facilityBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_facility, container, false);

        facility api = ApiClient.getClient(getContext().getApplicationContext()).create(facility.class);
        DisposableSingleObserver<List<Facility>> error = api.getFacilities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Facility>>() {
                    @Override
                    public void onSuccess(List<Facility> facilities) {
                        if(0 > facilities.size()){
                            return;
                        }

                        for (int i = 0; i < facilities.size(); i++) {
                            facilitySet.add(new Facility(facilities.get(i).getFacility_id(), facilities.get(i).getCategory(), facilities.get(i).getStatus(), facilities.get(i).getDescription()));
                        }
                        adapterSearch = new FacilitySearchAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), facilitySet);
                        facilityBinding.facilityList.setAdapter(adapterSearch);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage(e.getMessage());
                        alertDialog.show();
                    }
                });

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
                if(0 <  facilitySet.size()){
                    adapterSearch.getFilter().filter(newText);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Facility is empty", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return facilityBinding.getRoot();
    }
}