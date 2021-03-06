package com.example.mobilereservation.adapters.listAdapter;

import android.content.Context;
import android.os.AsyncTask;
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
import com.example.mobilereservation.network.ApiClient;
import com.example.mobilereservation.network.apiService.request;
import com.example.mobilereservation.util.FormatDateTime;
import com.example.mobilereservation.util.PrefUtils;
import com.example.mobilereservation.view.bottomFragment.ConfirmationBottomFragment;
import com.example.mobilereservation.view.dialog.ErrorDialogFragment;
import com.example.mobilereservation.view.dialog.RequestDialogFragment;
import com.example.mobilereservation.view.toReturn.ToReturnBottomFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestListAdapter extends ArrayAdapter<Request> implements View.OnClickListener{

    private static final String TAG = "RequestListAdapter";
    private final boolean cancelable;
    private final boolean denied;
    private final boolean acceptable;

    private RequestDialogFragment equipmentDialogFragment;

    private FormatDateTime dateTime = new FormatDateTime();// FOR FORMATTING DATE

    private long mLastClickTime = 0;
    private long THRESHOLD = 6000; // ms threshold
    private long INFO_THRESHOLD = 1000; // ms threshold

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
        ImageView request_denied;
        ImageView request_cancel;

    }

    public RequestListAdapter(List<Request> data, int ctr, Context context, FragmentManager fragmentManager, boolean acceptable, boolean cancelable, boolean denied) {
        super(context, R.layout.row_request_swipe_item, data);
        this.context = context;
        this.ctr = ctr;
        this.requestDataSet = data;
        this.fragmentManager = fragmentManager;
        this.cancelable = cancelable;
        this.denied = denied;
        this.acceptable = acceptable;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object= getItem(position);
        final Request requestDataModel = (Request)object;

        requestDataModel.setIdModerator(PrefUtils.getUserLogID(context));
        switch (v.getId()) {
            case R.id.request_info:
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < INFO_THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                String details = "Status: " +requestDataModel.getStatus() +
                                "\nUsername: " +requestDataModel.getUsername() +
                                "\nStart:\n" +dateTime.formatDateTime(requestDataModel.getStartAt())+
                                "\nEnd:\n" +dateTime.formatDateTime(requestDataModel.getEndAt()) +
                                "\nFacility: " +requestDataModel.getFacility() +
                                "\nEquipments: \n" +requestDataModel.getEquipment().getEquipment_id() +
                                "\nPurpose:\n" +requestDataModel.getPurpose();
                RequestDialogFragment equipmentDialogFragment = RequestDialogFragment.newInstance(requestDataModel.getRequest_id(), details);
                equipmentDialogFragment.show(fragmentManager, "dialog_equipment");
                break;

            case R.id.request_approve:
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < INFO_THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                
                if(requestDataModel.getStatus().equals("Accepted")){ // WHEN REQUEST IS DONE WITH ITS FLOW PENDING ->  ACCEPTED -> FINISHED
                    ToReturnBottomFragment toReturnBottomFragment = ToReturnBottomFragment.newInstance(requestDataModel);
                    toReturnBottomFragment.show(fragmentManager,"TAG");
                    requestDataSet.remove(object);
                }
                if(requestDataModel.getStatus().equals("Pending")){
                    requestDataModel.setStatus("Accepted");// USER TRASH ITS REQUEST IS PENDING -> ACCEPTED
                    RequestAcceptedAsyncTask asyncTask = new RequestAcceptedAsyncTask(requestDataModel.getRequest_id(), requestDataModel);
                    asyncTask.execute();
//                    requestDataSet.remove(object);
                }
                break;

            case R.id.request_denied:
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if(requestDataModel.getStatus().equals("Pending")){
                    requestDataModel.setStatus("Denied");// WHEN REQUEST WAS DENIED  PENDING -> DENIED

                    List<String> EquipmentStatuses = new ArrayList<>();
                    for(int i = 0; i < requestDataModel.getEquipment().getEquipment_Status().size(); i++){
                        EquipmentStatuses.add("Denied");
                    }
                    requestDataModel.getEquipment().setEquipment_Status(EquipmentStatuses);

                    ConfirmationBottomFragment confirmationBottomFragment = ConfirmationBottomFragment.newInstance(requestDataModel, true);
                    confirmationBottomFragment.show(fragmentManager,"TAG");

//                    requestDataSet.remove(object);
                }
                break;

            case R.id.request_cancel:
                // mis-clicking prevention, using threshold
                if (SystemClock.elapsedRealtime() - mLastClickTime < THRESHOLD){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                if(requestDataModel.getStatus().equals("Pending")){
                    requestDataModel.setStatus("Canceled"); // USER TRASH ITS REQUEST NOW STATUS TO PENDING -> CANCELED

                    List<String> EquipmentStatuses = new ArrayList<>();
                    for(int i = 0; i < requestDataModel.getEquipment().getEquipment_Status().size(); i++){
                        EquipmentStatuses.add("Canceled");
                    }
                    requestDataModel.getEquipment().setEquipment_Status(EquipmentStatuses);

                    ConfirmationBottomFragment confirmationBottomFragment = ConfirmationBottomFragment.newInstance(requestDataModel, false);
                    confirmationBottomFragment.show(fragmentManager,"TAG");

//                    requestDataSet.remove(object);
                }
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
            viewHolder.request_denied = (ImageView) convertView.findViewById(R.id.request_denied);
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
        viewHolder.request_equipment.setText("Equipment: "+requestDataSet.getEquipment().getEquipment_id());
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

        if(denied){
            viewHolder.request_denied.setEnabled(true);
            viewHolder.request_denied.setOnClickListener(this);
            viewHolder.request_denied.setTag(ctr);
        }
        else {
            viewHolder.request_denied.setVisibility(View.GONE);
            viewHolder.request_denied.setEnabled(false);
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

    private class RequestStatusAsyncTask extends AsyncTask<Void, Void, Void> {

        private Request request;
        private String request_id;

        RequestStatusAsyncTask(String request_id, Request request){
            this.request_id = request_id;
            this.request = request;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            request api = ApiClient.getClient(context).create(request.class);
            Call<Request> call = api.updateRequest(request_id, request);
            call.enqueue(new Callback<Request>() {
                @Override
                public void onResponse(Call<Request> call, Response<Request> response) {
                    if(response.code() == 201 || response.code() == 200){
                        RequestDialogFragment requestDialogFragment = RequestDialogFragment.newInstance("Successful", response+"\nRequest was successfully Updated\n");
                        requestDialogFragment.show(fragmentManager, "dialog_request");
                    }
                    else{
                        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", response.code()+" "+response.message());
                        errorDialogFragment.show(fragmentManager, "dialog_error");
                    }
                }
                @Override
                public void onFailure(Call<Request> call, Throwable t) {
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", t.getCause()+"\n=========\n"+t.getMessage());
                    errorDialogFragment.show(fragmentManager, "dialog_error");
                }

            });

            return null;
        }
        @Override
        protected void onPostExecute(Void v){}

        @Override
        protected void onPreExecute() {}
    }

    private class RequestAcceptedAsyncTask extends AsyncTask<Void, Void, Void> {

        private Request request;
        private String request_id;

        RequestAcceptedAsyncTask(String request_id, Request request){
            this.request_id = request_id;
            this.request = request;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            request api = ApiClient.getClient(context).create(request.class);
            Call<Request> call = api.updateRequest_Accepted(request_id, request.getStartAt(), request.getEndAt(), request);
            call.enqueue(new Callback<Request>() {
                @Override
                public void onResponse(Call<Request> call, Response<Request> response) {
                    if(response.code() == 201 || response.code() == 200){
                        RequestDialogFragment requestDialogFragment = RequestDialogFragment.newInstance("Successful", response+"\nRequest was successfully Updated\n");
                        requestDialogFragment.show(fragmentManager, "dialog_request");
                    }
                    else{
                        ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", response.code()+" "+response.message());
                        errorDialogFragment.show(fragmentManager, "dialog_error");
                    }
                }
                @Override
                public void onFailure(Call<Request> call, Throwable t) {
                    ErrorDialogFragment errorDialogFragment = ErrorDialogFragment.newInstance("Error", t.getCause()+"\n=========\n"+t.getMessage());
                    errorDialogFragment.show(fragmentManager, "dialog_error");
                }

            });

            return null;
        }
        @Override
        protected void onPostExecute(Void v){}

        @Override
        protected void onPreExecute() {}
    }
    
}
