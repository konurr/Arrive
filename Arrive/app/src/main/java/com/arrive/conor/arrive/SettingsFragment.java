package com.arrive.conor.arrive;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {
    public static final String PREFS_NAME = "ALARM_INFO";
    SharedPreferences sharedPreferences;

    private ListView settingsListview;
    private ArrayAdapter<String> listAdapter;

    public SettingsFragment() {
    }// Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        return view;
    }
}
