package com.arrive.conor.arrive;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    android.app.FragmentManager manager = getFragmentManager();
    CreateAlarmFABFragment createAlarmFABFragment;

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
                //Show FAB on alarm page
                manager.beginTransaction().show(createAlarmFABFragment).commit();
                return true;
            case R.id.navigation_history://Display alarm history frag
                manager.beginTransaction().hide(createAlarmFABFragment).commit();
                return true;
            case R.id.navigation_settings://Switch to settings activity
                manager.beginTransaction().hide(createAlarmFABFragment).commit();
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creates bottom nav bar
        // Sets listener for selected bottom nav bar item
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Start app showing FAB fragment for creating new alarm
        createAlarmFABFragment = new CreateAlarmFABFragment();
        manager.beginTransaction().replace(
                R.id.contentLayout,
                createAlarmFABFragment,
                createAlarmFABFragment
                        .getTag())
                        .commit();
    }

}
