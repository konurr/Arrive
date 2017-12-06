package com.arrive.conor.arrive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

//Class responsible for 'kicking off' the alarm process
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.i("SILENCE:", intent.getExtras().getString("silence_method"));
//        Log.i("REPEATS:", intent.getExtras().getString("repeats"));
//        Log.i("RINGTONE:", intent.getExtras().getString("ringtone"));
//        Log.i("NAVIGATION:", intent.getExtras().getString("navigation"));
        //Create intent to ringtone service
        Intent serviceIntent = new Intent(context, RingtoneService.class);

        //start ringtone service if flag is 1 else stop it
        if (intent.getExtras().getBoolean("startAlarm")) {
            context.startService(serviceIntent);
        } else {
            context.stopService(serviceIntent);
        }
    }
}
