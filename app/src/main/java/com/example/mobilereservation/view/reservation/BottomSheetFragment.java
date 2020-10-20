package com.example.mobilereservation.view.reservation;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.FragmentBottomsSheetBinding;
import com.example.mobilereservation.model.Request;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.request;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private static final String TAG = "BottomSheet";

    private Button buttonOK;

    private static final String CATEGORY = "category";
    private static final String START = "start";
    private static final String END = "end";
    private static String category = "", start = "", end = "";

    FragmentBottomsSheetBinding fragmentBottomsSheetBinding;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBottomsSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottoms_sheet, container, false);
        View root = fragmentBottomsSheetBinding.getRoot();

        buttonOK = (Button) root.findViewById(R.id.reservation_ok);
        buttonOK.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ReservationAsyncTask asyncTask = new ReservationAsyncTask(start, end);
//                asyncTask.execute();
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
                return false;
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
