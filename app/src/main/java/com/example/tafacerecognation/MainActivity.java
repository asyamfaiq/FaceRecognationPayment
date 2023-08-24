package com.example.tafacerecognation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tafacerecognation.api.ApiClient;
import com.example.tafacerecognation.api.ApiInterface;
import com.example.tafacerecognation.model.LoginResponse;
import com.example.tafacerecognation.request.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button login,register;
    EditText emaillogin,passwordlogin;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("sharepre", Context.MODE_PRIVATE);

        emaillogin = findViewById(R.id.emaillogin);
        passwordlogin = findViewById(R.id.passwordlogin);

        login = findViewById(R.id.btLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emaillogin.getText().toString();
                String password = passwordlogin.getText().toString();

                LoginRequest loginRequest = new LoginRequest(email, password);

                ApiInterface methods = ApiClient.getClient().create(ApiInterface.class);
                Call<LoginResponse> call = methods.login(loginRequest);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            int id = response.body().getId();

                            Log.e("Test", "onResponse: code: " + response.code());
                            Toast.makeText(MainActivity.this, "Selamat Datang", Toast.LENGTH_LONG).show();

                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("id", id);
                            editor.commit();

                            startActivity(new Intent(getApplicationContext(), Home.class));
                            finish();

                        } else if (response.code() != 201) {
                            Toast.makeText(MainActivity.this, "Email atau password yang anda masukkan salah", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.e("test", "onFailure" + t.getMessage());
                        Toast.makeText(MainActivity.this, "Server gagal", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        register = findViewById(R.id.btDaftar);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }
}