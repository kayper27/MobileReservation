package com.example.mobilereservation.ui.facilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilereservation.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FacilityListAdapter extends ArrayAdapter<FacilityViewModel> implements View.OnClickListener {

    private ArrayList<FacilityViewModel> facilityDataSet;
    private Context Context;
    private int ctr;
    private int lastPosition = -1;
    // View lookup cache
    private static class ViewHolder {
        TextView facility_id;
        TextView facility_status;
        ImageView facility_info;
    }

    public FacilityListAdapter(ArrayList<FacilityViewModel> data, int ctr, Context context) {
        super(context, R.layout.facility_row_item, data);
        this.ctr = ctr;
        this.facilityDataSet = data;
        this.Context = context;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        FacilityViewModel facilityDataModel=(FacilityViewModel)object;
        switch (v.getId())
        {
            case R.id.facility_info:
                Snackbar.make(v, "Type: " +facilityDataModel.getType()+ "\nDecription: " +facilityDataModel.getDescription(), Snackbar.LENGTH_LONG).setAction("No action", null).show();
                break;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FacilityViewModel facilityDataModel = getItem(ctr);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.facility_row_item, parent, false);
            viewHolder.facility_id = (TextView) convertView.findViewById(R.id.facility_id);
            viewHolder.facility_status = (TextView) convertView.findViewById(R.id.facility_status);
            viewHolder.facility_info = (ImageView) convertView.findViewById(R.id.facility_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(Context, (ctr > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = ctr;

        viewHolder.facility_id.setText(facilityDataModel.getFacility_id());
        viewHolder.facility_status.setText(facilityDataModel.getStatus());
        viewHolder.facility_info.setOnClickListener(this);
        viewHolder.facility_info.setTag(ctr);
        // Return the completed view to render on screen
        return convertView;
    }
}