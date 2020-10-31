package com.example.mobilereservation.adapters.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mobilereservation.R;
import com.example.mobilereservation.model.Equipment;

import java.util.List;

public class ReservationEquipmentListAdapter extends ArrayAdapter<Equipment> {

    private List<Equipment> equipmentDataSet;

    private static class ViewHolder {
        TextView reservation_equipment;
    }

    public ReservationEquipmentListAdapter(List<Equipment> data, Context context) {
        super(context, R.layout.row_reservation_equipment, data);
        this.equipmentDataSet = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Equipment equipmentData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if(convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_reservation_equipment, parent, false);
            viewHolder.reservation_equipment = (TextView) convertView.findViewById(R.id.reservation_equipment);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.reservation_equipment.setText(equipmentData.getType());

        return convertView;
    }
}
