package com.example.mobilereservation.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.mobilereservation.R;
import com.example.mobilereservation.model.Continent;
import com.example.mobilereservation.model.Country;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MyListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Continent> continentList;
    private ArrayList<Continent> originalList;

    public MyListAdapter(Context context, ArrayList<Continent> continentList){
        this.context = context;
        this.continentList = continentList;
        this.continentList.addAll(continentList);
        this.originalList = new ArrayList<>();
        this.originalList.addAll(continentList);
    }


    @Override
    public int getGroupCount() {
        return continentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Country> countryList = continentList.get(groupPosition).getCountriesList();
        return countryList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return continentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Country> countryList = continentList.get(groupPosition).getCountriesList();
        return countryList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        Continent continent = (Continent) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_row, null);
        }

        TextView heading = (TextView) convertView.findViewById(R.id.heading);
        heading.setTypeface(null, Typeface.BOLD);
        heading.setText(continent.getName().trim());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Country country = (Country) getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child_row, null);
        }
        TextView code = (TextView) convertView.findViewById(R.id.code);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView population = (TextView) convertView.findViewById(R.id.population);
        code.setText(country.getCode().trim());
        name.setText(country.getName().trim());
        population.setText(NumberFormat.getNumberInstance(Locale.US).format(country.getPopulation()));

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void filterData(String query){
        query = query.toLowerCase();
        Log.v("MyListAdapter", String.valueOf(continentList.size()));
        continentList.clear();
        if(query.isEmpty()){
            continentList.addAll(originalList);
        }
        else{
            for(Continent continent: originalList){
                ArrayList<Country> countryList = continent.getCountriesList();
                ArrayList<Country> newList = new ArrayList<>();
                for(Country country: countryList){
                    if(country.getCode().toLowerCase().contains(query) || country.getName().toLowerCase().contains(query)){
                        newList.add(country);
                    }
                }
                if(newList.size() > 0){
                    Continent nContnent = new Continent(continent.getName(), newList);
                    continentList.add(nContnent);
                }
            }
        }
        Log.v("MyListAdapter", String.valueOf(continentList.size()));
        notifyDataSetChanged();
    }

}
