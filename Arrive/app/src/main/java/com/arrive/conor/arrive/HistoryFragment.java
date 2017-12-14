package com.arrive.conor.arrive;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class HistoryFragment extends Fragment{
    public static final String PREFS_NAME = "ALARM_INFO";
    SharedPreferences sharedPreferences;
    TextView timeTakenTextview;
    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        timeTakenTextview = (TextView) view.findViewById(R.id.tvTimeTaken);

        //Check if time taken string is present in shared preferences
        //If true update textview with info
        if(sharedPreferences.contains("time_taken")) {
            String timeTaken = "It took " + sharedPreferences.getString("time_taken", "") +
                    " seconds to turn off your last alarm";
            timeTakenTextview.setText(timeTaken);
        }

        return view;
    }
}
