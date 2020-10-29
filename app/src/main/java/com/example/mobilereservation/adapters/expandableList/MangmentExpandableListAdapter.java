package com.example.mobilereservation.adapters.expandableList;

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
import com.example.mobilereservation.adapters.listAdapter.RequestListAdapter;
import com.example.mobilereservation.model.Request;

import java.util.HashMap;
import java.util.List;

public class MangmentExpandableListAdapter extends BaseExpandableListAdapter {

    private RequestListAdapter adapter;

    private FragmentManager fragmentManager;
    private Context context;

    private List<String> expandableListTitle;
    private HashMap<String, List<Request>> expandableListDetail;

    public MangmentExpandableListAdapter(Context context, FragmentManager fragmentManager, List<String> expandableListTitle, HashMap<String, List<Request>> expandableListDetail) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(groupPosition));
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
        String listTitle = (String) getGroup(groupPosition);
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        adapter = new RequestListAdapter(this.expandableListDetail.get(this.expandableListTitle.get(groupPosition)) , childPosition, context, fragmentManager);
        ListView expandedListTextView = (ListView) convertView.findViewById(R.id.expandedListItem);

        expandedListTextView.setAdapter(adapter);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
