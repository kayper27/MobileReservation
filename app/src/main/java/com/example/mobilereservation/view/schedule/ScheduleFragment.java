package com.example.mobilereservation.view.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.FragmentScheduleBinding;
import com.example.mobilereservation.viewModel.ScheduleViewModel;

public class ScheduleFragment extends Fragment {

    private FragmentScheduleBinding fragmentScheduleBinding;

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentScheduleBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false);
        fragmentScheduleBinding.setViewModel((new ScheduleViewModel()));
        fragmentScheduleBinding.executePendingBindings();
        return fragmentScheduleBinding.getRoot();
    }


}