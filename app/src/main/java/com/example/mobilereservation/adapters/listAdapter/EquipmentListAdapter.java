package com.example.mobilereservation.adapters.listAdapter;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.mobilereservation.R;
import com.example.mobilereservation.view.dialog.EquipmentDialogFragment;
import com.example.mobilereservation.model.Equipment;

import java.util.List;

public class EquipmentListAdapter extends ArrayAdapter<Equipment> implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private List<Equipment> equipmentDataSet;
    private int ctr;

    private long mLastClickTime = 0;
    private long THRESHOLD = 1000; // ms threshold

    private static class ViewHolder {
        TextView equipment_id;
        TextView equipment_status;
        ImageView equipment_info;
    }

    public EquipmentListAdapter(List<Equipment> data, int ctr, Context context, FragmentManager fragmentManager) {
        super(context, R.layout.row_equipment_item, data);
        this.ctr = ctr;
        this.equipmentDataSet = data;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Equipment equipmentDataModel = (Equipment)object;
        switch (v.getId())
        {
            case R.id.equipment_info:
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                String details = "Status: " + equipmentDataModel.getStatus() +
                                "\nType: " + equipmentDataModel.getType()+
                                "\nBrand: "+ equipmentDataModel.getBrand() +
                                "\nModel No: "+ equipmentDataModel.getModel_no() +
                                "\nDecription: \n" +equipmentDataModel.getDescription();
                EquipmentDialogFragment equipmentDialogFragment = EquipmentDialogFragment.newInstance(equipmentDataModel.getEquipment_id().toUpperCase(), details);
                equipmentDialogFragment.show(fragmentManager, "dialog_equipment");
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
            convertView = inflater.inflate(R.layout.row_equipment_item, parent, false);
            viewHolder.equipment_id = (TextView) convertView.findViewById(R.id.equipment_id);
            viewHolder.equipment_status = (TextView) convertView.findViewById(R.id.equipment_status);
            viewHolder.equipment_info = (ImageView) convertView.findViewById(R.id.equipment_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.equipment_id.setText(equipmentDataSet.getEquipment_id());
        viewHolder.equipment_status.setText(equipmentDataSet.getStatus());
        viewHolder.equipment_info.setOnClickListener(this);
        viewHolder.equipment_info.setTag(ctr);
        // Return the completed view to render on screen
        return convertView;
    }
}
