package com.example.kelvinharron.qralarm;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This class is used when a user is adding a new alarm and chooses the location alarm type.
 * This class will open a Google Maps screen allowing a user to long press and add a marker of their location.
 */
public class ActivityMaps extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    /**
     * Main class for the GoogleMap object that will allow us to setup an entry point to creating and interacting with GoogleMaps.
     */
    private GoogleMap googleMap;

    private SharedPreferences sharedPrefs;

    private SharedPreferences.Editor editor;

    private String testVar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toast.makeText(getApplicationContext(), "To set your location simply long press on the map", Toast.LENGTH_LONG).show();
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMapLongClickListener(this);
        setUpMap();
    }

    /**
     * Method sets up camera view with predefined location and uses functions to set marker on map at specific zoom level.
     */
    private void setUpMap() {
        LatLng userLocation = new LatLng(54.584782, -5.936335); // default location set to Queen's University
        CameraUpdate centre = CameraUpdateFactory.newLatLng(userLocation);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(50);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.addMarker(new MarkerOptions().position(userLocation).title("Home")); // string on marker when user clicks
        googleMap.moveCamera(centre);
        try {
            googleMap.setMyLocationEnabled(true);
            googleMap.animateCamera(zoom);
        } catch (SecurityException securityException) {
            securityException.printStackTrace();
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (googleMap != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Home")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            appendLocationString(latLng);
            Toast.makeText(getApplicationContext(), "Home Location set", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void appendLocationString(LatLng latLng) {
        StringBuilder builder = new StringBuilder();
        builder.append(latLng.latitude);
        builder.append(",");
        builder.append(latLng.longitude);
        editor = sharedPrefs.edit();
        editor.putString("location", builder.toString());
        editor.apply();
        testVar = sharedPrefs.getString("location", "");
        Log.e(testVar, "Location Log Test");
    }
}
