package com.example.tafacerecognation.request;

import com.google.gson.annotations.SerializedName;

public class UserRequest {
    @SerializedName("id")
    private int id;


    public UserRequest(int id) {
        this.id = id;
    }
}
