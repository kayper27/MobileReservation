package com.example.mobilereservation.adapters.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.mobilereservation.R;
import com.example.mobilereservation.model.Facility;
import com.example.mobilereservation.view.dialog.FacilityDialogFragment;

import java.util.ArrayList;

public class FacilityCheckBocListAdapter  extends ArrayAdapter<Facility> implements View.OnClickListener {

    private ArrayList<Facility> facilityDataSet;

    private FragmentManager fragmentManager;
    private Context Context;

    private int ctr;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView facility_id;
        TextView facility_status;
        ImageView facility_info;
        CheckBox facility_checkBox;
    }

    public FacilityCheckBocListAdapter(ArrayList<Facility> data, int ctr, Context context, FragmentManager fragmentManager) {
        super(context, R.layout.row_facility_checkbox_item, data);
        this.ctr = ctr;
        this.facilityDataSet = data;
        this.Context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Facility facility = (Facility)object;
        ViewGroup row  = (ViewGroup) v.getParent();

        switch (v.getId())
        {
            case R.id.facility_info:
                String details = "Status: " + facility.getStatus() +
                        "\nDecription: \n" +facility.getDescription();
                FacilityDialogFragment facilityDialogFragment = FacilityDialogFragment.newInstance(facility.getFacility_id().toUpperCase(), details);
                facilityDialogFragment.show(fragmentManager, "dialog_facility");
                break;
            case R.id.facility_checkBox:
                facility.setChecked(!facility.getChecked());
                break;
        }
    }

    @Override
    public Facility getItem(int position) {
        return (Facility) facilityDataSet.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Facility facility = getItem(ctr);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_facility_checkbox_item, parent, false);
            viewHolder.facility_id = (TextView) convertView.findViewById(R.id.facility_id);
            viewHolder.facility_status = (TextView) convertView.findViewById(R.id.facility_status);
            viewHolder.facility_info = (ImageView) convertView.findViewById(R.id.facility_info);
            viewHolder.facility_checkBox = (CheckBox) convertView.findViewById(R.id.facility_checkBox);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(Context, (ctr > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = ctr;

        viewHolder.facility_id.setText(facility.getFacility_id());
        viewHolder.facility_status.setText(facility.getStatus());
        viewHolder.facility_info.setOnClickListener(this);
        viewHolder.facility_info.setTag(ctr);
        viewHolder.facility_checkBox.setOnClickListener(this);
        viewHolder.facility_checkBox.setTag(ctr);
        // Return the facility_checkBox view to render on screen
        return convertView;
    }
}
