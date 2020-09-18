package com.example.mobilereservation.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;

public class GalleryFragment extends Fragment {

//    private FragmentGalleryBinding maindBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        maindBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        return root;
    }
}