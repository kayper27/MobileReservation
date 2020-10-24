package com.example.mobilereservation.view.schedule;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.expandableList.RequestExpandableListAdapter;
import com.example.mobilereservation.model.Equipment;
import com.example.mobilereservation.model.Request;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.request;
import com.example.mobilereservation.util.convertUtcToLocal;
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

public class ScheduleFragment extends Fragment {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<Request>> expandableListDetail;

    private ArrayList<List<Request>> requestSeparated = new ArrayList<>();
    private List<Request> reqst = new ArrayList<>();

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);

        RequestAsyncTask asyncTask = new RequestAsyncTask();
        asyncTask.execute();

        expandableListView = root.findViewById(R.id.scheduleExpandableListView);

        return root;
    }


    private class RequestAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            request api = ApiClient.getClient(getActivity().getApplicationContext()).create(request.class);
            DisposableSingleObserver<List<Request>> error = api.getSpecificStatus("Accepted")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<Request>>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(List<Request> requests) {
                            final HashMap<String, List<Request>> expandalbleList = new HashMap<>();
                            convertUtcToLocal dateTime = new convertUtcToLocal();
                            if(0 > requests.size()){
                                return;
                            }

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
                            for(int i = 0; i < requestSeparated.size(); i++){
                                expandalbleList.put(requestSeparated.get(i).get(0).getStartAt().substring(0,10).toUpperCase(), requestSeparated.get(i));
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
                            expandableListAdapter = new RequestExpandableListAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), expandableListTitle, expandableListDetail);
                            expandableListView.setAdapter(expandableListAdapter);
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
            progressDialog = ProgressDialog.show(getContext(), "Processing", "Fetching Schedules");
        }
    }
    private void saveSeparatedRequest(){
        requestSeparated.add(reqst);
        reqst = new ArrayList<>();
    }
}