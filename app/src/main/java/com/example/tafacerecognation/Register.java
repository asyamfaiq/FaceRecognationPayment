package com.example.tafacerecognation;

import static android.opengl.ETC1.encodeImage;
import static androidx.camera.core.CameraX.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tafacerecognation.api.ApiClient;
import com.example.tafacerecognation.api.ApiInterface;
import com.example.tafacerecognation.model.LoginResponse;
import com.example.tafacerecognation.model.RegistResponse;
import com.example.tafacerecognation.model.WajahResponse;
import com.example.tafacerecognation.request.LoginRequest;
import com.example.tafacerecognation.request.RegistRequest;
import com.example.tafacerecognation.request.WajahRequest;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    private ImageCapture imageCapture;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    Button btRegister;
    EditText username,emailRegis,passwordRegis;
    String nama;
    String base64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        previewView = findViewById(R.id.previewView);
        btRegister = findViewById(R.id.btRegister);
        username = findViewById(R.id.username);
        emailRegis = findViewById(R.id.emailRegis);
        passwordRegis = findViewById(R.id.passwordRegis);


        requestCameraPermission();
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = username.getText().toString();
                String email = emailRegis.getText().toString();
                String password = passwordRegis.getText().toString();
                capturePhoto();
                RegistRequest registRequest = new RegistRequest(nama,email,password);
                ApiInterface methods = ApiClient.getClient().create(ApiInterface.class);
                Call<RegistResponse> call = methods.add(registRequest);

                call.enqueue(new Callback<RegistResponse>() {
                    @Override
                    public void onResponse(Call<RegistResponse> call, Response<RegistResponse> response) {
                        if (response.isSuccessful()) {

                            Log.e("Test", "onResponse: code: " + response.code());
                            Toast.makeText(Register.this, "Proses", Toast.LENGTH_LONG).show();


                        } else if (response.code() != 201) {
                            Toast.makeText(Register.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistResponse> call, Throwable t) {
                        Log.e("test", "onFailure" + t.getMessage());
                        Toast.makeText(Register.this, "Server gagal", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestCameraPermission() {
        if (!hasCameraPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            // Camera permission has been granted
            // Do something here
        }
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {

        cameraProvider.unbindAll();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        Preview preview = new Preview.Builder().build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    private void capturePhoto() {
        long timeStamp = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(
                        getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Uri imageUri = outputFileResults.getSavedUri();
                        if (imageUri != null) {
                            String uriString = imageUri.toString();
                            try {
                                base64 = convertToBase64(imageUri);

                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            Toast.makeText(Register.this, "Berhasil", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(Register.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private String convertToBase64(Uri uri) throws FileNotFoundException {
        final InputStream imageStream;
        imageStream = getContentResolver().openInputStream(uri);
        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        String encodedImage = encodeImage(selectedImage);

        ApiInterface methods = ApiClient.getClient().create(ApiInterface.class);
        WajahRequest wajahRequest = new WajahRequest(nama, encodedImage);
        Call<WajahResponse> call2 = methods.wajah(wajahRequest);

        call2.enqueue(new Callback<WajahResponse>() {
            @Override
            public void onResponse(Call<WajahResponse> call, Response<WajahResponse> response) {
                if (response.isSuccessful()) {

                    Log.e("Test", "onResponse: code: " + response.code());
                    Toast.makeText(Register.this, "Berhasil", Toast.LENGTH_LONG).show();
                    finish();

                } else if (response.code() != 201) {
                    Toast.makeText(Register.this, "Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WajahResponse> call, Throwable t) {
                Log.e("test", "onFailure" + t.getMessage());
                Toast.makeText(Register.this, "Berhasil", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        return encodedImage;
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
}