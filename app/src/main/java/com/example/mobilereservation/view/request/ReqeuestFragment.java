package com.example.mobilereservation.view.request;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.expandableList.MangmentExpandableListAdapter;
import com.example.mobilereservation.model.Equipment;
import com.example.mobilereservation.model.Request;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.request;
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

public class ReqeuestFragment extends Fragment {

    //// EXPANDABLE VARIABLES
    private ExpandableListView expandableListView;  // THE EXPANDABLE UI VARIABLE
    private ExpandableListAdapter expandableListAdapter; // ADAPTER FOR THE EXPANDABLE
    private List<String> expandableListTitle; // THE TITLE OF THE GROUP
    private HashMap<String, List<Request>> expandableListDetail; // DATA LIST OF THE GROUP

    private ArrayList<List<Request>> requestSeparated = new ArrayList<>();// HOLDS DATA DATA
    private List<Request> reqst = new ArrayList<>(); // A TEMP VARIABLE TO ALLOCATE SORTED VALUES

    public ReqeuestFragment() {}

    public static ReqeuestFragment newInstance() {
        return new ReqeuestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reqeuest, container, false);

        RequestAsyncTask asyncTask = new RequestAsyncTask();
        asyncTask.execute();

        expandableListView = root.findViewById(R.id.requestExpandableListView);

        return root;
    }

    private void saveSeparatedRequest(){
        requestSeparated.add(reqst);
        reqst = new ArrayList<>();
    }

    private class RequestAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            request api = ApiClient.getClient(getActivity().getApplicationContext()).create(request.class);
            DisposableSingleObserver<List<Request>> error = api.getUserRequest("2015105910")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Request>>() {
                        @Override
                        public void onSuccess(List<Request> requests) {
                            final HashMap<String, List<Request>> expandalbleList = new HashMap<>();

                            for(int i = 0; i < requests.size(); i++){
                                reqst.add(new Request(
                                        requests.get(i).getRequest_id(),
                                        requests.get(i).getStatus(),
                                        requests.get(i).getUsername(),
                                        requests.get(i).getStartAt(),
                                        requests.get(i).getEndAt(),
                                        requests.get(i).getFacility(),
                                        requests.get(i).getEquipment()
                                ));

                                if(i == requests.size()-1){
                                    saveSeparatedRequest();
                                }
                                else if(!requests.get(i).getStatus().equals(requests.get(i+1).getStatus())) {
                                    saveSeparatedRequest();
                                }
                            }
                            for(int i = 0; i < requestSeparated.size(); i++){
                                expandalbleList.put(requestSeparated.get(i).get(0).getStatus().toUpperCase(), requestSeparated.get(i));
                            }

                            HashMap arrangedRequest = new LinkedHashMap();
                            TreeMap<String, List<Request>> map = new TreeMap<>(expandalbleList);
                            Set set2 = map.entrySet();
                            Iterator iterator2 = set2.iterator();
                            while(iterator2.hasNext()) {
                                Map.Entry me2 = (Map.Entry)iterator2.next();
                                arrangedRequest.put(me2.getKey().toString(), (List<Equipment>)me2.getValue());
                            }

                            expandableListDetail = arrangedRequest;
                            expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                            expandableListAdapter = new MangmentExpandableListAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), expandableListTitle, expandableListDetail, false, true, false);
                            expandableListView.setAdapter(expandableListAdapter);
                            for(int i = 0; i < requestSeparated.size(); i++) {
                                if (requestSeparated.get(i).size() != 0) {
                                    expandableListView.expandGroup(i);
                                }
                            }
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
            progressDialog = ProgressDialog.show(getContext(), "Processing", "Fetching for your requests");
        }
    }

}