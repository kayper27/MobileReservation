package com.example.mobilereservation.view.reservation;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.MainReservationBinding;
import com.example.mobilereservation.util.DatePickerFragment;
import com.example.mobilereservation.util.TimePickerFragment;
import com.example.mobilereservation.viewModel.ReservationViewModel;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class ReservationFragment extends Fragment {

    private MainReservationBinding mainReservationBinding;
    private EditText textStartAt, textEndAt, textFacility;
    private Button buttonAddEquipment;
    private SlidingUpPanelLayout slidingUpPanelLayout;

    public static final int REQUEST_CODE = 11; // Used to identify the result

    public static ReservationFragment newInstance() {
        return new ReservationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reservation, container, false);

        mainReservationBinding = DataBindingUtil.inflate(inflater, R.layout.main_reservation, container, false);
        View childRootMain = mainReservationBinding.getRoot();
        mainReservationBinding.setViewModel(new ReservationViewModel(getContext().getApplicationContext()));
        mainReservationBinding.executePendingBindings();

        slidingUpPanelLayout = (SlidingUpPanelLayout) root.findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        slidingUpPanelLayout = (SlidingUpPanelLayout) root.findViewById(R.id.sliding_layout);
        textStartAt = (EditText) root.findViewById(R.id.editTextStratAt);
        textEndAt = (EditText) root.findViewById(R.id.editTextEndAt);
        textFacility = (EditText) root.findViewById(R.id.editTextFacility);
        buttonAddEquipment = (Button) root.findViewById(R.id.reservation_add_equipment);

        textStartAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textStartAt.setText("");
                getDateTime(textStartAt);

            }
        });
        textEndAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textEndAt.setText("");
                getDateTime(textEndAt);
            }
        });

        textFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        buttonAddEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
    }

    private void getDateTime(EditText dateTime){
        DialogFragment timePicker  = new TimePickerFragment(dateTime);
        // set the targetFragment to receive the results, specifying the request code
        timePicker.setTargetFragment(ReservationFragment.this,  REQUEST_CODE);
        // show the timePicker
        timePicker.show(getFragmentManager(), "timePicker");
        // create the datePickerFragment
        DialogFragment datePicker = new DatePickerFragment(dateTime);
        // set the targetFragment to receive the results, specifying the request code
        datePicker.setTargetFragment( ReservationFragment.this,  REQUEST_CODE);
        // show the datePicker
        datePicker.show(getFragmentManager(), "datePicker");
    }

}

