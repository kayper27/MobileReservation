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
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.listAdapter.ReservationEquipmentListAdapter;
import com.example.mobilereservation.databinding.FragmentReservationBinding;
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

    private FragmentReservationBinding fragmentReservationBinding;

    private EditText textStartAt, textEndAt;

    public static final int REQUEST_CODE = 11; // Used to identify the result

    private ReservationEquipmentListAdapter reservationEquipmentListAdapter; // ADAPTER FOR EQUIPMENT IN LIST

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
            reservationEquipmentListAdapter = new ReservationEquipmentListAdapter(equipmentData, getActivity().getApplicationContext());
            fragmentReservationBinding.reservationEquipmentList.setAdapter(reservationEquipmentListAdapter);
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
        fragmentReservationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reservation, container, false);

        View root = fragmentReservationBinding.getRoot();

        textStartAt = (EditText) root.findViewById(R.id.reservation_StartAt);
        textEndAt = (EditText) root.findViewById(R.id.reservation_EndAt);

        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(facilityReceiver, new IntentFilter("send-facility-data"));// Initiate variables wait form the action key to send data
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(equipmentReceiver, new IntentFilter("send-equipment-data"));

        fragmentReservationBinding.reservationStartAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textStartAt.setText(""); // CLEAR START TIME
                textEndAt.setText("");// CLEAR END TIME
                getDateTime(textStartAt, "");
            }
        });

        fragmentReservationBinding.reservationEndAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textStartAt != null){// CHECK IF START HAS VALUE
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
                if(facilityData != null){
                    fragmentReservationBinding.reservationChangeSchedule.setVisibility(View.VISIBLE);
                    selected = new String [] {fragmentReservationBinding.reservationFacility.getText().toString()};
                }
                if(isSchduleValid()){ // Validate if schedule has data
                    BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance("facility", selected, textStartAt.getText().toString(), textEndAt.getText().toString());
                    bottomSheetFragment.show(getActivity().getSupportFragmentManager(),"TAG");
                }
            }
        });

        fragmentReservationBinding.reservationAddEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(equipmentData.size() > 0){
                    selected = new String [equipmentData.size()];
                    for(int i = 0; i < equipmentData.size(); i++){
                        selected[i] = equipmentData.get(i).getEquipment_id();
                    }
                }

                if(isSchduleValid()){// Validate if schedule has data
                    fragmentReservationBinding.reservationChangeSchedule.setVisibility(View.VISIBLE);
                    BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance("equipment", selected, textStartAt.getText().toString(), textEndAt.getText().toString());
                    bottomSheetFragment.show(getActivity().getSupportFragmentManager(),"TAG");
                }
            }
        });

        fragmentReservationBinding.reservationChangeSchedule.setOnClickListener(new View.OnClickListener() {
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
                                fragmentReservationBinding.reservationFacility.setText("");
                                equipmentData.clear();
                                reservationEquipmentListAdapter = new ReservationEquipmentListAdapter(equipmentData, getActivity().getApplicationContext());
                                fragmentReservationBinding.reservationEquipmentList.setAdapter(reservationEquipmentListAdapter);
                                fragmentReservationBinding.reservationChangeSchedule.setVisibility(View.GONE);
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
                
            }
        });

        fragmentReservationBinding.reservationScheduleInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Add your equipment by pressing the button bellow."+
                                    "\nQ&A\n+" +
                                    "Q: Why I cannot find the equipment that I need?\nA: The equipment is already booked in that time slot"+
                                    "Q: How to remove equipment in list?\nA: Swipe right then press remove");
                builder.setCancelable(true);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {} });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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