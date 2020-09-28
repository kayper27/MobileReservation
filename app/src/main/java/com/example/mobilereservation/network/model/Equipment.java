package com.example.mobilereservation.network.model;

public class Equipment {
    private String equipment_id;
    private String type;
    private String status;
    private String brand;
    private String model_no;
    private String equipment_type;
    private String description;

    public Equipment(String equipment_id, String type, String status, String brand, String model_no, String equipment_type, String description) {
        this.equipment_id = equipment_id;
        this.type = type;
        this.status = status;
        this.brand = brand;
        this.model_no = model_no;
        this.equipment_type = equipment_type;
        this.description = description;
    }

    public String getEquipment_id(){
        return equipment_id;
    }
    public String getType(){
        return type;
    }
    public String getStatus(){
        return status;
    }
    public String getBrand(){
        return brand;
    }
    public String getModel_no(){
        return model_no;
    }
    public String getEquipment_type(){
        return equipment_type;
    }
    public String getDescription(){
        return description;
    }
}
