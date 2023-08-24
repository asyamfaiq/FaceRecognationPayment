package com.example.tafacerecognation.request;

import com.google.gson.annotations.SerializedName;

public class TransferRequest {
    @SerializedName("account_number")
    private String account_number;

    @SerializedName("amount")
    private String amount;

    @SerializedName("nama")
    private String nama;

    @SerializedName("wajah")
    private String wajah;

    public TransferRequest(String account_number, String amount, String nama, String wajah) {
        this.account_number = account_number;
        this.amount = amount;
        this.nama = nama;
        this.wajah = wajah;
    }
}
