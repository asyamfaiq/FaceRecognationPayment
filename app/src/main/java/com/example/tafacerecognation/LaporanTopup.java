package com.example.tafacerecognation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

public class LaporanTopup extends AppCompatActivity {
    TextView status;
    Button btKembali;
    private int loading = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_topup);

        status = findViewById(R.id.status);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                status.setText("Success");
                status.setTextColor(ContextCompat.getColor(LaporanTopup.this, R.color.teal_700));
            }
        },loading);
    }
}