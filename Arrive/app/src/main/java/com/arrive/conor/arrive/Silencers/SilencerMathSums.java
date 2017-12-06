package com.arrive.conor.arrive.Silencers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arrive.conor.arrive.AlarmReceiver;
import com.arrive.conor.arrive.R;

public class SilencerMathSums extends AppCompatActivity implements View.OnClickListener {
    Button btnStopAlarm;

    AlarmManager alarmManager;
    Intent stopAlarm;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silencer_math_sums);

        btnStopAlarm = (Button) findViewById(R.id.btnStopAlarm);
        btnStopAlarm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnStopAlarm) {
            stopAlarm = new Intent(this, AlarmReceiver.class);

            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        /* STOPPPING THE ALARM */
            stopAlarm.putExtra("startAlarm", false);
            pendingIntent = PendingIntent.getBroadcast(this, 0, stopAlarm,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
        }
    }
}
