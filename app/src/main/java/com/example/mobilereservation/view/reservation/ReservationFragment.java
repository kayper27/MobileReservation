package com.example.mobilereservation.view.reservation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.listAdapter.ReservationEquipmentListAdapter;
import com.example.mobilereservation.databinding.FragmentReservationBinding;
import com.example.mobilereservation.model.CreateRequest;
import com.example.mobilereservation.model.Equipment;
import com.example.mobilereservation.model.Equips;
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.request;
import com.example.mobilereservation.util.DatePickerFragment;
import com.example.mobilereservation.util.TimePickerFragment;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;
import com.example.mobilereservation.view.dialog.RequestDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationFragment extends Fragment {

    private static final String TAG = "ReservationFragment";

    private FragmentReservationBinding fragmentReservationBinding;

    private EditText textStartAt, textEndAt;

    public static final int REQUEST_CODE = 11; // Used to identify the result

    private ReservationEquipmentListAdapter reservationEquipmentListAdapter; // ADAPTER FOR EQUIPMENT IN LIST

    private long mLastClickTime = 0;
    private long THRESHOLD = 1000; // ms threshold

    ArrayList<Equipment> equipmentData = new ArrayList<>();// HOLDS EQUIPMENT DATA
    String facilityData;// HOLDS FACILITY DATA
    String[] selected;

    private BroadcastReceiver facilityReceiver = new BroadcastReceiver() {// BroadcastReceiver Variable that listen to intents from BottomFragmentDialog
        @Override
        public void onReceive(Context context, Intent intent) {
            facilityData = intent.getStringExtra("facility");
            Log.d("receiver", "Got message: " + facilityData);
            fragmentReservationBinding.reservationFacility.setText(facilityData);
        }
    };

    private BroadcastReceiver equipmentReceiver = new BroadcastReceiver() {// BroadcastReceiver Variable that listen to intents from BottomFragmentDialog
        @Override
        public void onReceive(Context context, Intent intent) {
            equipmentData = (ArrayList<Equipment>) intent.getSerializableExtra("equipment");
            Log.d("receiver", "Got message: " + equipmentData);
            try {
                if(equipmentData.size() > 0 || equipmentData != null){
                    reservationEquipmentListAdapter = new ReservationEquipmentListAdapter(equipmentData, getActivity().getApplicationContext());
                    fragmentReservationBinding.reservationEquipmentList.setAdapter(reservationEquipmentListAdapter);
                }
            }
            catch (Exception e){
                Log.d(TAG, "ERROR IN onReceive "+e.getMessage().toUpperCase());
                System.out.println("|TEST| "+e.getMessage());
            }
        }
    };

    public ReservationFragment() {
        // Required empty public constructor
    }

    public static ReservationFragment newInstance() {
        ReservationFragment fragment = new ReservationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentReservationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reservation, container, false);

        View root = fragmentReservationBinding.getRoot();

        textStartAt = (EditText) root.findViewById(R.id.reservation_StartAt);
        textEndAt = (EditText) root.findViewById(R.id.reservation_EndAt);

        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(facilityReceiver, new IntentFilter("send-facility-data"));// Initiate variables wait form the action key to send data
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(equipmentReceiver, new IntentFilter("send-equipment-data"));

        fragmentReservationBinding.reservationStartAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                textStartAt.setText(""); // CLEAR START TIME
                textEndAt.setText("");// CLEAR END TIME
                getDateTime(textStartAt, "");
            }
        });

        fragmentReservationBinding.reservationEndAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(textStartAt.getText().toString().length() == 16 && !textStartAt.getText().toString().isEmpty()){// CHECK IF START HAS VALUE
                    textEndAt.setText("");
                    getDateTime(textEndAt, textStartAt.getText().toString()); // CALL DATE AND TIME DIALOG
                }
                else{
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Invalid Input", "Please select when your schedule starts");
                    errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                }
            }
        });

        fragmentReservationBinding.reservationFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if(facilityData != null){
                    selected = new String [] {fragmentReservationBinding.reservationFacility.getText().toString()};
                }
                else{
                    selected = null;
                }

                if(isScheduleValid()){ // Validate if schedule has data
                    ReservationBottomFragment reservationBottomFragment = ReservationBottomFragment.newInstance("facility", selected, textStartAt.getText().toString(), textEndAt.getText().toString());
                    reservationBottomFragment.show(getActivity().getSupportFragmentManager(),"TAG");
                }
            }
        });

        fragmentReservationBinding.reservationAddEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                selected = new String [equipmentData.size()];
                if(equipmentData.size() > 0){
                    for(int i = 0; i < equipmentData.size(); i++){
                        selected[i] = equipmentData.get(i).getEquipment_id();
                    }
                }
                if(isScheduleValid()){// Validate if schedule has data
                    ReservationBottomFragment reservationBottomFragment = ReservationBottomFragment.newInstance("equipment", selected, textStartAt.getText().toString(), textEndAt.getText().toString());
                    reservationBottomFragment.show(getActivity().getSupportFragmentManager(),"TAG");
                }
            }
        });

        fragmentReservationBinding.reservationChangeSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Changing your schedule now will erase everything.");
                builder.setCancelable(true);
                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                wipeCLean();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        fragmentReservationBinding.reservationSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                String startAt = textStartAt.getText().toString();
                String endAt = textEndAt.getText().toString();
                String facility = fragmentReservationBinding.reservationFacility.getText().toString();
                ArrayList<String> equips_id = new ArrayList<>();
                ArrayList<String> equips_status = new ArrayList<>();

                if(facility.isEmpty() && equipmentData.size() == 0){
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Request Invalid","Your request is invalid");
                    errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                }
                else {
                    for(int i = 0; i < equipmentData.size(); i++){
                        equips_id.add(equipmentData.get(i).getEquipment_id());
                        equips_status.add("Pending");
                    }
                    RequestAsyncTask asyncTask = new RequestAsyncTask(new CreateRequest( "2015105910", "2015105910", startAt, endAt, facility, new Equips(equips_id, equips_status)));
                    asyncTask.execute();
                }

            }
        });

        fragmentReservationBinding.reservationScheduleInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Select your schedule before adding facility and equipment.\nFormat: yyyy-MM-dd HH:mm");
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {} });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        fragmentReservationBinding.reservationFacilityInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Add your facility by pressing the field bellow.\nQ&A\nQ: Why I cannot find the room that I need?\nA: The room is already booked in that time slot");
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {} });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        fragmentReservationBinding.reservationEquipmentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Add your equipment by pressing the button bellow."+
                                    "\nQ&A\n" +
                                    "Q: Why I cannot find the equipment that I need?\nA: The equipment is already booked in that time slot"+
                                    "\nQ: How to remove equipment in list?\nA: Swipe right then press remove");
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {} });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return root;
    }

    private class RequestAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        private CreateRequest request;

        RequestAsyncTask(CreateRequest request){
            this.request = request;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            request api = ApiClient.getClient(getActivity().getApplicationContext()).create(request.class);
            Call<CreateRequest> call = api.createRequest(request);
            call.enqueue(new Callback<CreateRequest>() {
                @Override
                public void onResponse(Call<CreateRequest> call, Response<CreateRequest> response) {
                    if(response.code() == 201 || response.code() == 200){
                        wipeCLean();
                        RequestDialogFragment requestDialogFragment = RequestDialogFragment.newInstance("Successful", response+"\nYour request was successfully sent \n");
                        requestDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_request");
                    }
                    else{
                        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", response.code()+" "+response.message()+"\nYour schedule has a conflict with equipment or facility check the schedule tab then search by start date, facility or equipment");
                        errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                    }
                }

                @Override
                public void onFailure(Call<CreateRequest> call, Throwable t) {
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", t.getCause()+"\n=========\n"+t.getMessage());
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

    private void getDateTime(EditText dateTime,@Nullable String startData){
        DialogFragment timePicker  = new TimePickerFragment(dateTime, startData);// PASS ITS OWN EDITTEXT AND START DATETIME TO END
        // set the targetFragment to receive the results, specifying the request code
        timePicker.setTargetFragment(ReservationFragment.this,  REQUEST_CODE);
        // show the timePicker
        timePicker.show(getFragmentManager(), "timePicker");
        // create the datePickerFragment
        DialogFragment datePicker = new DatePickerFragment(dateTime, startData); // PASS ITS OWN EDITTEXT AND START DATETIME TO END
        // set the targetFragment to receive the results, specifying the request code
        datePicker.setTargetFragment( ReservationFragment.this,  REQUEST_CODE);
        // show the datePicker
        datePicker.show(getFragmentManager(), "datePicker");
    }

    private boolean isScheduleValid(){
        boolean flag = false;
        if(textStartAt.getText().length() != 16 || textEndAt.getText().length() != 16){
            ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Invalid Input", "Please select your schedule");
            errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
        }
        else{
            textStartAt.setEnabled(false);
            textEndAt.setEnabled(false);
            fragmentReservationBinding.reservationChangeSchedule.setVisibility(View.VISIBLE);
            flag = true;
        }
        return flag;
    }

    private void wipeCLean(){
        textStartAt.setText("");
        textEndAt.setText("");
        textStartAt.setEnabled(true);
        textEndAt.setEnabled(true);
        equipmentData.clear();
        fragmentReservationBinding.reservationFacility.setText("");
        reservationEquipmentListAdapter = new ReservationEquipmentListAdapter(equipmentData, getActivity().getApplicationContext());
        fragmentReservationBinding.reservationEquipmentList.setAdapter(reservationEquipmentListAdapter);
        fragmentReservationBinding.reservationChangeSchedule.setVisibility(View.GONE);
    }
}