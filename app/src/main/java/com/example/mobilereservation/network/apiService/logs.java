package com.example.mobilereservation.network.apiService;

import com.example.mobilereservation.model.LoggedInUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface logs {

    @Headers({"Access-Control-Allow-Methods: POST"})
    @POST("login_logs/")
    Call<LoggedInUser> createLogs(@Body LoggedInUser logs);
}
