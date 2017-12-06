package com.arrive.conor.arrive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

//Class responsible for 'kicking off' the alarm process
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //intent for ringtone service
        Intent ringtoneServiceIntent = new Intent(context, RingtoneService.class);
        ringtoneServiceIntent.putExtra("silence_method", intent.getExtras().getString("silence_method"));

        //start ringtone service if flag is true else stop it
        if (intent.getExtras().getBoolean("startAlarm")) {
            context.startService(ringtoneServiceIntent);
        } else {
            context.stopService(ringtoneServiceIntent);
        }
    }
}
