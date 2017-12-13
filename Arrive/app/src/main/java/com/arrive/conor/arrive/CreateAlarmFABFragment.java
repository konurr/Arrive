package com.arrive.conor.arrive;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAlarmFABFragment extends Fragment {
    FloatingActionButton fab;
    public static final String PREFS_NAME = "ALARM_INFO";
    SharedPreferences sharedPreferences;
    TextView tvNextAlarm;


    public CreateAlarmFABFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_alarm_fab, container, false);

        sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        tvNextAlarm = (TextView) view.findViewById(R.id.tvNextAlarm);
        long nextAlarmLong = sharedPreferences.getLong("next_alarm_long", -1);
        if (nextAlarmLong != -1) {
            Date nextAlarm = determineNextAlarmTime(sharedPreferences.getLong("next_alarm_long", 0L));
            String displayedAlarm = new SimpleDateFormat("EEE d MMM @ h:mm a").format(nextAlarm);
            tvNextAlarm.setText("Next Alarm: " + displayedAlarm);
            tvNextAlarm.setTextSize(24);
        }

        fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateAlarmActivity.class);
                CreateAlarmFABFragment.this.startActivity(intent);
            }
        });

        return view;
    }

    public Date determineNextAlarmTime(Long nextAlarmTime) {
        Date nextAlarm = new Date(nextAlarmTime);

        return nextAlarm;
    }
}
