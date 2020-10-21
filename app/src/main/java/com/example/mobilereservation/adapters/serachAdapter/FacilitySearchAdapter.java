package com.example.mobilereservation.adapters.serachAdapter;

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
import com.example.mobilereservation.adapters.listAdapter.FacilityCheckBocListAdapter;
import com.example.mobilereservation.adapters.listAdapter.FacilityListAdapter;
import com.example.mobilereservation.databinding.SearchFacilityListBinding;
import com.example.mobilereservation.databinding.SearchReseravtionListBinding;
import com.example.mobilereservation.model.Facility;

import java.util.ArrayList;

public class FacilitySearchAdapter extends BaseAdapter implements Filterable {

    private FragmentManager fragmentManager;
    private Context context;
    private Boolean flagCheckbox;

    private SearchReseravtionListBinding searchReseravtionListBinding;
    private SearchFacilityListBinding facilityRowBinding;
    private FacilityListAdapter adapterList;
    private FacilityCheckBocListAdapter adapterCheckBox;
    private ArrayList<Facility> fStringFilterList;
    private ArrayList<Facility> fData;
    private ValueFilter valueFilter;
    private LayoutInflater inflater;

    public FacilitySearchAdapter(Context context, FragmentManager fragmentManager, ArrayList<Facility> cancel_type, Boolean flagCheckbox) {
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.flagCheckbox = flagCheckbox;
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

        if(flagCheckbox.equals(false)){
            facilityRowBinding = DataBindingUtil.inflate(inflater, R.layout.search_facility_list, parent, false);
            ListView listView = facilityRowBinding.getRoot().findViewById(R.id.facility_search_List);
            adapterList = new FacilityListAdapter(fData, position, parent.getContext().getApplicationContext(), fragmentManager);
            listView.setAdapter(adapterList);
            return facilityRowBinding.getRoot();
        }
        else{
            searchReseravtionListBinding = DataBindingUtil.inflate(inflater, R.layout.search_reseravtion_list, parent, false);
            ListView listView = searchReseravtionListBinding.getRoot().findViewById(R.id.reservation_search_List);
            adapterCheckBox = new FacilityCheckBocListAdapter(fData, position, parent.getContext().getApplicationContext(), fragmentManager);
            listView.setAdapter(adapterCheckBox);
            return searchReseravtionListBinding.getRoot();
        }
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
                ArrayList<Facility> filterList = new ArrayList<>();
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
            fData = (ArrayList<Facility>) results.values;
            notifyDataSetChanged();
        }
    }

}
