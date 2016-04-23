package com.example.kelvinharron.qralarm;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This class is used when a user is adding a new alarm and chooses the location alarm type. This class will open a Google Maps screen allowing a user to long press and add a marker of their location.
 * TODO: Create a Dialog when the user first opens the map view with simple instructions on what to do.
 * TODO: Add a button to the map to confirm the location.
 * TODO: Get the location and store it in the database.
 * TODO: Remove Google buttons that appear on the bottom right of the map when a user taps their location marker.
 */
public class ActivityMaps extends FragmentActivity {

    /**
     * Main class for the GoogleMap object that will allow us to setup an entry point to creating and interacting with GoogleMaps.
     */
    private GoogleMap googleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toast.makeText(getApplicationContext(), "To set your location simply long press on the map", Toast.LENGTH_LONG).show();
        setUpMapIfNeeded();
        setMarkerLocation();
    }

    /**
     * Method where a user can long press a location on a map to set as their location.
     * TODO: get and store LatLng from user.
     */
    private void setMarkerLocation() {
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                googleMap.clear();
                Toast.makeText(getApplicationContext(), "Home location set", Toast.LENGTH_LONG).show();
                googleMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
    }

    /**
     * Method checks if we have already instantiated a googlemap view.
     * If not instantiated, we use the supportMapFragment to obtain a reference to the map.
     * Calls setUpMap method.
     */
    private void setUpMapIfNeeded() {
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * Method sets up camera view with predefined location and uses functions to set marker on map at specific zoom level.
     */
    private void setUpMap() {
        LatLng userLocation = new LatLng(54.5731707, -5.9428905); // default for now
        CameraUpdate centre = CameraUpdateFactory.newLatLng(userLocation);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(20);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.addMarker(new MarkerOptions().position(userLocation).title("My place")); // string on marker when user clicks
        googleMap.moveCamera(centre);
        googleMap.animateCamera(zoom);
    }
}