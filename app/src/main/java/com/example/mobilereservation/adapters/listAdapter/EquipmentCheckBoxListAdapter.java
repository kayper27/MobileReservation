package com.example.mobilereservation.adapters.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.mobilereservation.R;
import com.example.mobilereservation.model.Equipment;
import com.example.mobilereservation.view.dialog.EquipmentDialogFragment;

import java.util.List;

public class EquipmentCheckBoxListAdapter extends ArrayAdapter<Equipment> implements View.OnClickListener {
    private FragmentManager fragmentManager;
    private List<Equipment> equipmentDataSet;
    private int ctr;

    private static class ViewHolder {
        TextView equipment_type;
        TextView equipment_status;
        ImageView equipment_info;
        CheckBox equipment_checkBox;
    }

    public EquipmentCheckBoxListAdapter(List<Equipment> data, int ctr, Context context, FragmentManager fragmentManager) {
        super(context, R.layout.row_equipment_checkbox_item, data);
        this.ctr = ctr;
        this.equipmentDataSet = data;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Equipment equipment = (Equipment)object;
        switch (v.getId())
        {
            case R.id.equipment_info:
                String details = "Status: " + equipment.getStatus() +
                        "\nType: " + equipment.getType()+
                        "\nBrand: "+ equipment.getBrand() +
                        "\nModel No: "+ equipment.getModel_no() +
                        "\nDecription: \n" +equipment.getDescription();
                EquipmentDialogFragment equipmentDialogFragment = EquipmentDialogFragment.newInstance(equipment.getEquipment_id().toUpperCase(), details);
                equipmentDialogFragment.show(fragmentManager, "dialog_equipment");
                break;
            case R.id.equipment_checkBox:
                equipment.setChecked(!equipment.getChecked());
                break;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Equipment equipmentDataSet = getItem(ctr);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_equipment_checkbox_item, parent, false);
            viewHolder.equipment_type = (TextView) convertView.findViewById(R.id.equipment_type);
            viewHolder.equipment_status = (TextView) convertView.findViewById(R.id.equipment_status);
            viewHolder.equipment_info = (ImageView) convertView.findViewById(R.id.equipment_info);
            viewHolder.equipment_checkBox = (CheckBox) convertView.findViewById(R.id.equipment_checkBox);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.equipment_type.setText(equipmentDataSet.getType());
        viewHolder.equipment_status.setText(equipmentDataSet.getStatus());
        viewHolder.equipment_info.setOnClickListener(this);
        viewHolder.equipment_info.setTag(ctr);
        viewHolder.equipment_checkBox.setOnClickListener(this);
        viewHolder.equipment_checkBox.setChecked(equipmentDataSet.getChecked());
        viewHolder.equipment_checkBox.setTag(ctr);
        // Return the completed view to render on screen
        return convertView;
    }
}
