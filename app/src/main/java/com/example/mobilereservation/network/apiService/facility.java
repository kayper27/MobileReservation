package com.example.mobilereservation.network.apiService;

import com.example.mobilereservation.network.model.Facility;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface facility {
    // Fetch all Facility
    @GET("facility_management")
    Single<List<Facility>> getfacilities();
}
