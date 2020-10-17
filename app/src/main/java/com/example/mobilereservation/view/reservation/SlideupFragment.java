package com.example.mobilereservation.view.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.FragmentSlideupBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SlideupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlideupFragment extends Fragment {

    private FragmentSlideupBinding slideUpFragmentBinding;
    private Button buttonOK;

    public SlideupFragment() {
        // Required empty public constructor
    }

    public static SlideupFragment newInstance() {
        SlideupFragment fragment = new SlideupFragment();
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
        slideUpFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_slideup, container,false);
        View childRoot = slideUpFragmentBinding.getRoot();

        buttonOK = (Button) childRoot.findViewById(R.id.reservation_ok);
        buttonOK.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));

        slideUpFragmentBinding.reservationSearch.setActivated(false);
        slideUpFragmentBinding.reservationSearch.setQueryHint("Search");
        slideUpFragmentBinding.reservationSearch.onActionViewExpanded();
        slideUpFragmentBinding.reservationSearch.setIconified(false);
        slideUpFragmentBinding.reservationSearch.clearFocus();
        return  childRoot;
    }
}