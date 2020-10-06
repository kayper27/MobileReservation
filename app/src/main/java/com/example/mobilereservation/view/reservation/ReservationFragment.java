package com.example.mobilereservation.view.reservation;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.FragmentReservationBinding;
import com.example.mobilereservation.viewModel.ReservationViewModel;

public class ReservationFragment extends Fragment {

    private FragmentReservationBinding fragmentReservationBinding;
    private EditText textStratAt, textEndAt;

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
        return root;
    }


}

