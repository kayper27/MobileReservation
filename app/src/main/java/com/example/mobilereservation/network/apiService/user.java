package com.example.mobilereservation.network.apiService;

import com.example.mobilereservation.model.LoggedInUser;
import com.example.mobilereservation.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface user {

    @Headers({"Access-Control-Allow-Methods: POST"})
    @FormUrlEncoded
    @POST("users/login")
    Call<LoggedInUser> userLogin(@Field ("account_id") String user, @Field("password") String password);

    @Headers({"Access-Control-Allow-Methods: POST"})
    @POST("users/signup")
    Call<User> createUser(@Body User user);

}
