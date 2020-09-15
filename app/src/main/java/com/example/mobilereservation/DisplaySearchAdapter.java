package com.example.mobilereservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.databinding.DataBindingUtil;

import com.example.mobilereservation.databinding.FragmentGalleryBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class DisplaySearchAdapter{

    private static CustomAdapter adapter;


    private Context context;
    private LayoutInflater inflater;
    private ViewGroup container;
    ArrayList<DataModel> dataModels;

    ArrayList<DataModel> mData;
    ArrayList<DataModel> mStringFilterList;

    FragmentGalleryBinding maindBinding;

    public DisplaySearchAdapter(Context context, LayoutInflater inflater, ViewGroup container, ArrayList<DataModel> dataModels) {
        this.context = context;
        this.inflater = inflater;
        this.container = container;
        this.dataModels = dataModels;
        this.mData = dataModels;
        this.mStringFilterList = dataModels;
    }

    public View setDisplay(){
        maindBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false);
        View root = maindBinding.getRoot();
        final ListView listView = root.findViewById(R.id.list);

        maindBinding.searchList.setActivated(true);
        maindBinding.searchList.setQueryHint("Type your keyword here");
        maindBinding.searchList.onActionViewExpanded();
        maindBinding.searchList.setIconified(false);
        maindBinding.searchList.clearFocus();

        maindBinding.searchList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("Test Here !");
                return false;
            }
        });

        adapter= new CustomAdapter(dataModels, context);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataModel dataModel= dataModels.get(position);
                Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getType()+" API: "+dataModel.getVersion_number(), Snackbar.LENGTH_LONG).setAction("No action", null).show();
            }
        });
        return root;
    }


}
