package com.example.mobilereservation.ui.equipment;

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

public class EquipmentListAdapter extends ArrayAdapter<EquipmentModel> implements View.OnClickListener {

    private ArrayList<EquipmentModel> equipmentDataSet;
    private Context Context;
    private int ctr;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView equipment_id;
        TextView equipment_status;
        ImageView equipment_info;
    }

    public EquipmentListAdapter(ArrayList<EquipmentModel> data, int ctr, Context context) {
        super(context, R.layout.equipment_row_item, data);
        this.ctr = ctr;
        this.equipmentDataSet = data;
        this.Context = context;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        EquipmentModel equipmentDataModel = (EquipmentModel)object;
        switch (v.getId())
        {
            case R.id.facility_info:
                Snackbar.make(v, "Type: " +equipmentDataModel.getType()+
                        "\nBrand: "+equipmentDataModel.getBrand() +
                        "\nModel No: "+ equipmentDataModel.getModel_no() +
                        "\nDecription: " +equipmentDataModel.getDescription(), Snackbar.LENGTH_LONG).setAction("No action", null).show();
                break;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        EquipmentModel equipmentDataSet = getItem(ctr);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.facility_row_item, parent, false);
            viewHolder.equipment_id = (TextView) convertView.findViewById(R.id.equipment_id);
            viewHolder.equipment_status = (TextView) convertView.findViewById(R.id.equipment_status);
            viewHolder.equipment_info = (ImageView) convertView.findViewById(R.id.equipment_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(Context, (ctr > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = ctr;

        viewHolder.equipment_id.setText(equipmentDataSet.getEquipment_id());
        viewHolder.equipment_status.setText(equipmentDataSet.getStatus());
        viewHolder.equipment_info.setOnClickListener(this);
        viewHolder.equipment_info.setTag(ctr);
        // Return the completed view to render on screen
        return convertView;
    }
}
