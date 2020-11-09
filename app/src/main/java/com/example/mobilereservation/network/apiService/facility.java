package com.example.mobilereservation.network.apiService;

import com.example.mobilereservation.model.Facility;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface facility {
    // Fetch all Facility
    @GET("facility")
    Single<List<Facility>> getFacilities();

    @GET("facility/status/available")
    Single<List<Facility>> getAvailableFacilities();
}
