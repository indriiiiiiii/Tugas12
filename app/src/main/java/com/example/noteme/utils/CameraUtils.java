package com.example.noteme.utils;

import android.content.Context;
import android.util.Log;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;

public class CameraUtils {

    public static void startCamera(Context context, Executor executor, CameraProviderCallback callback) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();

                ImageCapture imageCapture = new ImageCapture.Builder().build();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle((androidx.lifecycle.LifecycleOwner) context,
                        cameraSelector, preview, imageCapture);

                callback.onCameraReady(imageCapture, preview);

            } catch (Exception e) {
                Log.e("CameraUtils", "Failed to start camera: " + e.getMessage());
            }
        }, executor);
    }

    public interface CameraProviderCallback {
        void onCameraReady(ImageCapture imageCapture, Preview preview);
    }
}
