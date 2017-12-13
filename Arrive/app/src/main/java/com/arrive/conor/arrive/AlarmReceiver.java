package com.arrive.conor.arrive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

//Class responsible for 'kicking off' the alarm process
public class AlarmReceiver extends BroadcastReceiver {
    public static final String PREFS_NAME = "ALARM_INFO";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onReceive(Context context, Intent intent) {

        //intent for ringtone service
        Intent ringtoneServiceIntent = new Intent(context, RingtoneService.class);
        ringtoneServiceIntent.putExtra("silence_method", intent.getExtras().getString("silence_method"));
        ringtoneServiceIntent.putExtra("ringtone", intent.getExtras().getString("ringtone"));

        //start ringtone service if flag is true else stop it
        if (intent.getExtras().getBoolean("startAlarm")) {
            context.startService(ringtoneServiceIntent);
        } else {
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.remove("next_alarm_long").commit();
            context.stopService(ringtoneServiceIntent);
        }
    }
}
