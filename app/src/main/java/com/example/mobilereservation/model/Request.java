package com.example.mobilereservation.model;

import java.io.Serializable;

public class Request implements Serializable {

    private String _id;
    private String status;
    private String username;
    private String startAt;
    private String endAt;
    private String facility_id;
    private Equips equipment;
    private String dateCreated;

    public Request(String _id, String status, String username, String startAt, String endAt, String facility_id, Equips equipment, String dateCreated) {
        this._id = _id;
        this.status = status;
        this.username = username;
        this.startAt = startAt;
        this.endAt = endAt;
        this.facility_id = facility_id;
        this.equipment = new  Equips(equipment.getEquipment_id(), equipment.getEquipment_Status());
        this.dateCreated = dateCreated;
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

    public Equips getEquipment() {
        return equipment;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEquipment(Equips equipment) {
        this.equipment = equipment;
    }

    public String getDateCreated() {
        return dateCreated;
    }
}
