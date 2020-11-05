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
import com.example.mobilereservation.model.Request;
import com.example.mobilereservation.util.FormatDateTime;
import com.example.mobilereservation.view.dialog.RequestDialogFragment;
import com.example.mobilereservation.view.toReturn.ToReturnBottomFragment;

import java.util.List;

public class RequestListAdapter extends ArrayAdapter<Request> implements View.OnClickListener{

    private static final String TAG = "RequestListAdapter";
    private final boolean cancelable;
    private final boolean delete;
    private final boolean acceptable;

    private RequestDialogFragment equipmentDialogFragment;

    private FormatDateTime dateTime = new FormatDateTime();// FOR FORMATTING DATE

    private boolean swipe;

    private long mLastClickTime = 0;
    private long THRESHOLD = 1000; // ms threshold

    private FragmentManager fragmentManager;
    private Context context;
    private List<Request> requestDataSet;
    private int ctr;

    private static class ViewHolder {
        TextView request_status;
        TextView request_id;
        TextView request_startAt;
        TextView request_endAt;
        TextView request_facility;
        TextView request_equipment;
        ImageView request_info;
        ImageView request_approve;
        ImageView request_trash;
        ImageView request_cancel;

    }

    public RequestListAdapter(List<Request> data, int ctr, Context context, FragmentManager fragmentManager, boolean acceptable, boolean cancelable, boolean delete) {
        super(context, R.layout.row_request_swipe_item, data);
        this.context = context;
        this.ctr = ctr;
        this.requestDataSet = data;
        this.fragmentManager = fragmentManager;
        this.cancelable = cancelable;
        this.delete = delete;
        this.acceptable = acceptable;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Request requestDataModel = (Request)object;
        switch (v.getId())
        {
            case R.id.request_info:
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                String details = "Status: " +requestDataModel.getStatus() +
                                "\nUsername: " +requestDataModel.getUsername() +
                                "\nStart:\n" +dateTime.formatDateTime(requestDataModel.getStartAt())+
                                "\nEnd:\n" +dateTime.formatDateTime(requestDataModel.getEndAt()) +
                                "\nFacility: " +requestDataModel.getFacility() +
                                "\nEquipments: \n" +requestDataModel.getEquipment_id();
                RequestDialogFragment equipmentDialogFragment = RequestDialogFragment.newInstance(requestDataModel.getRequest_id(), details);
                equipmentDialogFragment.show(fragmentManager, "dialog_equipment");
                break;

            case R.id.request_approve:
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(requestDataModel.getStatus().equals("Accepted")){
                    ToReturnBottomFragment toReturnBottomFragment = ToReturnBottomFragment.newInstance(requestDataModel);
                    toReturnBottomFragment.show(fragmentManager,"TAG");
                }
                break;

            case R.id.request_trash:
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                equipmentDialogFragment = RequestDialogFragment.newInstance(requestDataModel.getRequest_id(), "TRASH YOU SHIT");
                equipmentDialogFragment.show(fragmentManager, "dialog_equipment");
                break;

            case R.id.request_cancel:
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                equipmentDialogFragment = RequestDialogFragment.newInstance(requestDataModel.getRequest_id(), "CANCEL YOU SHIT");
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

            convertView = inflater.inflate(R.layout.row_request_swipe_item, parent, false);

            viewHolder.request_id = (TextView) convertView.findViewById(R.id.request_id);
            viewHolder.request_status = (TextView) convertView.findViewById(R.id.request_status);
            viewHolder.request_startAt = (TextView) convertView.findViewById(R.id.request_startAt);
            viewHolder.request_endAt = (TextView) convertView.findViewById(R.id.request_endAt);
            viewHolder.request_facility = (TextView) convertView.findViewById(R.id.request_facility);
            viewHolder.request_equipment = (TextView) convertView.findViewById(R.id.request_equipment);
            viewHolder.request_info = (ImageView) convertView.findViewById(R.id.request_info);
            viewHolder.request_approve = (ImageView) convertView.findViewById(R.id.request_approve);
            viewHolder.request_trash = (ImageView) convertView.findViewById(R.id.request_trash);
            viewHolder.request_cancel = (ImageView) convertView.findViewById(R.id.request_cancel);
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

        if(acceptable){
            viewHolder.request_approve.setEnabled(true);
            viewHolder.request_approve.setOnClickListener(this);
            viewHolder.request_approve.setTag(ctr);
        }
        else {
            viewHolder.request_approve.setVisibility(View.GONE);
            viewHolder.request_approve.setEnabled(false);
        }

        if(delete){
            viewHolder.request_trash.setEnabled(true);
            viewHolder.request_trash.setOnClickListener(this);
            viewHolder.request_trash.setTag(ctr);
        }
        else {
            viewHolder.request_trash.setVisibility(View.GONE);
            viewHolder.request_trash.setEnabled(false);
        }

        if(cancelable && requestDataSet.getStatus().equals("Pending")){
            viewHolder.request_cancel.setEnabled(true);
            viewHolder.request_cancel.setOnClickListener(this);
            viewHolder.request_cancel.setTag(ctr);
        }
        else{
            viewHolder.request_cancel.setVisibility(View.GONE);
            viewHolder.request_cancel.setEnabled(false);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
