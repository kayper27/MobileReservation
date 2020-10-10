package com.example.mobilereservation.view.toReturn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.CustomAdapter;
import com.example.mobilereservation.model.DataModel;
import com.example.mobilereservation.viewModel.ToReturnViewModel;

import java.util.ArrayList;

public class ToReturnFragment extends Fragment {

    private ToReturnViewModel mViewModel;
    ArrayList dataModels;
    ListView listView;
    private CustomAdapter adapter;

    public static ToReturnFragment newInstance() {
        return new ToReturnFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_to_return, container, false);
        listView = (ListView) root.findViewById(R.id.listView);

        dataModels = new ArrayList();

        dataModels.add(new DataModel("Apple Pie", false));
        dataModels.add(new DataModel("Banana Bread", false));
        dataModels.add(new DataModel("Cupcake", false));
        dataModels.add(new DataModel("Donut", true));
        dataModels.add(new DataModel("Eclair", true));
        dataModels.add(new DataModel("Froyo", true));
        dataModels.add(new DataModel("Gingerbread", true));
        dataModels.add(new DataModel("Honeycomb", false));
        dataModels.add(new DataModel("Ice Cream Sandwich", false));
        dataModels.add(new DataModel("Jelly Bean", false));
        dataModels.add(new DataModel("Kitkat", false));
        dataModels.add(new DataModel("Lollipop", false));
        dataModels.add(new DataModel("Marshmallow", false));
        dataModels.add(new DataModel("Nougat", false));

        adapter = new CustomAdapter(dataModels, getActivity().getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                DataModel dataModel= (DataModel) dataModels.get(position);
                dataModel.checked = !dataModel.checked;
                Toast.makeText(getContext().getApplicationContext(), "Hello from "+dataModel.name, Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ToReturnViewModel.class);
        // TODO: Use the ViewModel
    }

}