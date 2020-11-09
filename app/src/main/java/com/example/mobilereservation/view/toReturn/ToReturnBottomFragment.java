package com.example.mobilereservation.view.toReturn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.databinding.DataBindingUtil;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.listAdapter.ToReturnEquipmentListAdapter;
import com.example.mobilereservation.databinding.FragmentToReturnBottomBinding;
import com.example.mobilereservation.model.Request;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.request;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;
import com.example.mobilereservation.view.dialog.RequestDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ToReturnBottomFragment extends BottomSheetDialogFragment {

    private static final String REQUEST = "request";
    private Request request;

    private FragmentToReturnBottomBinding fragmentToReturnBottomBinding;

    private FrameLayout bottomSheet;
    private BottomSheetDialog dialog;
    private BottomSheetBehavior behavior;

    public ToReturnBottomFragment() {
        // Required empty public constructor
    }

    public static ToReturnBottomFragment newInstance(Request request) {
        ToReturnBottomFragment fragment = new ToReturnBottomFragment();
        Bundle args = new Bundle();
        args.putSerializable(REQUEST, request);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.request = (Request) getArguments().getSerializable(REQUEST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentToReturnBottomBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_to_return_bottom, container, false);
        View root = fragmentToReturnBottomBinding.getRoot();

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dialog = (BottomSheetDialog) ToReturnBottomFragment.this.getDialog();
                if (dialog != null) {
                    bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
                    if (bottomSheet != null) {
                        behavior = BottomSheetBehavior.from(bottomSheet);
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        behavior.setSkipCollapsed(true);
                        behavior.setHideable(false);
                    }
                }
            }
        });


        fragmentToReturnBottomBinding.toReturnRequestId.setText(request.getRequest_id());

        ToReturnEquipmentListAdapter toReturnEquipmentListAdapter = new ToReturnEquipmentListAdapter( request.getEquipment(), getActivity().getApplicationContext());
        fragmentToReturnBottomBinding.toReturnList.setAdapter(toReturnEquipmentListAdapter);

        fragmentToReturnBottomBinding.toReturnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;

                for(int i = 0; i < request.getEquipment().getEquipment_Status().size(); i++){
                    if(request.getEquipment().getEquipment_Status().get(i).equals("Pending")){
                        flag = false;
                        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Invalid", "Please select status for equipment");
                        errorDialogFragment.show(getActivity().getSupportFragmentManager(), "error_dialog");
                        break;
                    }
                }
                if(flag){
                    RequestStatusAsyncTask asyncTask = new RequestStatusAsyncTask("2015105910", request.getRequest_id(), request);
                    asyncTask.execute();
                }
            }
        });
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(request != null){
            for(int i = 0; i < request.getEquipment().getEquipment_Status().size(); i++){
                request.getEquipment().getEquipment_Status().set(i, "Pending");
            }
        }
    }

    private class RequestStatusAsyncTask extends AsyncTask<Void, Void, Void> {

        private Request request;
        private String id, request_id;

        RequestStatusAsyncTask(String id, String request_id, Request request){
            this.id = id;
            this.request_id = request_id;
            this.request = request;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            com.example.mobilereservation.network.apiService.request api = ApiClient.getClient(getActivity().getApplicationContext()).create(request.class);
            Call<Request> call = api.updateRequest(id, request_id, request);
            call.enqueue(new Callback<Request>() {
                @Override
                public void onResponse(Call<Request> call, Response<Request> response) {
                    if(response.code() == 201 || response.code() == 200){
                        RequestDialogFragment requestDialogFragment = RequestDialogFragment.newInstance("Successful", response+"\nRequest was successfully Updated\n");
                        requestDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_request");
                    }
                    else{
                        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", response.code()+" "+response.message());
                        errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                    }
                }
                @Override
                public void onFailure(Call<Request> call, Throwable t) {
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", t.getCause()+"\n=========\n"+t.getMessage());
                    errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                }

            });

            return null;
        }
        @Override
        protected void onPostExecute(Void v){}

        @Override
        protected void onPreExecute() {}
    }

}