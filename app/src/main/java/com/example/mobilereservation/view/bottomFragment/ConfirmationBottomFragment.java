package com.example.mobilereservation.view.bottomFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.databinding.DataBindingUtil;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.FragmentConfirmationBottomBinding;
import com.example.mobilereservation.model.Request;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.request;
import com.example.mobilereservation.util.FormatDateTime;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;
import com.example.mobilereservation.view.dialog.RequestDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationBottomFragment extends BottomSheetDialogFragment {

    private static final String REQUEST = "request";
    private static final String FLAG = "flag";
    private Request request;

    private FragmentConfirmationBottomBinding fragmentConfirmationBottomBinding;

    private FormatDateTime dateTime = new FormatDateTime();// FOR FORMATTING DATE

    private FrameLayout bottomSheet;
    private BottomSheetDialog dialog;
    private BottomSheetBehavior behavior;

    public ConfirmationBottomFragment() {
        // Required empty public constructor
    }

    public static ConfirmationBottomFragment newInstance(Request request, boolean flag) {
        ConfirmationBottomFragment fragment = new ConfirmationBottomFragment();
        Bundle args = new Bundle();
        args.putSerializable(REQUEST, request);
        args.putBoolean(FLAG, flag);
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
        fragmentConfirmationBottomBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirmation_bottom, container, false);
        View root = fragmentConfirmationBottomBinding.getRoot();

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dialog = (BottomSheetDialog) ConfirmationBottomFragment.this.getDialog();
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

        fragmentConfirmationBottomBinding.confirmationRequestId.setText(request.getRequest_id());
        fragmentConfirmationBottomBinding.confirmationUsername.setText("Username:\n" +request.getUsername());
        fragmentConfirmationBottomBinding.confirmationStartAt.setText("Start:\n" +dateTime.formatDateTime(request.getStartAt()));
        fragmentConfirmationBottomBinding.confirmationEndAt.setText("End:\n" +dateTime.formatDateTime(request.getEndAt()));
        fragmentConfirmationBottomBinding.confirmationFacility.setText("Facility:\n" +request.getFacility());
        fragmentConfirmationBottomBinding.confirmationEquipment.setText("Equipment:\n" +request.getEquipment().getEquipment_id());
        fragmentConfirmationBottomBinding.confirmationPurpose.setText("Purpose:\n" +request.getPurpose());

        if(getArguments().getBoolean(FLAG)){
            fragmentConfirmationBottomBinding.confirmationTitle.setText("Reason for Denying request");
        }
        else{
            fragmentConfirmationBottomBinding.confirmationReason.setVisibility(View.GONE);
            fragmentConfirmationBottomBinding.confirmationReason.setEnabled(false);
            fragmentConfirmationBottomBinding.confirmationTitle.setText("Cancellation of Request\nDo you want continue canceling your request?");
        }
        fragmentConfirmationBottomBinding.confirmationOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.setReason(fragmentConfirmationBottomBinding.confirmationReason.getText().toString());
                if(!request.getReason().isEmpty() || !getArguments().getBoolean(FLAG)){
                    RequestStatusAsyncTask asyncTask = new RequestStatusAsyncTask(request.getRequest_id(), request);
                    asyncTask.execute();
                }
            }
        });

        fragmentConfirmationBottomBinding.confirmationCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return root;
    }

    private class RequestStatusAsyncTask extends AsyncTask<Void, Void, Void> {

        private Request request;
        private String request_id;

        RequestStatusAsyncTask(String request_id, Request request){
            this.request_id = request_id;
            this.request = request;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            com.example.mobilereservation.network.apiService.request api = ApiClient.getClient(getActivity().getApplicationContext()).create(request.class);
            Call<Request> call = api.updateRequest(request_id, request);
            call.enqueue(new Callback<Request>() {
                @Override
                public void onResponse(Call<Request> call, Response<Request> response) {
                    if(response.code() == 201 || response.code() == 200){
                        dialog.dismiss();
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