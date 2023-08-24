package com.example.tafacerecognation.api;

import com.example.tafacerecognation.model.RegistResponse;
import com.example.tafacerecognation.model.WajahResponse;
import com.example.tafacerecognation.request.RegistRequest;
import com.example.tafacerecognation.request.LoginRequest;
import com.example.tafacerecognation.request.TransferRequest;
import com.example.tafacerecognation.request.UserRequest;
import com.example.tafacerecognation.model.LoginResponse;
import com.example.tafacerecognation.model.TransferResponse;
import com.example.tafacerecognation.model.UserResponse;
import com.example.tafacerecognation.request.WajahRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("user")
    Call<UserResponse> user(@Body UserRequest userRequest);

    @POST("add")
    Call<RegistResponse> add(@Body RegistRequest registRequest);

    @POST("transfer")
    Call<TransferResponse> transfer(@Body TransferRequest transferRequest);

    @POST("wajahuser")
    Call<WajahResponse> wajah(@Body WajahRequest wajahRequest);
}
