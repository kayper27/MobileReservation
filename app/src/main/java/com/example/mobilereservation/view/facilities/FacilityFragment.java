package com.example.mobilereservation.view.facilities;

import android.app.ProgressDialog;
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
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.facility;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class FacilityFragment extends Fragment {

    private FragmentFacilityBinding facilityBinding; // CALLS LAYOUT
    private FacilitySearchAdapter adapterSearch; // ADAPTER FOR SEARCH DATA FOR LIST
    private ArrayList<Facility> facilitySet = new ArrayList<>(); // HOLDS DATA FOR LIST

    public FacilityFragment() {}

    public static FacilityFragment newInstance() {
        return new FacilityFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        facilityBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_facility, container, false);

        FacilityAsyncTask asyncTask = new FacilityAsyncTask();
        asyncTask.execute();

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

    private class FacilityAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            facility api = ApiClient.getClient(getActivity().getApplicationContext()).create(facility.class);
            DisposableSingleObserver<List<Facility>> error = api.getFacilities()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Facility>>() {
                        @Override
                        public void onSuccess(List<Facility> facilities) {

                            for (int i = 0; i < facilities.size(); i++) {
                                facilitySet.add(new Facility(facilities.get(i).getFacility_id(), facilities.get(i).getCategory(), facilities.get(i).getStatus(), facilities.get(i).getDescription(), null));
                            }
                            adapterSearch = new FacilitySearchAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), facilitySet, false);
                            facilityBinding.facilityList.setAdapter(adapterSearch);
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
            progressDialog = ProgressDialog.show(getContext(), "Processing", "Fetching for Facilities");
        }
    }
}