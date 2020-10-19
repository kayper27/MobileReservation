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

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.serachAdapter.FacilitySearchAdapter;
import com.example.mobilereservation.databinding.FragmentSlideupBinding;
import com.example.mobilereservation.model.Facility;
import com.example.mobilereservation.model.Request;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.request;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SlideupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlideupFragment extends Fragment {

    private FragmentSlideupBinding slideUpFragmentBinding;
    private FacilitySearchAdapter adapterSearch;

    private List<Request> requests;

    private Button buttonOK;

    private static final String CATEGORY = "category";
    private static final String START = "start";
    private static final String END = "end";
    private static String category = "", start = "", end = "";

    private ArrayList<Facility> facilitySet = new ArrayList<>();
    public SlideupFragment() {
        // Required empty public constructor
    }

    public static SlideupFragment newInstance(String category, String start, String end) {
        SlideupFragment fragment = new SlideupFragment();
        Bundle args = new Bundle();
//        args.putString(CATEGORY, category);
//        args.putString(START, start);
//        args.putString(END, end);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            category = getArguments().getString(CATEGORY);
//            start = getArguments().getString(START);
//            end = getArguments().getString(END);
//        }
//        if(category.equals("facility")){
//            System.out.println("|TEST| FACILITY");
//        }
//        else{
//            facilitySet = null;
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        slideUpFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_slideup, container,false);
        View childRoot = slideUpFragmentBinding.getRoot();

        buttonOK = (Button) childRoot.findViewById(R.id.reservation_ok);
        buttonOK.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservationAsyncTask asyncTask = new ReservationAsyncTask(start, end);
                asyncTask.execute();
            }
        }));

        slideUpFragmentBinding.reservationSearch.setActivated(false);
        slideUpFragmentBinding.reservationSearch.setQueryHint("Search");
        slideUpFragmentBinding.reservationSearch.onActionViewExpanded();
        slideUpFragmentBinding.reservationSearch.setIconified(false);
        slideUpFragmentBinding.reservationSearch.clearFocus();
        slideUpFragmentBinding.reservationSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(0 <  facilitySet.size()){ // CHANGE TO NOT SET TO facility arraylist
                    adapterSearch.getFilter().filter(newText);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        return  childRoot;
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
                            System.out.println("|TEST| "+requests.toString());
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
            progressDialog = ProgressDialog.show(getContext(), "Processing", "Fetching for Facilities");
        }
    }
}