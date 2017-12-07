package com.arrive.conor.arrive;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateAlarmActivity extends AppCompatActivity implements View.OnClickListener,
        SilenceAlarmFragment.Communicator {

    final String TAG = "LOG:";

    //TODO: Prevent rotation of this Activity

    //Global reference for Alarm Manager
    AlarmManager alarmManager;
    Calendar calendar = Calendar.getInstance();
    Intent createAlarm;
    PendingIntent pendingIntent;

    long time;
    //Global reference to all views in activity
    TextView tvSelectTime, tvSilenceMethod, tvRingtone, tvDestination;
    TextView hSelectTime, hSilenceMethod, hRingtone;
    Button btnSelectTime, btnSilenceMethod, btnRingtone, btnDestination, btnAlarmCreated;
    CheckBox monChkbox, tueChkbox, wedChkbox, thuChkbox, friChkbox, satChkbox, sunChkbox;
    Switch sRepeats, sNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);


        tvSelectTime = (TextView) findViewById(R.id.time_select_textView);
        hSelectTime = (TextView) findViewById(R.id.time_select_hint);
        btnSelectTime = (Button) findViewById(R.id.time_select_btn);
        btnSelectTime.setOnClickListener(this);

        tvSilenceMethod = (TextView) findViewById(R.id.silence_method_textView);
        hSilenceMethod = (TextView) findViewById(R.id.silence_mthd_hint);
        btnSilenceMethod = (Button) findViewById(R.id.silence_mthd_btn);
        btnSilenceMethod.setOnClickListener(this);

        sRepeats = (Switch) findViewById(R.id.repeats_switch);
        sRepeats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                repeatsShown(b);
            }
        });
        monChkbox = (CheckBox) findViewById(R.id.mon_chkbox);
        tueChkbox = (CheckBox) findViewById(R.id.tue_chkbox);
        wedChkbox = (CheckBox) findViewById(R.id.wed_chkbox);
        thuChkbox = (CheckBox) findViewById(R.id.thurs_chkbox);
        friChkbox = (CheckBox) findViewById(R.id.fri_chkbox);
        satChkbox = (CheckBox) findViewById(R.id.sat_chkbox);
        sunChkbox = (CheckBox) findViewById(R.id.sun_chkbox);
        //TODO: Implement listener for checkbox changed

        tvRingtone = (TextView) findViewById(R.id.ringtone_textView);
        hRingtone = (TextView) findViewById(R.id.ringtone_hint);
        btnRingtone = (Button) findViewById(R.id.ringtone_select_btn);
        btnRingtone.setOnClickListener(this);

        sNavigation = (Switch) findViewById(R.id.navigation_switch);
        sNavigation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                destinationShown(b);
            }
        });

        tvDestination = (TextView) findViewById(R.id.destination_textView);
        btnDestination = (Button) findViewById(R.id.change_destination_btn);
        btnDestination.setOnClickListener(this);

        btnAlarmCreated = (Button) findViewById(R.id.alarm_created_button);
        btnAlarmCreated.setOnClickListener(this);

        //Initialize alarm manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //Create intent to send broadcast to alarm receiver
        createAlarm = new Intent(this, AlarmReceiver.class);

    }

    //Display/hide repeats options if switch is on/off
    private void repeatsShown(boolean shown) {
        if (shown) {
            monChkbox.setVisibility(View.VISIBLE);
            tueChkbox.setVisibility(View.VISIBLE);
            wedChkbox.setVisibility(View.VISIBLE);
            thuChkbox.setVisibility(View.VISIBLE);
            friChkbox.setVisibility(View.VISIBLE);
            satChkbox.setVisibility(View.VISIBLE);
            sunChkbox.setVisibility(View.VISIBLE);
        } else {
            monChkbox.setVisibility(View.INVISIBLE);
            tueChkbox.setVisibility(View.INVISIBLE);
            wedChkbox.setVisibility(View.INVISIBLE);
            thuChkbox.setVisibility(View.INVISIBLE);
            friChkbox.setVisibility(View.INVISIBLE);
            satChkbox.setVisibility(View.INVISIBLE);
            sunChkbox.setVisibility(View.INVISIBLE);
        }
    }
    //Display/hide destination if navigation is on/off
    private void destinationShown(boolean shown) {
        if (shown) {
            tvDestination.setVisibility(View.VISIBLE);
            btnDestination.setVisibility(View.VISIBLE);
        } else {
            tvDestination.setVisibility(View.INVISIBLE);
            btnDestination.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.time_select_btn:
                showTimeDialog();
                break;
            case R.id.silence_mthd_btn:
                silenceMethod();
                break;
            case R.id.ringtone_select_btn:
                Toast.makeText(this, "Ringtone", Toast.LENGTH_SHORT).show();
                break;
            case R.id.change_destination_btn:
                Toast.makeText(this, "Destination", Toast.LENGTH_SHORT).show();
                break;
            case R.id.alarm_created_button:
                //Delay intent until alarm time
                createAlarm.putExtra("silence_method", tvSilenceMethod.getText().toString());
                createAlarm.putExtra("repeats", isRepeatsEnabled() ? getRepeatsDays() : "Never");
                createAlarm.putExtra("ringtone", /*getRingtone()*/ "default");//TODO implement getRingtone()
                createAlarm.putExtra("navigation", isNavigationEnabled() ? getDestination() : "not_required");
                createAlarm.putExtra("startAlarm", true);
                pendingIntent = PendingIntent.getBroadcast(this, 0, createAlarm,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                //Set the alarm manager
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pendingIntent); //TODO Make this repeating
                Log.i(TAG, "Alarm set for " + calendar.getTime());
                finish();
                break;
        }
    }

    //TODO Implement these properly
    private String[] getRepeatsDays() {
        return new String[0];
    }

    private String getDestination() {
        return "BT487JJ";
    }

    private boolean isNavigationEnabled() {
        return false;
    }

    private boolean getRingtone() {
        return false;
    }

    private boolean isRepeatsEnabled() {
        return false;
    }

    //Event listener for TimePicker Time selected
    TimePickerDialog.OnTimeSetListener dialogListener = new TimePickerDialog
            .OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int il) {
            tvSelectTime.setText(timePicker.getHour() + ":"
                    + timePicker.getMinute());
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
            time = (timePicker.getHour() * 100) + timePicker.getMinute(); //Sets the alarm time
        }
    };

    private void silenceMethod() {
        FragmentManager manager = getFragmentManager();
        SilenceAlarmFragment silenceAlarmFragment = new SilenceAlarmFragment();
        silenceAlarmFragment.show(manager, "SilenceAlarmFragment");
    }

    private void showTimeDialog() {
        new TimePickerDialog(CreateAlarmActivity.this, dialogListener, 7,
                30, true).show();
    }

    @Override
    public void onSilenceMethodSelected(String message) {
        tvSilenceMethod.setText(message);
    }
}
