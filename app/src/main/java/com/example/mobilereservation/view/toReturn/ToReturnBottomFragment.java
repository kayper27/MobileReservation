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
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;


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

        ToReturnEquipmentListAdapter toReturnEquipmentListAdapter = new ToReturnEquipmentListAdapter( request.getEquipment(), getActivity().getApplicationContext());
        fragmentToReturnBottomBinding.toReturnList.setAdapter(toReturnEquipmentListAdapter);

        fragmentToReturnBottomBinding.toReturnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String json = gson.toJson(request);
                boolean flag = true;
                for(int i = 0; i < request.getEquipment().getEquipment_Status().size(); i++){
                    if(request.getEquipment().getEquipment_Status().equals("Pending")){
                        flag = false;
                        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Invalid", "Please select status for equipment");
                        errorDialogFragment.show(getActivity().getSupportFragmentManager(), "error_dialog");
                        break;
                    }
                }
                if(flag){

                }
            }
        });
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(request != null){
            for(int i = 0; i < request.getEquipment().getEquipment_Status().size(); i++){
                request.getEquipment().getEquipment_Status().set(i, "Pending");
            }
        }
    }

}