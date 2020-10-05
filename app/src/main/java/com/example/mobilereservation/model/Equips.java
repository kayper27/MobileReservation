package com.example.mobilereservation.model;

import java.util.List;

public class Equips{
    private List<String> equipment_id;
    private List<String> equipment_status;

    public Equips(List<String> equipment_id, List<String> equipment_status) {
        this.equipment_id = this.equipment_id;
        this.equipment_status = this.equipment_status;
    }

    public List<String> getEquipment_id() {
        return equipment_id;
    }

    public List<String> getEquipment_Status() {
        return equipment_status;
    }

}
