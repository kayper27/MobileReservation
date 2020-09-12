package com.example.mobilereservation.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    FragmentSlideshowBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_slideshow, container, false);
        mBinding.tvHeading.setText("Welcome to JournalDev.com");
        mBinding.tvSubHeading.setText("Welcome to this Android Tutorial on DataBinding");
        View root = mBinding.getRoot();

        return root;
    }
}