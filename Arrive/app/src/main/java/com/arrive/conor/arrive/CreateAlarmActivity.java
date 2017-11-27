package com.arrive.conor.arrive;

import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    //Global reference to all views in activity
    TextView tvSelectTime, tvSilenceMethod, tvRingtone, tvDestination;
    TextView hSelectTime, hSilenceMethod, hRingtone;
    Button btnSelectTime, btnSilenceMethod, btnRingtone, btnDestination, btnAlarmCreated;
    CheckBox monChkbox, tueChkbox, wedChkbox, thuChkbox, friChkbox, satChkbox, sunChkbox;
    Switch sRepeats, sNavigation;
    Calendar cal = Calendar.getInstance();

    TimePickerDialog.OnTimeSetListener dialogListener = new TimePickerDialog
            .OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int il) {
            tvSelectTime.setText(timePicker.getHour() + ":"
                    + timePicker.getMinute());
        }
    };

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
    }

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
                finish();
                break;
        }
    }

    private void silenceMethod() {
        FragmentManager manager = getFragmentManager();
        SilenceAlarmFragment silenceAlarmFragment = new SilenceAlarmFragment();
        silenceAlarmFragment.show(manager, "SilenceAlarmFragment");
    }

    private void showTimeDialog() {
        new TimePickerDialog(CreateAlarmActivity.this, dialogListener, 7,
                30, false).show();
    }

    @Override
    public void onSilenceMethodSelected(String message) {
        tvSilenceMethod.setText(message);
    }
}
