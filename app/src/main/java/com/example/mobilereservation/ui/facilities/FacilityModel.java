package com.example.mobilereservation.ui.facilities;

public class FacilityModel {

    private String facility_id;
    private String type;
    private String status;
    private String description;


    public FacilityModel(String facility_id, String type, String status, String description) {
        this.facility_id = facility_id;
        this.type = type;
        this.status = status;
        this.description = description;
    }

    public String getFacility_id() {
        return facility_id;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

}