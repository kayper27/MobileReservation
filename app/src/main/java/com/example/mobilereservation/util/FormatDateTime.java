package com.example.mobilereservation.util;

public class FormatDateTime {

    public String formatDateTime(String dateTime){
        dateTime = dateTime.replace("T", " ");
        dateTime = dateTime.replace("Z", " ");
        return dateTime;
    }
}
