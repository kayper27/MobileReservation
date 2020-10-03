package com.example.mobilereservation.network.model;

public class Request {

    private String _id;
    private String status;
    private String username;
    private String startAt;
    private String endAt;
    private String facility_id;


    public Request(String _id, String status, String username, String startAt, String endAt, String facility_id) {
        this._id = _id;
        this.status = status;
        this.username = username;
        this.startAt = startAt;
        this.endAt = endAt;
        this.facility_id = facility_id;
    }

    public String getRequest_id() {
        return _id;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public String getStartAt() {
        return startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public String getFacility() {
        return facility_id;
    }



}
