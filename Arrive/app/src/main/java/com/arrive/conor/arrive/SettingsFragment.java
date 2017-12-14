package com.arrive.conor.arrive;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;


import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    Button btnRingtone, btnSilence, btnDestination;
    Switch navigation;

    String chosenRingtoneUri;
    Ringtone ringtone = null;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "ALARM_INFO";

    FragmentManager manager;



    public SettingsFragment() {
    }// Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        manager = getChildFragmentManager();

        sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btnRingtone = (Button) view.findViewById(R.id.btnSettingsRingtone);
        btnRingtone.setOnClickListener(this);

        btnSilence = (Button) view.findViewById(R.id.btnSettingsSilence);
        btnSilence.setOnClickListener(this);

        btnDestination = (Button) view.findViewById(R.id.btnSettingsDestination);
        btnDestination.setOnClickListener(this);

        navigation = (Switch) view.findViewById(R.id.switchSettingsNav);
        navigation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                destinationShown(b);
            }
        });

        return view;
    }

    private void destinationShown(boolean switchOn) {
        if (switchOn) {
            //add to shared prefs
            editor.putBoolean("default_navigation_enabled", true);
        } else {
            //add to shared prefs
            editor.putBoolean("default_navigation_enabled", false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSettingsRingtone:
                updateRingtone();
                break;
            case R.id.btnSettingsSilence:
                silenceMethod();
                break;
            case R.id.btnSettingsDestination:
                changeDestination();
                break;
        }
    }

    private void updateRingtone() {

        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(getContext(),
                RingtoneManager.TYPE_RINGTONE);
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for Arrive:");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, defaultRingtoneUri);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        startActivityForResult(intent, 999);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 999) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri != null) {
                this.chosenRingtoneUri = uri.toString();
                ringtone = RingtoneManager.getRingtone(getContext(), uri);
            } else {
                ringtone = RingtoneManager.getRingtone(getContext(),
                        RingtoneManager.getActualDefaultRingtoneUri(getContext(),
                                RingtoneManager.TYPE_ALARM));
                this.chosenRingtoneUri = null;
            }
        }
        //Update default ringtone in shared preferences
        editor.putString("default_ringtone", chosenRingtoneUri.toString()).commit();
        Toast.makeText(getContext(), "Default ringtone: " +
                String.valueOf(ringtone.getTitle(getContext())), Toast.LENGTH_SHORT).show();
    }

    private void silenceMethod() {
        SilenceAlarmFragment silenceAlarmFragment = new SilenceAlarmFragment();
        silenceAlarmFragment.show(manager, "SilenceAlarmFragment");
    }

//    @Override
//    public void onSilenceMethodSelected(String message) {
//        editor.putString("default_silence_method", message).commit();
//        Toast.makeText(getContext(), "Default silence method: " + message, Toast.LENGTH_SHORT).show();
//    }

    private void changeDestination() {
        InputDestinationFragment inputDestinationFragment = new InputDestinationFragment();
        inputDestinationFragment.show(manager, "InputDestinationFragment");
    }

//    @Override
//    public void setDestination(String streetNumber, String streetName, String city, String postcode) {
//        String destination = streetNumber.trim() + " "
//                + streetName.trim() + " "
//                + city.trim() + " "
//                + postcode.trim();
//        editor.putString("default_destination", destination).commit();
//        Toast.makeText(getContext(), "Default destination: " + destination, Toast.LENGTH_SHORT).show();
//    }
}
