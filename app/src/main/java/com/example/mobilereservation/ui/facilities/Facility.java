package com.example.mobilereservation.ui.facilities;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.VoiceInteractor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilereservation.R;

import org.json.JSONObject;

public class Facility extends Fragment {

    private static final String URL = "http://192.168.85.194:3000/";
    private FacilityViewModel facilityViewModel;

    public static Facility newInstance() {
        return new Facility();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        facilityViewModel = ViewModelProviders.of(this).get(FacilityViewModel.class);
        View root = inflater.inflate(R.layout.facility_fragment, container, false);
        final TextView textView = root.findViewById(R.id.text_facility);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());
                    }
                }
        );

        requestQueue.add(objectRequest);

        facilityViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        facilityViewModel = ViewModelProviders.of(this).get(FacilityViewModel.class);
        // TODO: Use the ViewModel
    }

}