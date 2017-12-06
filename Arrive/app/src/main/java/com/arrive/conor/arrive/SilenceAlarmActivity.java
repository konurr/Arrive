package com.arrive.conor.arrive;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arrive.conor.arrive.Silencers.SilencerMathSums;
import com.arrive.conor.arrive.Silencers.SilencerScanABarcode;
import com.arrive.conor.arrive.Silencers.SilencerShakeToWake;

public class SilenceAlarmActivity extends AppCompatActivity {
    AlarmManager alarmManager;
    Intent stopAlarm;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent receiveIntent = getIntent();
        Log.i("Silence Method:", receiveIntent.getExtras().getString("silence_method"));
        String silence_method = receiveIntent.getExtras().getString("silence_method");
        switch (silence_method) {
            case "Math Sums":
                Intent silencerMathSumsIntent = new Intent(this, SilencerMathSums.class);
                startActivity(silencerMathSumsIntent);
                break;
            case "Shake To Wake":
                Intent silencerShakeToWakeIntent = new Intent(this, SilencerShakeToWake.class);
                startActivity(silencerShakeToWakeIntent);
                break;
            case "Scan a Barcode":
                Intent silencerScanABarcodeIntent = new Intent(this, SilencerScanABarcode.class);
                startActivity(silencerScanABarcodeIntent);
                break;
            default:
                Toast.makeText(this, "Unknown Silence Method", Toast.LENGTH_SHORT).show();
        }
    }



    private void stopAlarmTest() {
        stopAlarm = new Intent(this, AlarmReceiver.class);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        /* STOPPPING THE ALARM */
        stopAlarm.putExtra("startAlarm", false);
        pendingIntent = PendingIntent.getBroadcast(this, 0, stopAlarm,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
    }
}
