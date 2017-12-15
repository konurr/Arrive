package com.arrive.conor.arrive;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class NavigationActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, InputDestinationFragment.Communicator {

    private GoogleMap mMap;
    final private int REQUEST_COARSE_ACCESS = 123;
    boolean permissionGranted = false;
    LocationManager lm;
    LocationListener locationListener;
    LatLng p;

    String defaultDestination;
    Button btnDestination;
    Button btnNavigate;

    FragmentManager manager = getFragmentManager();


    public static final String PREFS_NAME = "ALARM_INFO";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.remove("new_destination").commit(); //Clears down previously set new destination

        btnDestination = (Button) findViewById(R.id.setDestination);
        btnDestination.setOnClickListener(this);

        btnNavigate = (Button) findViewById(R.id.btnNavigate);
        btnNavigate.setOnClickListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        //----remove the location listener----
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_COARSE_ACCESS);
            return;
        } else {
            permissionGranted = true;
        }
        if (permissionGranted) {
            lm.removeUpdates(locationListener);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //---use the LocationManager class to obtain locations data---
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_COARSE_ACCESS);
            return;
        } else {
            permissionGranted = true;
        }
        if (permissionGranted) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        }

        try { //Try to get destination location to place pin
            p = getLocationFromAddress(sharedPreferences.getString("alarm_destination", ""));
            mMap.addMarker(new MarkerOptions().position(p).title("Destination")).showInfoWindow();
        } catch (IllegalArgumentException e) { //If network/location unavailable then use last known location
            p = new LatLng(
                    lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(),
                    lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());
            mMap.addMarker(new MarkerOptions().position(p).title("Last known location")).showInfoWindow();
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p, 12.0f));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_ACCESS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                } else {
                    permissionGranted = false;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException | IllegalArgumentException ex) { //IllegalArgumentException is thrown when internet connection is unavailable
            Toast.makeText(this, "No internet connection, using last known location", Toast.LENGTH_SHORT).show();
            try {
                p1 = new LatLng(
                        lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(),
                        lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());
            } catch (SecurityException e) {
                Toast.makeText(this, "Unable to get last known location. Navigation unavailable", Toast.LENGTH_SHORT).show();
                Log.e("ERROR", "Location unavailable, navigation unavailable");
                p1 = null;
            }
        }

        return p1;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setDestination: //Launch InputDestinationFragment
                InputDestinationFragment inputDestinationFragment = new InputDestinationFragment();
                inputDestinationFragment.show(manager, "InputDestinationFragment");
                break;
            case R.id.btnNavigate: //Launch intent to Google Maps App
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" +
                        sharedPreferences.getString("new_destination",
                                sharedPreferences.getString("alarm_destination", "")));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
        }
    }

    @Override
    public void setDestination(String streetNumber, String streetName, String city, String postcode) {
        String destination = streetNumber.trim() + " " + streetName.trim() + " " + city.trim() + " " + postcode.trim();
        Log.i("NEW DESTINATION", destination);

        p = getLocationFromAddress(destination);
        if (p != null) {
            mMap.addMarker(new MarkerOptions().position(p).title("Destination")).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p, 12.0f));

            editor.putString("new_destination", destination).commit();
            Toast.makeText(this, "Destination updated:" + destination, Toast.LENGTH_SHORT).show();
        }
    }

    private class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location loc) {

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
