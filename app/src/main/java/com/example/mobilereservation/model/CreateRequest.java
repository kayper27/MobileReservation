package com.example.mobilereservation.model;

import java.io.Serializable;

public class CreateRequest implements Serializable {

    private String id;
    private String username;
    private String startAt;
    private String endAt;
    private String purpose;
    private String facility_id;
    private Equips equipment;

    public CreateRequest(String id, String username, String startAt, String endAt, String purpose, String facility_id, Equips equipment){
        this.id = id;
        this.username = username;
        this.startAt = startAt;
        this.endAt = endAt;
        this.purpose = purpose;
        this.facility_id = facility_id;
        this.equipment = new  Equips(equipment.getEquipment_id(), equipment.getEquipment_Status());
    }

}
