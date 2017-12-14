package com.arrive.conor.arrive;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SilenceAlarmFragment.Communicator, InputDestinationFragment.Communicator{

    android.app.FragmentManager manager = getFragmentManager();
    CreateAlarmFABFragment createAlarmFABFragment;
    HistoryFragment historyFragment;
    SettingsFragment settingsFragment;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "ALARM_INFO";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) { //Determines which nav item is selected
            return displayFrag(item);
        }
    };

    protected boolean displayFrag(MenuItem item){
        switch (item.getItemId()) {
            case R.id.navigation_alarms://Display list of alarms frag
                //Show FAB alarm page
                manager.beginTransaction().replace(
                        R.id.contentLayout,
                        createAlarmFABFragment
                ).commit();
                return true;
            case R.id.navigation_history://Display alarm history frag
                manager.beginTransaction().replace(
                        R.id.contentLayout,
                        historyFragment
                ).commit();
                return true;
            case R.id.navigation_settings://Switch to settings activity
                manager.beginTransaction().replace(
                        R.id.contentLayout,
                        settingsFragment
                ).commit();
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Creates bottom nav bar
        // Sets listener for selected bottom nav bar item
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Initialize the fragments
        createAlarmFABFragment = new CreateAlarmFABFragment();
        historyFragment = new HistoryFragment();
        settingsFragment = new SettingsFragment();

        //Start app showing FAB fragment for creating new alarm
        manager.beginTransaction().replace(
                R.id.contentLayout,
                createAlarmFABFragment,
                createAlarmFABFragment
                        .getTag())
                        .commit();
    }

    @Override
    public void onSilenceMethodSelected(String message) {
        editor.putString("default_silence_method", message).commit();
        Toast.makeText(this, "Default silence method: " + message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void setDestination(String streetNumber, String streetName, String city, String postcode) {
        String destination = streetNumber.trim() + " "
                + streetName.trim() + " "
                + city.trim() + " "
                + postcode.trim();
        editor.putString("default_destination", destination).commit();
        Toast.makeText(this, "Default destination: " + destination, Toast.LENGTH_SHORT).show();
    }
}
