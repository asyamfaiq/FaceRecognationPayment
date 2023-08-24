package com.example.tafacerecognation.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.252.131.86:5000/";

    private static HttpLoggingInterceptor logger = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient.Builder okHttp = new OkHttpClient.Builder().addInterceptor(logger)
            .connectTimeout(120, TimeUnit.SECONDS) // Batas waktu koneksi: 30 detik
            .readTimeout(120, TimeUnit.SECONDS) // Batas waktu baca respons: 30 detik
            .writeTimeout(120, TimeUnit.SECONDS);
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttp.build())
                    .build();
        }
        return retrofit;
    }
}
