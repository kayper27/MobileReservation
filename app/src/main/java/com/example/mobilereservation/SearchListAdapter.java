package com.example.mobilereservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;

import com.example.mobilereservation.databinding.FragmentGalleryBinding;
import com.example.mobilereservation.databinding.SearchRowItemBinding;

import java.util.ArrayList;

public class SearchListAdapter extends BaseAdapter implements Filterable {

    private Context context;
    FragmentGalleryBinding maindBinding;

    ArrayList<DataModel> mData;
    ArrayList<DataModel> mStringFilterList;
    private ValueFilter valueFilter;
    private LayoutInflater inflater;


    private static ViewGroup container;
    private static CustomAdapter adapter;

    public SearchListAdapter(Context context, ArrayList<DataModel> cancel_type) {
        this.context = context;
        mData=cancel_type;
        mStringFilterList = cancel_type;
    }

    @Override
    public int getCount() {
        System.out.println("Test Ctr:S "+mData.size());
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        System.out.println("Test Item:S "+mData.get(position));
        return mData.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        System.out.println("Test Pos:S "+position);
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        System.out.println("Test Get:Pos:S "+position);
        System.out.println("Test Get:Par:S "+parent);
        System.out.println("Test Get:Inf:S "+inflater);
        System.out.println("Test HERE :)");

        if (inflater == null) {
            inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        SearchRowItemBinding rowItemBinding = DataBindingUtil.inflate(inflater, R.layout.search_row_item, parent, false);
        ListView listView = rowItemBinding.getRoot().findViewById(R.id.saerchlist);
        adapter= new CustomAdapter(mStringFilterList, position, parent.getContext().getApplicationContext());
//        rowItemBinding.stringName.setText(String.valueOf(mData.get(position)));
        listView.setAdapter(adapter);
        return rowItemBinding.getRoot();
    }

    @Override
    public Filter getFilter() {
        System.out.println("Test Fil:S:1 "+valueFilter);
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        System.out.println("Test Fil:S:2 "+valueFilter);
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            System.out.println("Test Res:S "+ context);
            FilterResults results = new FilterResults();
            System.out.println("Test Res:S "+results);
            if (constraint != null && constraint.length() > 0) {
                ArrayList<DataModel> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            System.out.println("Test Res:Ctr:S "+results.count);
            System.out.println("Test Res:Val:S "+results.values);
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mData = (ArrayList<DataModel>) results.values;
            System.out.println("Test Pul:S "+mData);
            notifyDataSetChanged();
        }
    }

}