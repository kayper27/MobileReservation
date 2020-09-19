package com.example.mobilereservation.ui.equipment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EquipmentExpandableListDataPump {
    public static HashMap<String, List<EquipmentModel>> getData() {

        HashMap<String, List<EquipmentModel>> expandableListDetail = new HashMap<String, List<EquipmentModel>>();

        ArrayList<EquipmentModel> projector = new ArrayList<>();
        projector.add(new EquipmentModel("projector0","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 0"));
        projector.add(new EquipmentModel("projector1","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 1"));
        projector.add(new EquipmentModel("projector2","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 2"));
        projector.add(new EquipmentModel("projector3","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 3"));
        projector.add(new EquipmentModel("projector4","Equipment", "Available", "EPSON", "x0000", "Projector", "This is projector data hard data 4"));

        ArrayList<EquipmentModel> computer = new ArrayList<>();
        computer.add(new EquipmentModel("computer0","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 0"));
        computer.add(new EquipmentModel("computer1","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 1"));
        computer.add(new EquipmentModel("computer2","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 2"));
        computer.add(new EquipmentModel("computer3","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 3"));
        computer.add(new EquipmentModel("computer4","Equipment", "Available", "MSI", "x0000", "Projector", "This is computer data hard data 4"));

        ArrayList<EquipmentModel> mouse = new ArrayList<>();
        mouse.add(new EquipmentModel("mouse0","Equipment", "Available", "Logitech", "x0000", "Projector", "This is projector data hard data 0"));
        mouse.add(new EquipmentModel("mouse1","Equipment", "Available", "Logitech", "x0000", "Projector", "This is projector data hard data 1"));
        mouse.add(new EquipmentModel("mouse2","Equipment", "Available", "Logitech", "x0000", "Projector", "This is projector data hard data 2"));
        mouse.add(new EquipmentModel("mouse3","Equipment", "Available", "Logitech", "x0000", "Projector", "This is projector data hard data 3"));
        mouse.add(new EquipmentModel("mouse4","Equipment", "Available", "Logitech", "x0000", "Projector", "This is projector data hard data 4"));

        expandableListDetail.put("PROJECTORS", projector);
        expandableListDetail.put("COMPUTERS", computer);
        expandableListDetail.put("MOUSES", mouse);
        return expandableListDetail;
    }
}
