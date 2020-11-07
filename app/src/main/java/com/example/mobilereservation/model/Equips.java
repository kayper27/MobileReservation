package com.example.mobilereservation.model;

import java.util.List;

public class Equips{
    private List<String> equipment_id;
    private List<String> status;

    public Equips(List<String> equipment_id, List<String> tatus) {
        this.equipment_id = equipment_id;
        this.status = tatus;
    }

    public List<String> getEquipment_id() {
        return equipment_id;
    }

    public List<String> getEquipment_Status() {
        return status;
    }

}
