package com.example.mobilereservation.network.apiService;

import com.example.mobilereservation.model.Equipment;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface equipment {
    // Fetch all Facility
    @GET("equipment")
    Single<List<Equipment>> getEquipments();

    @GET("equipment/status/available")
    Single<List<Equipment>> getAvailableEquipments();
}
