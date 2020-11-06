package com.example.mobilereservation.adapters.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilereservation.R;

import java.util.ArrayList;

public class ToReturnEquipmentListAdapter extends ArrayAdapter<String> implements RadioGroup.OnCheckedChangeListener {

    private final Context context;

    private static class ViewHolder {
        TextView toReturn_equipment_id;
        RadioGroup toReturn_equipment_status;
    }

    public ToReturnEquipmentListAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.row_to_return_item, data);
        this.context = context;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int radioGroup = group.getCheckedRadioButtonId();
        if (radioGroup != -1) {
            RadioButton answer = group.findViewById(checkedId);
            Toast.makeText(context, answer.getText(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String equipmentData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_to_return_item, parent, false);
            viewHolder.toReturn_equipment_id = (TextView) convertView.findViewById(R.id.toReturn_equipment_id);
            viewHolder.toReturn_equipment_status = (RadioGroup) convertView.findViewById(R.id.toReturn_equipment_status);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.toReturn_equipment_id.setText(equipmentData);
        viewHolder.toReturn_equipment_status.setOnCheckedChangeListener(this);
        // Return the completed view to render on screen
        return convertView;
    }
}
