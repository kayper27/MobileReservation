package com.example.mobilereservation.network.apiService;

import com.example.mobilereservation.model.CreateRequest;
import com.example.mobilereservation.model.Request;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface request {

    // Fetch all User Request
    @GET("booking_service/user/{username}")
    Single<List<Request>> getUserRequest(@Path("username") String username);

    @GET("booking_service/{start}/{end}")
    Single<List<Request>> getReservedSchedule(@Path("start") String start, @Path("end") String end);

    @GET("booking_service/stat/{status}")
    Single<List<Request>> getSpecificStatus(@Path("status") String status);

    @Headers({"Access-Control-Allow-Methods: POST"})
    @POST("booking_service/")
    Call<CreateRequest> createRequest(@Body CreateRequest request);

    @Headers({"Access-Control-Allow-Methods: PATCH"})
    @PATCH("booking_service/{request_id}")
    Call<Request> updateRequest(@Path("request_id") String request_id, @Body Request request);// CANCELED, DENIED, FINISHED

    @Headers({"Access-Control-Allow-Methods: PATCH"})
    @PATCH("booking_service/{request_id}/{startAt}/{endAt}")
    Call<Request> updateRequest_Accepted(@Path("request_id") String request_id,
                                         @Path("startAt") String startAt,
                                         @Path("endAt") String endAt,
                                         @Body Request request);// ACCEPTED

}
