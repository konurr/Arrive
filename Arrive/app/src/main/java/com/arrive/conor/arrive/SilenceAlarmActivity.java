package com.arrive.conor.arrive;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SilenceAlarmActivity extends AppCompatActivity implements View.OnClickListener {
    Button silenceAlarm;
    AlarmManager alarmManager;
    Intent stopAlarm;
    PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silence_alarm);

        silenceAlarm = (Button) findViewById(R.id.silenceBtn);
        silenceAlarm.setOnClickListener(this);

        stopAlarm = new Intent(this, AlarmReceiver.class);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.silenceBtn) {
            stopAlarm.putExtra("startAlarm", false);
            pendingIntent = PendingIntent.getBroadcast(this, 0, stopAlarm,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
        }
    }
}
