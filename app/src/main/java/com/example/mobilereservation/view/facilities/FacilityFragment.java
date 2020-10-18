package com.example.mobilereservation.view.facilities;

import android.os.AsyncTask;
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
import com.example.mobilereservation.model.Facility;
import com.example.mobilereservation.network.FacilityRequest;

import java.util.ArrayList;
import java.util.List;

public class FacilityFragment extends Fragment {

    private FragmentFacilityBinding facilityBinding;
    private FacilitySearchAdapter adapterSearch;
    private FacilityRequest facility, processedFacility;
    private ArrayList<Facility> facilitySet = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AsyncTaskRunner asyncTask = new AsyncTaskRunner();
        asyncTask.execute();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        facilityBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_facility, container, false);



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

    private class AsyncTaskRunner extends AsyncTask< Void, Void, List<Facility>> {

        @Override
        protected List<Facility> doInBackground(Void... voids) {
            facility = new FacilityRequest(getActivity().getApplicationContext(), getFragmentManager());
            return facility.getFacility();
        }

    }
}