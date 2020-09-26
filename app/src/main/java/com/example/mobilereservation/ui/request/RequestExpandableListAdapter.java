package com.example.mobilereservation.ui.request;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.mobilereservation.R;
import com.example.mobilereservation.ui.listAdapter.RequestListAdapter;
import com.example.mobilereservation.ui.model.RequestModel;

import java.util.HashMap;
import java.util.List;

public class RequestExpandableListAdapter extends BaseExpandableListAdapter {

    private RequestListAdapter adapter;

    private FragmentManager fragmentManager;
    private Context context;

    private List<String> expandableListTitle;
    private HashMap<String, List<RequestModel>> expandableListDetail;

    public RequestExpandableListAdapter(Context context, FragmentManager fragmentManager, List<String> expandableListTitle, HashMap<String, List<RequestModel>> expandableListDetail) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition));
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        adapter = new RequestListAdapter( this.expandableListDetail.get(this.expandableListTitle.get(listPosition)) , expandedListPosition, context, fragmentManager);
        ListView expandedListTextView = (ListView) convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setAdapter(adapter);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
