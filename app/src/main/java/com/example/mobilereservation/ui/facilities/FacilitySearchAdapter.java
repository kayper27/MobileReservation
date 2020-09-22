package com.example.mobilereservation.ui.facilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.example.mobilereservation.R;
import com.example.mobilereservation.databinding.FacilitySearchListBinding;

import java.util.ArrayList;

public class FacilitySearchAdapter extends BaseAdapter implements Filterable {

    private FragmentManager fragmentManager;
    private Context context;

    private FacilitySearchListBinding facilityRowBinding;
    private FacilityListAdapter adapter;
    private ArrayList<FacilityModel> fStringFilterList;
    private ArrayList<FacilityModel> fData;
    private ValueFilter valueFilter;
    private LayoutInflater inflater;

    public FacilitySearchAdapter(Context context, FragmentManager fragmentManager, ArrayList<FacilityModel> cancel_type) {
        this.fragmentManager = fragmentManager;
        this.context = context;
        fData = cancel_type;
        fStringFilterList = cancel_type;
    }

    @Override
    public int getCount() {
        return fData.size();
    }

    @Override
    public String getItem(int position) {
        return fData.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        adapter = new FacilityListAdapter(fData, position, parent.getContext().getApplicationContext(), fragmentManager) ;
        facilityRowBinding = DataBindingUtil.inflate(inflater, R.layout.facility_search_list, parent, false);
        ListView listView = facilityRowBinding.getRoot().findViewById(R.id.facility_search_List);
        listView.setAdapter(adapter);
        return facilityRowBinding.getRoot();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<FacilityModel> filterList = new ArrayList<>();
                for (int i = 0; i < fStringFilterList.size(); i++) {
                    if ((fStringFilterList.get(i).getFacility_id().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(fStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = fStringFilterList.size();
                results.values = fStringFilterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            fData = (ArrayList<FacilityModel>) results.values;
            notifyDataSetChanged();
        }
    }

}
