package com.example.tafacerecognation.request;

import com.google.gson.annotations.SerializedName;

public class RegistRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("password")
    private String password;


    public RegistRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
