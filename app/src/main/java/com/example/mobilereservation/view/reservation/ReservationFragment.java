package com.example.mobilereservation.view.reservation;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mobilereservation.R;
import com.example.mobilereservation.model.Equipment;
import com.example.mobilereservation.util.DatePickerFragment;
import com.example.mobilereservation.util.TimePickerFragment;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationFragment extends Fragment {

    private static final String EQUIPMENT = "equipment";

    private EditText textStartAt, textEndAt, textFacility;
    private Button changeSchedule, buttonAddEquipment, submitButton;

    public static final int REQUEST_CODE = 11; // Used to identify the result

    private BroadcastReceiver facilityReceiver = new BroadcastReceiver() {// BroadcastReceiver Variable that listen to intents from BottomFragmentDialog
        @Override
        public void onReceive(Context context, Intent intent) {
            String facilityData = intent.getStringExtra("facility");
            Log.d("receiver", "Got message: " + facilityData);
            textFacility.setText(facilityData);
        }
    };

    private BroadcastReceiver equipmentReceiver = new BroadcastReceiver() {// BroadcastReceiver Variable that listen to intents from BottomFragmentDialog
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Equipment> equipmentData = (ArrayList<Equipment>) intent.getSerializableExtra("equipment");
            Log.d("receiver", "Got message: " + equipmentData);
            System.out.println("|TEST| "+equipmentData);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reservation, container, false);

        textStartAt = (EditText) root.findViewById(R.id.editTextStratAt);
        textEndAt = (EditText) root.findViewById(R.id.editTextEndAt);
        textFacility = (EditText) root.findViewById(R.id.editTextFacility);
        changeSchedule = (Button) root.findViewById(R.id.reservation_change_schedule);
        buttonAddEquipment = (Button) root.findViewById(R.id.reservation_add_equipment);
        submitButton = (Button) root.findViewById(R.id.reservation_submit_button);

        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(facilityReceiver, new IntentFilter("send-facility-data"));// Initiate variables wait form the action key to send data
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(equipmentReceiver, new IntentFilter("send-equipment-data"));

        textStartAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textStartAt.setText(""); // CLEAR START TIME
                textEndAt.setText("");// CLEAR END TIME
                getDateTime(textStartAt, "");
            }
        });

        textEndAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textStartAt.getText().length() == 16){// CHECK IF START HAS VALUE
                    textEndAt.setText("");
                    getDateTime(textEndAt, textStartAt.getText().toString()); // CALL DATE AND TIME DIALOG
                }
                else{
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Invalid Input", "Please select when your schedule starts");
                    errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
                }
            }
        });

        textFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(true || isSchduleValid()){ // Change this when done coding
                    changeSchedule.setVisibility(View.VISIBLE);
                    BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance("facility", textStartAt.getText().toString(), textEndAt.getText().toString());
                    bottomSheetFragment.show(getActivity().getSupportFragmentManager(),"TAG");
                }
            }
        });
        buttonAddEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(true || isSchduleValid()){ // Change this when done coding
                    changeSchedule.setVisibility(View.VISIBLE);
                    BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance("equipment", textStartAt.getText().toString(), textEndAt.getText().toString());
                    bottomSheetFragment.show(getActivity().getSupportFragmentManager(),"TAG");
                }
            }
        });
        changeSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Changing your schedule now will erase everything.");
                builder.setCancelable(true);
                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                textStartAt.setText("");
                                textEndAt.setText("");
                                textStartAt.setEnabled(true);
                                textEndAt.setEnabled(true);
                                textFacility.setText("");
                                changeSchedule.setVisibility(View.GONE);
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
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        return root;
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

    public boolean isSchduleValid(){
        boolean flag = false;
        if(textStartAt.getText().length() != 16 || textEndAt.getText().length() != 16){
            ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Invalid Input", "Please select your schedule");
            errorDialogFragment.show(getActivity().getSupportFragmentManager(), "dialog_error");
        }
        else{
            textStartAt.setEnabled(false);
            textEndAt.setEnabled(false);
            flag = true;
        }
        return flag;
    }
}