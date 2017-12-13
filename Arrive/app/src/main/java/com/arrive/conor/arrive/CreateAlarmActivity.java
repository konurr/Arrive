package com.arrive.conor.arrive;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
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

import com.arrive.conor.arrive.Silencers.SilencerShakeToWake;

public class CreateAlarmActivity extends AppCompatActivity implements View.OnClickListener,
        SilenceAlarmFragment.Communicator, InputDestinationFragment.Communicator {

    final String TAG = "LOG:";

    FragmentManager manager = getFragmentManager();
    public static final String PREFS_NAME = "ALARM_INFO";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Ringtone ringtone = null;


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
    String chosenRingtoneUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

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
        if (sharedPreferences.getString("default_ringtone", "") != null) {
            Uri uri = Uri.parse(sharedPreferences.getString("default_ringtone", ""));
            Ringtone textViewRingtone = RingtoneManager.getRingtone(this, uri);
            tvRingtone.setText(textViewRingtone.getTitle(this) + " (default)");
        } else {
            tvRingtone.setText("Default");
        }
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
                getRingtone();
                break;
            case R.id.change_destination_btn:
                changeDestination();
                break;
            case R.id.alarm_created_button:
                //Delay intent until alarm time
                createAlarm.putExtra("silence_method", tvSilenceMethod.getText().toString());
                createAlarm.putExtra("repeats", isRepeatsEnabled() ? getRepeatsDays() : "Never");
                createAlarm.putExtra("ringtone", sharedPreferences.getString("default_ringtone",
                        "one_time_ringtone"));
                createAlarm.putExtra("navigation", isNavigationEnabled() ? getDestination() :
                        "not_required");
                createAlarm.putExtra("startAlarm", true);
                pendingIntent = PendingIntent.getBroadcast(this, 0, createAlarm,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                //Set the alarm manager
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pendingIntent); //TODO Make this repeating
                Log.i(TAG, "Alarm set for " + calendar.getTime());
                editor.putLong("next_alarm_long", calendar.getTimeInMillis());
                editor.putString("alarm_time", calendar.getTime().toString());
                editor.putString("alarm_destination", tvDestination.getText().toString());
                editor.commit();
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

    private void getRingtone() {

        Uri defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
        Ringtone defaultRingtone = RingtoneManager.getRingtone(this, defaultRintoneUri);
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for notifications:");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, defaultRintoneUri);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        startActivityForResult(intent, 999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 999) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri != null) {
                this.chosenRingtoneUri = uri.toString();
                ringtone = RingtoneManager.getRingtone(this, uri);
            } else {
                ringtone = RingtoneManager.getRingtone(this,
                        RingtoneManager.getActualDefaultRingtoneUri(this,
                                RingtoneManager.TYPE_ALARM));
                this.chosenRingtoneUri = null;
            }
        }
        String title = ringtone.getTitle(this);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Set " + title +
                " as default ringtone for Arrive?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    editor.putString("default_ringtone", chosenRingtoneUri.toString()).commit();
                    tvRingtone.setText(ringtone.getTitle(CreateAlarmActivity.this));
                    Toast.makeText(CreateAlarmActivity.this,
                            ringtone.getTitle(CreateAlarmActivity.this) +
                                    " set as default ringtone", Toast.LENGTH_SHORT).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    editor.putString("one_time_ringtone", chosenRingtoneUri.toString()).commit();
                    tvRingtone.setText(ringtone.getTitle(CreateAlarmActivity.this));
                    Toast.makeText(CreateAlarmActivity.this, "Default ringtone won't be changed",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private boolean isRepeatsEnabled() {
        return false;
    }

    //Event listener for TimePicker Time selected
    TimePickerDialog.OnTimeSetListener dialogListener = new TimePickerDialog
            .OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int il) {
            if (timePicker.getMinute() < 10)
                tvSelectTime.setText(timePicker.getHour() + ":0" + timePicker.getMinute());
            else
                tvSelectTime.setText(timePicker.getHour() + ":" + timePicker.getMinute());

            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
            time = (timePicker.getHour() * 100) + timePicker.getMinute(); //Sets the alarm time
        }
    };

    private void showTimeDialog() {
        new TimePickerDialog(CreateAlarmActivity.this, dialogListener, 7,
                30, true).show();
    }

    private void silenceMethod() {
        SilenceAlarmFragment silenceAlarmFragment = new SilenceAlarmFragment();
        silenceAlarmFragment.show(manager, "SilenceAlarmFragment");
    }

    @Override
    public void onSilenceMethodSelected(String message) {
        tvSilenceMethod.setText(message);
    }

    private void changeDestination() {
        InputDestinationFragment inputDestinationFragment = new InputDestinationFragment();
        inputDestinationFragment.show(manager, "InputDestinationFragment");
    }

    @Override
    public void setDestination(String streetNumber, String streetName, String city, String postcode) {
        Log.i("STREET NUMBER", streetNumber);
        Log.i("STREET NAME", streetName);
        Log.i("CITY", city);
        Log.i("POSTCODE", postcode);
        String destination = streetNumber.trim() + " "
                + streetName.trim() + " "
                + city.trim() + " "
                + postcode.trim();
        tvDestination.setText(destination);
    }
}
