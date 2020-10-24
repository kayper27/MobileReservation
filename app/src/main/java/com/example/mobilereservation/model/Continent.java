package com.example.mobilereservation.model;

import java.util.ArrayList;

public class Continent {

    private String name = "";
    private ArrayList<Country> countriesList;

    public Continent(String name, ArrayList<Country> countriesList) {
        this.name = name;
        this.countriesList = countriesList;
    }

    public String getName () {
        return name;
    }

    public void setName(String code) {
        this.name = code;
    }

    public ArrayList<Country> getCountriesList() {
        return countriesList;
    }

    public void setCountriesList(ArrayList<Country> countriesList) {
        this.countriesList = countriesList;
    }

}
