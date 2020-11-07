package com.example.mobilereservation.adapters.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilereservation.R;
import com.example.mobilereservation.model.Equips;

public class ToReturnEquipmentListAdapter extends BaseAdapter  {

    private final Context context;

    private Equips equipsSet;

    private static class ViewHolder {
        TextView toReturn_equipment_id;
        RadioGroup toReturn_equipment_status;
    }

    public ToReturnEquipmentListAdapter(Equips data, Context context) {
        this.equipsSet = data;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        String equipmentData = equipsSet.getEquipment_Status().get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_to_return_item, parent, false);
            viewHolder.toReturn_equipment_id = (TextView) convertView.findViewById(R.id.toReturn_equipment_id);
            viewHolder.toReturn_equipment_status = (RadioGroup) convertView.findViewById(R.id.toReturn_equipment_status);
            result=convertView;

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.toReturn_equipment_id.setText(equipmentData);
        viewHolder.toReturn_equipment_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioGroup = group.getCheckedRadioButtonId();

                if (radioGroup != -1) {
                    RadioButton answer = group.findViewById(checkedId);
                    Toast.makeText(context,equipsSet.getEquipment_Status().get(position)+" "+answer.getText(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public int getCount() {
        return equipsSet.getEquipment_id().size();
    }

    @Override
    public Object getItem(int position) {
        return equipsSet.getEquipment_id().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
