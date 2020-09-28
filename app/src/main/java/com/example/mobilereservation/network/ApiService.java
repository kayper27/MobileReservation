package com.example.mobilereservation.network;

import com.example.mobilereservation.network.model.FacilityModel;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ApiService {
    // Fetch all Facility
    @GET("facility_management")
    Single<List<FacilityModel>> getfacilities();
}
