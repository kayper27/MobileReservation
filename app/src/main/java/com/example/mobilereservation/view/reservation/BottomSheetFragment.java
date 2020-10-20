package com.example.mobilereservation.view.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mobilereservation.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private static final String TAG = "BottomSheet";

    private static final String CATEGORY = "category";
    private static final String START = "start";
    private static final String END = "end";
    private static String category = "", start = "", end = "";

    public BottomSheetFragment() {}

    public static BottomSheetFragment newInstance(String category, String start, String end) {
        BottomSheetFragment frag = new BottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        args.putString(START, start);
        args.putString(END, end);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottoms_sheet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
