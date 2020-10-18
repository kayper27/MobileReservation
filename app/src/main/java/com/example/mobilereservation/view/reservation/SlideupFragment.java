package com.example.mobilereservation.view.reservation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.serachAdapter.FacilitySearchAdapter;
import com.example.mobilereservation.databinding.FragmentSlideupBinding;
import com.example.mobilereservation.model.Facility;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SlideupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlideupFragment extends Fragment {

    private FragmentSlideupBinding slideUpFragmentBinding;
    private FacilitySearchAdapter adapterSearch;
    private Button buttonOK;

    private static final String CATEGORY = "category";
    private static final String START = "start";
    private static final String END = "end";
    private static String category = "", start = "", end = "";

    private ArrayList<Facility> facilitySet = new ArrayList<>();
    public SlideupFragment() {
        // Required empty public constructor
    }

    public static SlideupFragment newInstance(String category, String start, String end) {
        SlideupFragment fragment = new SlideupFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        args.putString(START, start);
        args.putString(END, end);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(CATEGORY);
            start = getArguments().getString(START);
            end = getArguments().getString(END);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        slideUpFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_slideup, container,false);
        View childRoot = slideUpFragmentBinding.getRoot();
        Toast.makeText(getContext().getApplicationContext(), "Test", Toast.LENGTH_SHORT);
        buttonOK = (Button) childRoot.findViewById(R.id.reservation_ok);

        buttonOK.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Test", "THIS IS DISPLAYING NOTHING |"+category+"|");
                errorDialogFragment.show(getFragmentManager(), "dialog_equipment");
            }
        }));

        adapterSearch = new FacilitySearchAdapter(getActivity().getApplicationContext(), getActivity().getSupportFragmentManager(), facilitySet);
        slideUpFragmentBinding.reservationList.setAdapter(adapterSearch);

        slideUpFragmentBinding.reservationSearch.setActivated(false);
        slideUpFragmentBinding.reservationSearch.setQueryHint("Search");
        slideUpFragmentBinding.reservationSearch.onActionViewExpanded();
        slideUpFragmentBinding.reservationSearch.setIconified(false);
        slideUpFragmentBinding.reservationSearch.clearFocus();
        return  childRoot;
    }
}