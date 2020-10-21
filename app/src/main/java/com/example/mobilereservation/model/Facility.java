package com.example.mobilereservation.model;

import androidx.annotation.Nullable;

public class Facility {

    private String facility_id;
    private String category;
    private String status;
    private String description;
    private Boolean checked;


    public Facility(String facility_id, String category, String status, String description, @Nullable Boolean checked) {
        this.facility_id = facility_id;
        this.category = category;
        this.status = status;
        this.description = description;
        this.checked = checked;
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

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean change){
        this.checked = change;
    }

}