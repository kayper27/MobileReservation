package com.example.mobilereservation.view.toReturn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobilereservation.R;
import com.example.mobilereservation.viewModel.ToReturnViewModel;

public class ToReturnFragment extends Fragment {

    private ToReturnViewModel mViewModel;

    public static ToReturnFragment newInstance() {
        return new ToReturnFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_to_return, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ToReturnViewModel.class);
        // TODO: Use the ViewModel
    }

}