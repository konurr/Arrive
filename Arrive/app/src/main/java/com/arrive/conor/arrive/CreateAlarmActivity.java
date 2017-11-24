package com.arrive.conor.arrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAlarmActivity extends AppCompatActivity implements View.OnClickListener {
    //Global reference to all views in activity
    TextView tvSelectTime, tvSilenceMethod, tvRepeats, tvRingtone, tvDestination;
    TextView hSelectTime, hSilenceMethod, hRingtone;
    Button btnSelectTime, btnSilenceMethod, btnRingtone, btnDestination, btnAlarmCreated;
    CheckBox monChkbox, tueChkbox, wedChkbox, thuChkbox, friChkbox, satChkbox, sunChkbox;
    Switch sNavigation;


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

        tvRepeats = (TextView) findViewById(R.id.repeats_textView);
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

    private void destinationShown(boolean shown) {
        if (shown) {
            tvDestination.setVisibility(View.VISIBLE);
            btnDestination.setVisibility(View.VISIBLE);
        } else {
            tvDestination.setVisibility(View.INVISIBLE);
            btnDestination.setVisibility(View.INVISIBLE);
        }
    }

//    private void loopViews(ViewGroup view) {
//        for (int i = 0; i < view.getChildCount(); i++) {
//            View v = view.getChildAt(i);
//
//            if (v instanceof Button) {
//                // set listener
//
//            } else if (v instanceof CheckBox) {
//                //set listener
//
//            } else if (v instanceof ViewGroup) {
//
//                this.loopViews((ViewGroup) v);
//            }
//        }
//    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.time_select_btn:
                Toast.makeText(this, "Time", Toast.LENGTH_SHORT).show();
                break;
            case R.id.silence_mthd_btn:
                Toast.makeText(this, "Silence", Toast.LENGTH_SHORT).show();
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
}
