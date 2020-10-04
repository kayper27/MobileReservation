package com.example.mobilereservation.model;

public class Equipment {
    private String equipment_id;
    private String status;
    private String category;
    private String brand;
    private String model_no;
    private String type;
    private String description;

    public Equipment(String equipment_id, String status, String category, String brand, String model_no, String type, String description) {
        this.equipment_id = equipment_id;
        this.status = status;
        this.category = category;
        this.brand = brand;
        this.model_no = model_no;
        this.type = type;
        this.description = description;
    }

    public String getEquipment_id(){
        return equipment_id;
    }

    public String getStatus(){
        return status;
    }

    public String getCategory(){
        return category;
    }

    public String getBrand(){
        return brand;
    }

    public String getModel_no(){
        return model_no;
    }

    public String getType(){
        return type;
    }

    public String getDescription(){
        return description;
    }
}
