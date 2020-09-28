package com.example.mobilereservation.network.apiService;

import com.example.mobilereservation.network.model.Facility;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface equipment {
    // Fetch all Facility
    @GET("equipment_management")
    Single<List<Facility>> getEqupments();
}
