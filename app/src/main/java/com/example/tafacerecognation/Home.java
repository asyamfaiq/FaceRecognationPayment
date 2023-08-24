package com.example.tafacerecognation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tafacerecognation.api.ApiClient;
import com.example.tafacerecognation.api.ApiInterface;
import com.example.tafacerecognation.model.UserResponse;
import com.example.tafacerecognation.request.UserRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    Button security,topup,transfer;
    TextView nominal,nama;
    private int loading = 25000;
    static String namaku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        security = findViewById(R.id.security);
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Wajah.class));
            }
        });

        topup = findViewById(R.id.topup2);
        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Topup.class));
            }
        });

        nama = findViewById(R.id.nama);
        nominal = findViewById(R.id.nominal);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //nominal.setText("100.000");
            }
        },loading);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("sharepre", Context.MODE_PRIVATE);
        int id = sp.getInt("id",0);
        UserRequest userRequest = new UserRequest(id);
        ApiInterface methods = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> call = methods.user(userRequest);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    double saldo = response.body().getSaldo();
                    namaku = response.body().getName();
                    String jumlah =Double.toString(saldo);
                    nominal.setText(jumlah);
                    nama.setText(namaku);
                    Log.e("Test", "onResponse: code: " + response.code());

                } else if (response.code() != 201) {

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("test", "onFailure" + t.getMessage());
                Toast.makeText(Home.this, "Server gagal", Toast.LENGTH_SHORT).show();
            }
        });


        transfer = findViewById(R.id.transfer);
        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Transfer.class));
            }
        });
    }
}