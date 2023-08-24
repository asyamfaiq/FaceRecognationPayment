package com.example.tafacerecognation;

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
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tafacerecognation.api.ApiClient;
import com.example.tafacerecognation.api.ApiInterface;
import com.example.tafacerecognation.model.TransferResponse;
import com.example.tafacerecognation.model.WajahResponse;
import com.example.tafacerecognation.request.TransferRequest;
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

public class Transfer extends AppCompatActivity {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    Button bTakePicture, bRecording,btSend;
    EditText etNoRek,etJumlah;
    private ImageCapture imageCapture;
    TextView hasil;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    String noRek,amount,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        requestCameraPermission();

        Home home = new Home();
        name = home.namaku;

        System.out.println(name);

        hasil = findViewById(R.id.hasil);
        bTakePicture = findViewById(R.id.bCapture);
        previewView = findViewById(R.id.previewView);

        bTakePicture.setOnClickListener(view -> capturePhoto());

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());

        etNoRek = findViewById(R.id.etNoRek);
        etJumlah = findViewById(R.id.etJumlah);

        btSend = findViewById(R.id.btSend);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noRek = etNoRek.getText().toString();
                amount = etJumlah.getText().toString();
                capturePhoto();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted
                // Do something here
            } else {
                // Camera permission has been denied
                // Do something here
            }
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
                                convertToBase64(imageUri);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            Toast.makeText(Transfer.this, "Berhasil", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(Transfer.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private String convertToBase64(Uri uri) throws FileNotFoundException {
        final InputStream imageStream;
        imageStream = getContentResolver().openInputStream(uri);
        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        String encodedImage = encodeImage(selectedImage);

        TransferRequest transferRequest = new TransferRequest(noRek,amount,name,encodedImage);
        ApiInterface methods = ApiClient.getClient().create(ApiInterface.class);
        Call<TransferResponse> call = methods.transfer(transferRequest);

        call.enqueue(new Callback<TransferResponse>() {
            @Override
            public void onResponse(Call<TransferResponse> call, Response<TransferResponse> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    hasil.setText(status);
                    Log.e("Test", "onResponse: code: " + response.code());

                } else if (response.code() != 201) {

                }
            }

            @Override
            public void onFailure(Call<TransferResponse> call, Throwable t) {
                Log.e("test", "onFailure" + t.getMessage());

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