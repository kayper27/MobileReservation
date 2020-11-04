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
import com.example.mobilereservation.adapters.listAdapter.EquipmentCheckBoxListAdapter;
import com.example.mobilereservation.databinding.SearchReseravtionListBinding;
import com.example.mobilereservation.model.Equipment;

import java.util.ArrayList;

public class EquipmentSearchAdapter extends BaseAdapter implements Filterable {
    private FragmentManager fragmentManager;
    private Context context;
    private Boolean flagCheckbox;

    private SearchReseravtionListBinding searchReseravtionListBinding;
    private EquipmentCheckBoxListAdapter adapterCheckBox;
    private ArrayList<Equipment> eStringFilterList;
    private ArrayList<Equipment> eData;
    private ValueFilter valueFilter;
    private LayoutInflater inflater;

    public EquipmentSearchAdapter(Context context, FragmentManager fragmentManager, ArrayList<Equipment> cancel_type, Boolean flagCheckbox) {
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.flagCheckbox = flagCheckbox;
        this.eData = cancel_type;
        this.eStringFilterList = cancel_type;
    }

    @Override
    public int getCount() {
        return eData.size();
    }

    @Override
    public String getItem(int position) {
        return eData.get(position).toString();
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
        searchReseravtionListBinding = DataBindingUtil.inflate(inflater, R.layout.search_reseravtion_list, parent, false);
        ListView listView = searchReseravtionListBinding.getRoot().findViewById(R.id.reservation_search_List);

        adapterCheckBox = new EquipmentCheckBoxListAdapter(eData, position, parent.getContext().getApplicationContext(), fragmentManager);
        listView.setAdapter(adapterCheckBox);
        return searchReseravtionListBinding.getRoot();
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
                ArrayList<Equipment> filterList = new ArrayList<>();
                for (int i = 0; i < eStringFilterList.size(); i++) {
                    if ((eStringFilterList.get(i).getType().toUpperCase()).contains(constraint.toString().toUpperCase()) || eStringFilterList.get(i).getChecked()) {
                        filterList.add(eStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = eStringFilterList.size();
                results.values = eStringFilterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            eData = (ArrayList<Equipment>) results.values;
            notifyDataSetChanged();
        }
    }
}
