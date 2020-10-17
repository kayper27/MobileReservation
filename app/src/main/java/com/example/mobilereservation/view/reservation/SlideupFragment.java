package com.example.mobilereservation.view.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SlideupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlideupFragment extends Fragment {

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slideup, container, false);
    }
}