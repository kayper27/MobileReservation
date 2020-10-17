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
import com.example.mobilereservation.databinding.FragmentSlideupBinding;
import com.example.mobilereservation.view.request.RequestDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SlideupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlideupFragment extends Fragment {

    private FragmentSlideupBinding slideUpFragmentBinding;
    private Button buttonOK;

    private static final String CATEGORY = "category";
    private static String category = "";

    public SlideupFragment() {
        // Required empty public constructor
    }

    public static SlideupFragment newInstance(String category) {
        SlideupFragment fragment = new SlideupFragment();
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
        slideUpFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_slideup, container,false);
        View childRoot = slideUpFragmentBinding.getRoot();
        Toast.makeText(getContext().getApplicationContext(), "Test", Toast.LENGTH_SHORT);
        buttonOK = (Button) childRoot.findViewById(R.id.reservation_ok);

        buttonOK.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOK.setText(category);
                RequestDialogFragment equipmentDialogFragment = RequestDialogFragment.newInstance("Test", "THIS IS DISPLAYING NOTHING |"+category+"|");
                equipmentDialogFragment.show(getFragmentManager(), "dialog_equipment");
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