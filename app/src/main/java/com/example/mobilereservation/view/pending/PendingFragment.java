package com.example.mobilereservation.view.pending;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.SearchView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.expandableList.MangmentExpandableListAdapter;
import com.example.mobilereservation.databinding.FragmentPendingBinding;
import com.example.mobilereservation.model.Equipment;
import com.example.mobilereservation.model.Request;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.request;
import com.example.mobilereservation.util.FormatDateTime;
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

public class PendingFragment extends Fragment implements SearchView.OnQueryTextListener, SearchView.OnClickListener{

    private FragmentPendingBinding fragmentPendingBinding; // CALLS LAYOUT

    private FormatDateTime dateTime = new FormatDateTime(); // FOR FORMATTING DATE

    //// EXPANDABLE VARIABLES
    private ExpandableListAdapter expandableListAdapter; // ADAPTER FOR THE EXPANDABLE
    private List<String> expandableListTitle; // THE TITLE OF THE GROUP
    private HashMap<String, List<Request>> expandableListDetail; // DATA LIST OF THE GROUP

    // VARIABLE THAT HOLDS THE DATA
    private ArrayList<List<Request>> originalRequest = new ArrayList<>(); // HOLDS DATA DATA FULL DATA
    private ArrayList<List<Request>> filteredRequest = new ArrayList<>(); // HOLDS DATA DATA FILTERED DATA
    private List<Request> reqst = new ArrayList<>(); // A TEMP VARIABLE TO ALLOCATE SORTED VALUES

    public PendingFragment() {
        // Required empty public constructor
    }

    public static PendingFragment newInstance() {
        return new PendingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentPendingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pending, container, false);
        View root = fragmentPendingBinding.getRoot();

        RequestAsyncTask asyncTask = new RequestAsyncTask();
        asyncTask.execute();

        fragmentPendingBinding.pendingSearch.setActivated(true);
        fragmentPendingBinding.pendingSearch.setQueryHint("Search");
        fragmentPendingBinding.pendingSearch.onActionViewExpanded();
        fragmentPendingBinding.pendingSearch.setIconified(false);
        fragmentPendingBinding.pendingSearch.clearFocus();
        fragmentPendingBinding.pendingSearch.setOnQueryTextListener(this);
        fragmentPendingBinding.pendingSearch.setOnClickListener(this);
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

    private class RequestAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            request api = ApiClient.getClient(getActivity().getApplicationContext()).create(request.class);
            DisposableSingleObserver<List<Request>> error = api.getSpecificStatus("Pending")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Request>>() {
                        @Override
                        public void onSuccess(List<Request> requests) {

                            for(int i = 0; i < requests.size(); i++){
                                reqst.add(new Request(
                                        requests.get(i).getRequest_id(),
                                        requests.get(i).getStatus(),
                                        requests.get(i).getUsername(),
                                        dateTime.formatDateTime(requests.get(i).getStartAt()),
                                        dateTime.formatDateTime(requests.get(i).getEndAt()),
                                        requests.get(i).getFacility(),
                                        requests.get(i).getEquipment()));

                                if(i == requests.size()-1){
                                    saveSeparatedRequest();
                                }
                                else if(!requests.get(i).getStartAt().substring(0,10).equals(requests.get(i+1).getStartAt().substring(0,10))) { // Check if date is same as the next one the substring selects date only
                                    saveSeparatedRequest();
                                }
                            }
                            setValueExpandableList(originalRequest);
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
            progressDialog = ProgressDialog.show(getContext(), "Processing", "Fetching Schedules");
        }
    }
    private void saveSeparatedRequest(){
        originalRequest.add(reqst);
        reqst = new ArrayList<>();
    }

    private void filterRequest(String query){
        filteredRequest = new ArrayList<>();
        for(int i = 0; i < originalRequest.size(); i++){
            reqst = new ArrayList<>();
            for(int x = 0; x < originalRequest.get(i).size();x++){
                if(originalRequest.get(i).get(x).getRequest_id().toLowerCase().contains(query.toLowerCase()) ||
                   originalRequest.get(i).get(x).getUsername().toLowerCase().contains(query.toLowerCase()) ||
                   originalRequest.get(i).get(x).getStartAt().toLowerCase().contains(query.toLowerCase()) ||
                   originalRequest.get(i).get(x).getEndAt().toLowerCase().contains(query.toLowerCase())) {

                    reqst.add(new Request(
                            originalRequest.get(i).get(x).getRequest_id(),
                            originalRequest.get(i).get(x).getStatus(),
                            originalRequest.get(i).get(x).getUsername(),
                            dateTime.formatDateTime(originalRequest.get(i).get(x).getStartAt()),
                            dateTime.formatDateTime(originalRequest.get(i).get(x).getEndAt()),
                            originalRequest.get(i).get(x).getFacility(),
                            originalRequest.get(i).get(x).getEquipment()));
                    filteredRequest.add(reqst);
                }
            }
        }
        if(query.length() > 2){
            setValueExpandableList(filteredRequest);
        }
        else{
            setValueExpandableList(originalRequest);
        }
    }

    private void setValueExpandableList(ArrayList<List<Request>> expandableRequestData){ //
        final HashMap<String, List<Request>> expandalbleList = new HashMap<>();
        for(int i = 0; i < expandableRequestData.size(); i++){
            expandalbleList.put(expandableRequestData.get(i).get(0).getStartAt().substring(0,10).toUpperCase(), expandableRequestData.get(i));
        }

        HashMap arrangedRequest = new LinkedHashMap(); //Sort Alphabetically and numerically
        TreeMap<String, List<Request>> map = new TreeMap<>(expandalbleList);
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        while(iterator2.hasNext()) {
            Map.Entry me2 = (Map.Entry)iterator2.next();
            arrangedRequest.put(me2.getKey().toString(), (List<Equipment>)me2.getValue());
        }
        expandableListDetail = arrangedRequest;
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new MangmentExpandableListAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), expandableListTitle, expandableListDetail);
        fragmentPendingBinding.pendingExpandableListView.setAdapter(expandableListAdapter);
    }
}