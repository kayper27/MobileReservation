package com.example.mobilereservation.adapters;

import android.view.View;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;

public class bindingAdapter {
    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null)
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
