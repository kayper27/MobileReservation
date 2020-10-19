package com.example.mobilereservation.view.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.util.DatePickerFragment;
import com.example.mobilereservation.util.TimePickerFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationFragment extends Fragment {

    private EditText textStartAt, textEndAt, textFacility;
    private Button buttonAddEquipment;
    private SlidingUpPanelLayout slidingUpPanelLayout;

    public static final int REQUEST_CODE = 11; // Used to identify the result

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
//                SlideupFragment slideupFragment = SlideupFragment.newInstance("facility", textStartAt.getText().toString(), textEndAt.getText().toString());
//                getFragmentManager().beginTransaction().replace(R.id.slidup_fragment, slideupFragment).commit();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        buttonAddEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SlideupFragment slideupFragment = SlideupFragment.newInstance("equipment", textStartAt.getText().toString(), textEndAt.getText().toString());
//                getFragmentManager().beginTransaction().replace(R.id.slidup_fragment, slideupFragment).commit();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


        return root;
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