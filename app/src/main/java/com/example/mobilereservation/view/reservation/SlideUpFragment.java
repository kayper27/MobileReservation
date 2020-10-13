package com.example.mobilereservation.view.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.SlideupReservationBinding;

public class SlideUpFragment extends Fragment {

    public SlideUpFragment() {
        // Required empty public constructor
    }

    public static SlideUpFragment newInstance() {
        SlideUpFragment fragment = new SlideUpFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private SlideupReservationBinding slideupReservationBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        slideupReservationBinding = DataBindingUtil.inflate(inflater, R.layout.slideup_reservation, container,false);
        slideupReservationBinding.reservationSearch.setActivated(true);
        slideupReservationBinding.reservationSearch.setQueryHint("Search");
        slideupReservationBinding.reservationSearch.onActionViewExpanded();
        slideupReservationBinding.reservationSearch.setIconified(false);
        slideupReservationBinding.reservationSearch.clearFocus();
        return  slideupReservationBinding.getRoot();
    }
}