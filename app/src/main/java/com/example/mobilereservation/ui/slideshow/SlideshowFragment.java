package com.example.mobilereservation.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mobilereservation.ListAdapter;
import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.FragmentSlideshowBinding;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    ListAdapter adapter;
    List<String> arrayList= new ArrayList<>();
    FragmentSlideshowBinding mBinding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_slideshow, container, false);
        arrayList.add("January");
        arrayList.add("February");
        arrayList.add("March");
        arrayList.add("April");
        arrayList.add("May");
        arrayList.add("June");
        arrayList.add("July");
        arrayList.add("August");
        arrayList.add("September");
        arrayList.add("October");
        arrayList.add("November");
        arrayList.add("December");
        adapter = new ListAdapter(arrayList);
        mBinding.listView.setAdapter(adapter);

        mBinding.search.setActivated(true);
        mBinding.search.setQueryHint("Type your keyword here");
        mBinding.search.onActionViewExpanded();
        mBinding.search.setIconified(false);
        mBinding.search.clearFocus();
        View root = mBinding.getRoot();
        mBinding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);

                return false;
            }
        });

        return root;
    }
}