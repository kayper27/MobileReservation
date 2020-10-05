package com.example.mobilereservation.view.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.FragmentReservationBinding;
import com.example.mobilereservation.viewModel.ReservationViewModel;

public class ReservationFragment extends Fragment {

    FragmentReservationBinding fragmentReservationBinding;

    public static ReservationFragment newInstance() {
        return new ReservationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentReservationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reservation, container, false);
        fragmentReservationBinding.setViewModel((new ReservationViewModel(getActivity().getApplicationContext())));
        fragmentReservationBinding.executePendingBindings();
        return fragmentReservationBinding.getRoot();
    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null)
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }

}