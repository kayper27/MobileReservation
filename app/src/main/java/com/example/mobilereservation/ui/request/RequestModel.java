package com.example.mobilereservation.ui.request;

public class RequestModel {

    private String request_id;
    private String status;
    private String username;
    private String startAt;
    private String endAt;
    private String facility;
    private String[] equipments = new String[5];
    private String[] equipmentStatus = new String[5];

    public RequestModel(String request_id, String status, String username, String startAt, String endAt, String facility, String[] equipments) {
        this.request_id = request_id;
        this.status = status;
        this.username = username;
        this.startAt = startAt;
        this.endAt = endAt;
        this.facility = facility;
        this.equipments = equipments;
    }

    public String getRequest_id() {
        return request_id;
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
        return facility;
    }

    public String[] getEquipments() {
        return equipments;
    }

}
