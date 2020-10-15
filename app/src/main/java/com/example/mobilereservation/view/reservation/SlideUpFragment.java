package com.example.mobilereservation.view.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.SlideupReservationBinding;

public class SlideUpFragment extends Fragment {

    private static final String CATEGORY = "category";
    private String category;

    private SlideupReservationBinding slideupReservationBinding;
    private ListView listInterest;
    private Button buttonOK;


    public SlideUpFragment() {
        // Required empty public constructor
    }

    public static SlideUpFragment newInstance(String category) {
        SlideUpFragment fragment = new SlideUpFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        slideupReservationBinding = DataBindingUtil.inflate(inflater, R.layout.slideup_reservation, container,false);
        View childRoot = slideupReservationBinding.getRoot();

        listInterest = (ListView) childRoot.findViewById(R.id.reservation_list);
        buttonOK = (Button) childRoot.findViewById(R.id.reservation_ok);

        buttonOK.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));

        slideupReservationBinding.reservationSearch.setActivated(false);
        slideupReservationBinding.reservationSearch.setQueryHint("Search");
        slideupReservationBinding.reservationSearch.onActionViewExpanded();
        slideupReservationBinding.reservationSearch.setIconified(false);
        slideupReservationBinding.reservationSearch.clearFocus();
        return  childRoot;
    }
}