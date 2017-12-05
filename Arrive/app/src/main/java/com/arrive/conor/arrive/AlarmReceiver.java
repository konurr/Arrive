package com.arrive.conor.arrive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("We are in the receiver!", "Yay!");

        //Create intent to ringtone service
        Intent serviceIntent = new Intent(context, RingtoneService.class);

        //start ringtone service
        context.startService(serviceIntent);
    }
}
