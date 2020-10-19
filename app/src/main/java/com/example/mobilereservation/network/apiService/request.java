package com.example.mobilereservation.network.apiService;

import com.example.mobilereservation.model.Request;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface request {
    // Fetch all User Request
    @GET("booking_service/user/{username}")
    Single<List<Request>> getUserRequest(@Path("username") String username);

    @GET("booking_service/{start}/{end}")
    Single<List<Request>> getReservedchedule(@Path("start") String start, @Path("end") String end);
}
