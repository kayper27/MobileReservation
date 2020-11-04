package com.example.mobilereservation.adapters.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.mobilereservation.R;
import com.example.mobilereservation.model.Request;
import com.example.mobilereservation.util.FormatDateTime;
import com.example.mobilereservation.view.dialog.RequestDialogFragment;

import java.util.List;

public class RequestListAdapter extends ArrayAdapter<Request> implements View.OnClickListener{

    private FragmentManager fragmentManager;
    private List<Request> requestDataSet;
    private int ctr;

    private FormatDateTime dateTime = new FormatDateTime();// FOR FORMATTING DATE

    private static class ViewHolder {
        TextView request_status;
        TextView request_id;
        TextView request_startAt;
        TextView request_endAt;
        TextView request_facility;
        TextView request_equipment;
        ImageView request_info;
    }

    public RequestListAdapter(List<Request> data, int ctr, Context context, FragmentManager fragmentManager) {
        super(context, R.layout.row_request_item, data);
        this.ctr = ctr;
        this.requestDataSet = data;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Request requestDataModel = (Request)object;
        switch (v.getId())
        {
            case R.id.request_info:
                String details = "Status: " +requestDataModel.getStatus() +
                                "\nUsername: " +requestDataModel.getUsername() +
                                "\nStart:\n" +dateTime.formatDateTime(requestDataModel.getStartAt())+
                                "\nEnd:\n" +dateTime.formatDateTime(requestDataModel.getEndAt()) +
                                "\nFacility: " +requestDataModel.getFacility() +
                                "\nEquipments: \n" +requestDataModel.getEquipment_id();
                RequestDialogFragment equipmentDialogFragment = RequestDialogFragment.newInstance(requestDataModel.getRequest_id(), details);
                equipmentDialogFragment.show(fragmentManager, "dialog_equipment");
                break;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Request requestDataSet = getItem(ctr);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_request_item, parent, false);
            viewHolder.request_id = (TextView) convertView.findViewById(R.id.request_id);
            viewHolder.request_status = (TextView) convertView.findViewById(R.id.request_status);
            viewHolder.request_startAt = (TextView) convertView.findViewById(R.id.request_startAt);
            viewHolder.request_endAt = (TextView) convertView.findViewById(R.id.request_endAt);
            viewHolder.request_facility = (TextView) convertView.findViewById(R.id.request_facility);
            viewHolder.request_equipment = (TextView) convertView.findViewById(R.id.request_equipment);
            viewHolder.request_info = (ImageView) convertView.findViewById(R.id.request_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.request_id.setText("ID: "+requestDataSet.getRequest_id());
        viewHolder.request_status.setText(requestDataSet.getStatus());
        viewHolder.request_startAt.setText("Start: "+dateTime.formatDateTime(requestDataSet.getStartAt()));
        viewHolder.request_endAt.setText("End: "+dateTime.formatDateTime(requestDataSet.getEndAt()));
        viewHolder.request_facility.setText("Facility: "+requestDataSet.getFacility());
        viewHolder.request_equipment.setText("Equipment: "+requestDataSet.getEquipment_id());
        viewHolder.request_info.setOnClickListener(this);
        viewHolder.request_info.setTag(ctr);
        // Return the completed view to render on screen
        return convertView;
    }
}
