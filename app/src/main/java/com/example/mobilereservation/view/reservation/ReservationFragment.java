package com.example.mobilereservation.view.reservation;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.FragmentReservationBinding;
import com.example.mobilereservation.util.DatePickerFragment;
import com.example.mobilereservation.viewModel.ReservationViewModel;

public class ReservationFragment extends Fragment {

    private FragmentReservationBinding fragmentReservationBinding;
    private EditText textStratAt, textEndAt;
    String selectedDate;

    public static final int REQUEST_CODE = 11; // Used to identify the result

    public static ReservationFragment newInstance() {
        return new ReservationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentReservationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reservation, container, false);
        View root = fragmentReservationBinding.getRoot();

        fragmentReservationBinding.setViewModel((new ReservationViewModel(getContext().getApplicationContext())));
        fragmentReservationBinding.executePendingBindings();

        textStratAt = (EditText) root.findViewById(R.id.editTextStratAt);
        textEndAt = (EditText) root.findViewById(R.id.editTextEndAt);

        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();

        // Using an onclick listener on the editText to show the datePicker
        textStratAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create the datePickerFragment
                DialogFragment datePicker = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                datePicker.setTargetFragment( ReservationFragment.this,  REQUEST_CODE);
                // show the datePicker
                datePicker.show(getFragmentManager(), "datePicker");
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            selectedDate = data.getStringExtra("selectedDate");
            // set the value of the editText
            textStratAt.setText(selectedDate);
        }
    }

}

