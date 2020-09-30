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
import com.example.mobilereservation.ui.request.RequestDialogFragment;
import com.example.mobilereservation.network.model.Request;

import java.util.List;

public class RequestListAdapter extends ArrayAdapter<Request> implements View.OnClickListener{

    private FragmentManager fragmentManager;
    private List<Request> requestDataSet;
    private int ctr;

    private static class ViewHolder {
        TextView request_status;
        TextView request_id;
        TextView request_startAt;
        TextView request_endAt;
        ImageView request_info;
    }

    public RequestListAdapter(List<Request> data, int ctr, Context context, FragmentManager fragmentManager) {
        super(context, R.layout.request_row_item, data);
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
                                "\nStart:\n" +requestDataModel.getStartAt() +
                                "\nEnd:\n" +requestDataModel.getEndAt() +
                                "\nFacility: " +requestDataModel.getFacility() +
                                "\nEqupment/s:  " +requestDataModel.getEquipments()[0];
                RequestDialogFragment equipmentDialogFragment = RequestDialogFragment.newInstance(requestDataModel.getRequest_id(), details);
                equipmentDialogFragment.show(fragmentManager, "equipment_details");
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
            convertView = inflater.inflate(R.layout.request_row_item, parent, false);
            viewHolder.request_id = (TextView) convertView.findViewById(R.id.request_id);
            viewHolder.request_status = (TextView) convertView.findViewById(R.id.request_status);
            viewHolder.request_startAt = (TextView) convertView.findViewById(R.id.request_startAt);
            viewHolder.request_endAt = (TextView) convertView.findViewById(R.id.request_endAt);
            viewHolder.request_info = (ImageView) convertView.findViewById(R.id.request_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.request_id.setText("ID: "+requestDataSet.getRequest_id());
        viewHolder.request_status.setText(requestDataSet.getStatus());
        viewHolder.request_startAt.setText("Start: "+requestDataSet.getStartAt());
        viewHolder.request_endAt.setText("End: "+requestDataSet.getEndAt());
        viewHolder.request_info.setOnClickListener(this);
        viewHolder.request_info.setTag(ctr);
        // Return the completed view to render on screen
        return convertView;
    }
}
