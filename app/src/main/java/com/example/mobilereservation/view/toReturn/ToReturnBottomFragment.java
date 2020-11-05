package com.example.mobilereservation.view.toReturn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.FragmentToReturnBottomBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ToReturnBottomFragment extends BottomSheetDialogFragment {

    private FragmentToReturnBottomBinding fragmentToReturnBottomBinding;

    public ToReturnBottomFragment() {
        // Required empty public constructor
    }

    public static ToReturnBottomFragment newInstance() {
        ToReturnBottomFragment fragment = new ToReturnBottomFragment();
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
        fragmentToReturnBottomBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_to_return_bottom, container, false);
        View root = fragmentToReturnBottomBinding.getRoot();
        // Inflate the layout for this fragment
        return root;
    }
}