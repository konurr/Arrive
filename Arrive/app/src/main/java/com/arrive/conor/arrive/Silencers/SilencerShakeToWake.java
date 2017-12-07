package com.arrive.conor.arrive.Silencers;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arrive.conor.arrive.AlarmReceiver;
import com.arrive.conor.arrive.MainActivity;
import com.arrive.conor.arrive.R;

public class SilencerShakeToWake extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    TextView tvMsg, tvCounter, tvTotal;
    int cnt;

    AlarmManager alarmManager;
    Intent stopAlarm, returnHome;
    PendingIntent pendingIntent;

    Button btnStopAlarm;

    private SensorManager sensorManager;
    private long lastUpdateTime;
    private static final double SHAKE_THRESHOLD_GRAVITY = 1.25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silencer_shake_to_wake);

        tvMsg = (TextView) findViewById(R.id.tvMsg);
        tvCounter = (TextView) findViewById(R.id.tvShakeCount);
        tvTotal = (TextView) findViewById(R.id.tvTotal);

        btnStopAlarm = (Button) findViewById(R.id.btnStopAlarm);
        btnStopAlarm.setOnClickListener(this);
        btnStopAlarm.setVisibility(View.INVISIBLE);

        cnt = 0;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(sensorEvent);
        }
    }

    private void getAccelerometer(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        long currentTime = System.currentTimeMillis();
        if (gForce >= SHAKE_THRESHOLD_GRAVITY) {

            if (currentTime - lastUpdateTime < 200) {
                return;
            }
            lastUpdateTime = currentTime;
            if (cnt <= 50) {
                tvCounter.setText(String.valueOf(cnt++));
            } else { //device shaken 50 times, stop alarm.
                sensorManager.unregisterListener(this);
                //show stop button
                btnStopAlarm.setVisibility(View.VISIBLE);

            }
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //TODO: Start Navigation Activity
                    Toast.makeText(SilencerShakeToWake.this, "Launching navigation activity", Toast.LENGTH_SHORT).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    returnHome = new Intent(SilencerShakeToWake.super.getApplicationContext(), MainActivity.class);
                    startActivity(returnHome);
                    break;
            }
        }
    };

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    public void onClick(View view) {
        stopAlarm = new Intent(this, AlarmReceiver.class);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        stopAlarm.putExtra("startAlarm", false);
        pendingIntent = PendingIntent.getBroadcast(this, 0, stopAlarm,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                pendingIntent);

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Is Navigation Required?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
