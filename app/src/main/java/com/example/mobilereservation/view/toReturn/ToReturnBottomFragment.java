package com.example.mobilereservation.view.toReturn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.databinding.DataBindingUtil;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.listAdapter.ToReturnEquipmentListAdapter;
import com.example.mobilereservation.databinding.FragmentToReturnBottomBinding;
import com.example.mobilereservation.model.Request;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ToReturnBottomFragment extends BottomSheetDialogFragment {

    private static final String REQUEST = "request";
    private Request request;

    private FragmentToReturnBottomBinding fragmentToReturnBottomBinding;

    private FrameLayout bottomSheet;
    private BottomSheetDialog dialog;
    private BottomSheetBehavior behavior;

    public ToReturnBottomFragment() {
        // Required empty public constructor
    }

    public static ToReturnBottomFragment newInstance(Request request) {
        ToReturnBottomFragment fragment = new ToReturnBottomFragment();
        Bundle args = new Bundle();
        args.putSerializable(REQUEST, request);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.request = (Request) getArguments().getSerializable(REQUEST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentToReturnBottomBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_to_return_bottom, container, false);
        View root = fragmentToReturnBottomBinding.getRoot();

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dialog = (BottomSheetDialog) ToReturnBottomFragment.this.getDialog();
                if (dialog != null) {
                    bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
                    if (bottomSheet != null) {
                        behavior = BottomSheetBehavior.from(bottomSheet);
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        behavior.setSkipCollapsed(true);
                        behavior.setHideable(false);
                    }
                }
            }
        });


        fragmentToReturnBottomBinding.toReturnRequestId.setText(request.getRequest_id());

        ToReturnEquipmentListAdapter toReturnEquipmentListAdapter = new ToReturnEquipmentListAdapter((ArrayList<String>) request.getEquipment_id(), getActivity().getApplicationContext());
        fragmentToReturnBottomBinding.toReturnList.setAdapter(toReturnEquipmentListAdapter);
        // Inflate the layout for this fragment
        return root;
    }
}