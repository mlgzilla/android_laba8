package com.example.laba2;

import android.content.ContentValues;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Range;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExposureState;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import com.example.laba2.databinding.AuthActivityBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class AuthActivity extends AppCompatActivity {
    private AuthActivityBinding activityBinding;

    private ProcessCameraProvider cameraProvider;
    private ImageCapture imageCapture;
    private CameraControl cameraControl;
    private CameraInfo cameraInfo;

    @Override
    protected void onStart() {
        super.onStart();
        activityBinding = AuthActivityBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());
        activityBinding.seekBar.setActivated(false);

        activityBinding.shootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
            }
        });
        activityBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                cameraControl.setExposureCompensationIndex(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderListenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraProvider = cameraProviderListenableFuture.get();
                    startCamera(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void capturePhoto() {
        if (imageCapture == null) return;

        String name = System.currentTimeMillis() + "";

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraXStable");
        }

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues).build();
        imageCapture.takePicture(outputFileOptions,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(AuthActivity.this,
                                "Сохранено в " + outputFileResults.getSavedUri(),
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(AuthActivity.this,
                                "Что то пошло не так",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void startCamera(ProcessCameraProvider cameraProvider) {
        CameraSelector defaultFrontCamera = CameraSelector.DEFAULT_FRONT_CAMERA;
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(activityBinding.viewFinder.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder().build();
        try {
            cameraProvider.unbindAll();
            Camera camera = cameraProvider.bindToLifecycle(this, defaultFrontCamera, preview, imageCapture);
            cameraControl = camera.getCameraControl();
            cameraInfo = camera.getCameraInfo();
            cameraControl.setExposureCompensationIndex(0);
            Range<Integer> exposureRange = cameraInfo.getExposureState().getExposureCompensationRange();
            activityBinding.seekBar.setMax(exposureRange.getUpper());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activityBinding.seekBar.setMin(exposureRange.getLower());
            }
            activityBinding.seekBar.setProgress(cameraInfo.getExposureState().getExposureCompensationIndex());
            activityBinding.seekBar.setActivated(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
