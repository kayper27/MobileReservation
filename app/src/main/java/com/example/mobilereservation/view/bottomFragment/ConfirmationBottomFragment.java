package com.example.mobilereservation.view.bottomFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.FragmentConfirmationBottomBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ConfirmationBottomFragment extends BottomSheetDialogFragment {

    private FragmentConfirmationBottomBinding fragmentConfirmationBottomBinding;

    public ConfirmationBottomFragment() {
        // Required empty public constructor
    }

    public static ConfirmationBottomFragment newInstance() {
        ConfirmationBottomFragment fragment = new ConfirmationBottomFragment();
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
        fragmentConfirmationBottomBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirmation_bottom, container, false);
        View root = fragmentConfirmationBottomBinding.getRoot();

        return root;
    }
}