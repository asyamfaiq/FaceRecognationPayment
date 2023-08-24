package com.example.tafacerecognation.request;

import com.google.gson.annotations.SerializedName;

public class WajahRequest {
    @SerializedName("name")
    private String name;

    @SerializedName("wajah")
    private String wajah;

    public WajahRequest(String name, String wajah) {
        this.name = name;
        this.wajah = wajah;
    }
}
