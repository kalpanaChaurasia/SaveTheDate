package com.arunsoorya.savethedate.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.arunsoorya.savethedate.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;

public class ImageReadActivity extends AppCompatActivity {

    private static final String TAG = "image read";
    private CameraSource mCameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_read);

        // Create the mCameraSource using the TextRecognizer.

    }

    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();

        // Create the TextRecognizer
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        // TODO: Set the TextRecognizer's Processor.

        // Check if the TextRecognizer is operational.
        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }
        mCameraSource =
                new CameraSource.Builder(getApplicationContext(), textRecognizer)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        .setRequestedPreviewSize(1280, 1024)
                        .setRequestedFps(15.0f)
//                        .setFlashMode(false ? Camera.Parameters.FLASH_MODE_TORCH : null)
//                        .setFocusMode(true ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null)
                        .build();
    }

}
