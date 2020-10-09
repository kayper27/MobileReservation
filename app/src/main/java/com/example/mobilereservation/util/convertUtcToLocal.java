package com.example.mobilereservation.util;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class convertUtcToLocal {
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String convertUtcDateTime(String dateTime) {
        OffsetDateTime parsed = LocalDateTime.parse(dateTime.replace("Z", "")).atOffset(ZoneOffset.UTC);
        ZoneId zone = ZoneId.of("Asia/Manila");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
        return outputFormatter.format(parsed.atZoneSameInstant(zone));
    }

    public String formatDateTime(String dateTime){
        return dateTime;
    }

}