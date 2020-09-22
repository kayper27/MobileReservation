package com.example.mobilereservation.ui.facilities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mobilereservation.R;

public class FacilityDialogFragment extends DialogFragment {

    public FacilityDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }
    public static FacilityDialogFragment newInstance(String title, String details) {
        FacilityDialogFragment frag = new FacilityDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("details", details);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.facility_details, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        TextView titleView = (TextView) view.findViewById(R.id.facility_title);
        TextView detailView = (TextView) view.findViewById(R.id.facility_detail);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title");
        String details = getArguments().getString("details");
        titleView.setText(title);
        detailView.setText(details);
    }
}
