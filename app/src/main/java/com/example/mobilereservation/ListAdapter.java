package com.example.mobilereservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;

import com.example.mobilereservation.databinding.SearchRowItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter implements Filterable {

    List<String> mData;
    List<String> mStringFilterList;
    private ValueFilter valueFilter;
    private LayoutInflater inflater;

    public ListAdapter(List<String> cancel_type) {
        mData=cancel_type;
        mStringFilterList = cancel_type;
    }

    @Override
    public int getCount() {
        System.out.println("Test Ctr:L "+mData.size());
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        System.out.println("Test Item:L "+mData.get(position));
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        System.out.println("Test Pos:L "+position);
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        System.out.println("Test Get:Pos:L "+position);
        System.out.println("Test Get:CnV:L "+convertView);
        System.out.println("Test Get:Par:L "+parent);
        System.out.println("Test Get:Inf:L "+inflater);
        if (inflater == null) {
            inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        SearchRowItemBinding rowItemBinding = DataBindingUtil.inflate(inflater, R.layout.search_row_item, parent, false);
        rowItemBinding.stringName.setText(String.valueOf(mData.get(position)));

        return rowItemBinding.getRoot();
    }

    @Override
    public Filter getFilter() {
        System.out.println("Test Fil:L:1 "+valueFilter);
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        System.out.println("Test Fil:L:@ "+valueFilter);
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            System.out.println("Test Res:L "+results);
            if (constraint != null && constraint.length() > 0) {
                List<String> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            System.out.println("Test Res:Ctr:L "+results);
            System.out.println("Test Res:Val:L "+results);
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mData = (List<String>) results.values;
            System.out.println("Test Pul:L "+mData);
            notifyDataSetChanged();
        }

    }

}