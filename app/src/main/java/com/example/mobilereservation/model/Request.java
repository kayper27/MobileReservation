package com.example.mobilereservation.model;

import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {

    private String _id;
    private String status;
    private String username;
    private String startAt;
    private String endAt;
    private String facility_id;
    private Equips equipment;
    private List<String> equipment_id;
    private List<String> equipment_status;

    public Request(String _id, String status, String username, String startAt, String endAt, String facility_id, Equips equipment){
        this._id = _id;
        this.status = status;
        this.username = username;
        this.startAt = startAt;
        this.endAt = endAt;
        this.facility_id = facility_id;
        this.equipment = equipment;
        this.equipment_id = equipment.getEquipment_id();
        this.equipment_status = equipment.getEquipment_Status();
    }

    public Request(String username, String startAt, String endAt, String facility_id, Equips equipment){
        this.username = username;
        this.startAt = startAt;
        this.endAt = endAt;
        this.facility_id = facility_id;
        this.equipment = equipment;
        this.equipment_id = equipment.getEquipment_id();
        this.equipment_status = equipment.getEquipment_Status();
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

    public List<String> getEquipment_id() {
        return equipment_id;
    }

    public List<String> getEquipment_status() {
        return equipment_status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEquipment(Equips equipment) {
        this.equipment = equipment;
    }

}
