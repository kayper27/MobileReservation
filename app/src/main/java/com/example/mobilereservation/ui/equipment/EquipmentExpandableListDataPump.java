package com.example.mobilereservation.ui.equipment;

import com.example.mobilereservation.network.model.Equipment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EquipmentExpandableListDataPump {
    public static HashMap<String, List<Equipment>> getData() {

        HashMap<String, List<Equipment>> expandableListDetail = new HashMap<String, List<Equipment>>();

        ArrayList<Equipment> projector = new ArrayList<>();
        projector.add(new Equipment("projector0","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 0"));
        projector.add(new Equipment("projector1","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 1"));
        projector.add(new Equipment("projector2","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 2"));
        projector.add(new Equipment("projector3","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 3"));
        projector.add(new Equipment("projector4","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 4"));

        ArrayList<Equipment> computer = new ArrayList<>();
        computer.add(new Equipment("computer0","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 0"));
        computer.add(new Equipment("computer1","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 1"));
        computer.add(new Equipment("computer2","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 2"));
        computer.add(new Equipment("computer3","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 3"));
        computer.add(new Equipment("computer4","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 4"));

        ArrayList<Equipment> mouse = new ArrayList<>();
        mouse.add(new Equipment("mouse0","Equipment", "Available", "Logitech", "x0000", "Projector", "This is mouse data hard data 0"));
        mouse.add(new Equipment("mouse1","Equipment", "Available", "Logitech", "x0000", "Projector", "This is mouse data hard data 1"));
        mouse.add(new Equipment("mouse2","Equipment", "Available", "Logitech", "x0000", "Projector", "This is mouse data hard data 2"));
        mouse.add(new Equipment("mouse3","Equipment", "Available", "Logitech", "x0000", "Projector", "This is mouse data hard data 3"));
        mouse.add(new Equipment("mouse4","Equipment", "Available", "Logitech", "x0000", "Projector", "This is mouse data hard data 4"));

        expandableListDetail.put("PROJECTORS", projector);
        expandableListDetail.put("COMPUTERS", computer);
        expandableListDetail.put("MOUSES", mouse);
        return expandableListDetail;
    }
}
