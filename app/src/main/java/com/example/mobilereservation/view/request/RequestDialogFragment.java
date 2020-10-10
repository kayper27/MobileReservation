package com.example.mobilereservation.view.request;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mobilereservation.R;

public class RequestDialogFragment extends DialogFragment {
    private TextView titleView, detailView;

    public static RequestDialogFragment newInstance(String title, String details) {
        RequestDialogFragment frag = new RequestDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("details", details);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_request, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        titleView = (TextView) view.findViewById(R.id.request_title);
        detailView = (TextView) view.findViewById(R.id.request_detail);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title");
        String details = getArguments().getString("details");
        titleView.setText(title);
        detailView.setText(details);
    }
}

