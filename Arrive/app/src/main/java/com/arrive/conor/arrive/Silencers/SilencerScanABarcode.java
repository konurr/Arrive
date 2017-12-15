package com.arrive.conor.arrive.Silencers;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arrive.conor.arrive.AlarmReceiver;
import com.arrive.conor.arrive.MainActivity;
import com.arrive.conor.arrive.NavigationActivity;
import com.arrive.conor.arrive.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SilencerScanABarcode extends AppCompatActivity implements View.OnClickListener {

    AlarmManager alarmManager;
    Intent stopAlarm, returnHome;
    PendingIntent pendingIntent;

    Button btnSubmitBarcode;

    SurfaceView cameraView;
    TextView barcodeInfo;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    Calendar alarmStart, alarmStop;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "ALARM_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmStart = Calendar.getInstance();
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setContentView(R.layout.activity_silencer_scan_abarcode);

        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        barcodeInfo = (TextView) findViewById(R.id.code_info);
        btnSubmitBarcode = (Button) findViewById(R.id.btnSubmitBarcode);
        btnSubmitBarcode.setOnClickListener(this);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(SilencerScanABarcode.super.getApplicationContext(),
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Request permission if not present!
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }

                barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                    @Override
                    public void release() {
                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {
                        final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                        if (barcodes.size() != 0) {
                            alarmStop = Calendar.getInstance();
                            String timeTakenToSilence = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Math.abs(alarmStop.getTimeInMillis() - alarmStart.getTimeInMillis())));
                            silenceAlarm(timeTakenToSilence);
                            Log.i("TIME TAKEN TO SILENCE", timeTakenToSilence);
                            cameraView.post(new Runnable() {
                                @Override
                                public void run() {
                                    btnSubmitBarcode.setVisibility(View.VISIBLE);
                                    Log.i("BARCODE READING:",  barcodes.valueAt(0).displayValue);
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Intent startNavigation = new Intent(getApplicationContext(), NavigationActivity.class);
                    startActivity(startNavigation);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    returnHome = new Intent(
                            SilencerScanABarcode.super.getApplicationContext(),
                            MainActivity.class);
                    startActivity(returnHome);
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Is Navigation Required?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    protected void silenceAlarm(String timeTakenToSilence) {
        editor.putString("time_taken", timeTakenToSilence).commit();

        stopAlarm = new Intent(SilencerScanABarcode.super.getApplicationContext(),
                AlarmReceiver.class);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        stopAlarm.putExtra("startAlarm", false);
        pendingIntent = PendingIntent.getBroadcast(
                SilencerScanABarcode.super.getApplicationContext(), 0, stopAlarm,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                pendingIntent);
    }
}
