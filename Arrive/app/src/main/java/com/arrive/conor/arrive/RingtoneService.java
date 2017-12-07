package com.arrive.conor.arrive;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


public class RingtoneService extends Service {

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.start();

        Intent startSilencer = new Intent(this, SilenceAlarmActivity.class);
        startSilencer.putExtra("silence_method", intent.getExtras().getString("silence_method"));
        startActivity(startSilencer);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("RIP:", "You killed the ringtone service!");
        mediaPlayer.stop();
        super.onDestroy();
    }


}
