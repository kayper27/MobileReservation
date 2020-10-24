package com.example.mobilereservation.view.pending;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.example.mobilereservation.R;
import com.example.mobilereservation.adapters.MyListAdapter;
import com.example.mobilereservation.model.Continent;
import com.example.mobilereservation.model.Country;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PendingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendingFragment extends Fragment implements SearchView.OnQueryTextListener, SearchView.OnClickListener{

    private SearchView search;
    private MyListAdapter listAdapter;
    private ExpandableListView myList;
    private ArrayList<Continent> continentList = new ArrayList<>();

    public PendingFragment() {
        // Required empty public constructor
    }

    public static PendingFragment newInstance() {
        PendingFragment fragment = new PendingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pending, container, false);;

        SearchManager searchManager= (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        search =  (SearchView) root.findViewById(R.id.pending_search);
        search.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        search.setIconified(false);
        search.setOnQueryTextListener(this);
        search.setOnClickListener(this);

        displayList(root);
        expandAll();
        // Inflate the layout for this fragment
        return root;
    }

    private void expandAll(){
        int count = listAdapter.getGroupCount();
        for(int i = 0; i < count; i++){
            myList.expandGroup(i);
        }
    }


    private void displayList(View v){
        loadSomeData();
        myList = (ExpandableListView) v.findViewById(R.id.pendingExpandableListView);
        listAdapter = new MyListAdapter(getActivity().getApplicationContext(), continentList);
        myList.setAdapter(listAdapter);
    }

    private void loadSomeData(){
        ArrayList<Country> countryList = new ArrayList<>();
        Country country = new Country("BMU", "Bermuda", 100000000);
        countryList.add(country);
        country = new Country("CAN", "Canada", 200000000);
        countryList.add(country);
        country = new Country("USA", "United States", 500000000);
        countryList.add(country);

        Continent continent = new Continent("North America", countryList);
        continentList.add(continent);

        countryList = new ArrayList<>();
        country = new Country("CHN", "China", 100000100);
        countryList.add(country);
        country = new Country("JPN", "Japan", 200002000);
        countryList.add(country);
        country = new Country("THA", "Thailand", 500000000);
        countryList.add(country);

        continent = new Continent("Asia", countryList);
        continentList.add(continent);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        listAdapter.filterData(query);
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        listAdapter.filterData(newText);
        expandAll();
        return false;
    }
}