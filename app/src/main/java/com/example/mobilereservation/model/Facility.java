package com.example.mobilereservation.model;

public class Facility {

    private String facility_id;
    private String category;
    private String status;
    private String description;


    public Facility(String facility_id, String category, String status, String description) {
        this.facility_id = facility_id;
        this.category = category;
        this.status = status;
        this.description = description;
    }

    public String getFacility_id() {
        return facility_id;
    }

    public String getCategory(){
        return category;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

}