package com.example.kelvinharron.qralarm;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
 * The ActivityMaps.class file is used to handle the logic of the preferences menu where the user
 * can set their location. As part of the location aspect of this application, if a user is away
 * from a home location when the alarm goes off, we can't expect them to scan the normal object code
 * so instead we allow them to dismiss the alarm from view to avoid annoyance.
 */
public class ActivityMaps extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    /**
     * Main class for the GoogleMap object that will allow us to setup an entry point to creating and interacting with GoogleMaps.
     */
    private GoogleMap googleMap;

    /**
     * Gets a reference to the shared preferences object that allows us to get and set the value
     * of the location key.
     */
    private SharedPreferences sharedPrefs;

    /**
     * Allows us to edit and commit the values of the latLng to the preferences file.
     */
    private SharedPreferences.Editor editor;

    /**
     * Stores a reference to the latLng of the user when we commit the changes to the preferences.
     */
    private String prefsLocation;


    /**
     * When the user opens this activity, they are given basic instructions on what to do. We need
     * them to verify their home location by long pressing on the map where they live.
     * We use this value each time the alarm goes off as an exception where they cannot scan the
     * object code as they are likely away from it if they are not at the home location.
     *
     * @param savedInstanceState
     */
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
        googleMap.getUiSettings().setMapToolbarEnabled(false); // disables google specific controls from appearing
        googleMap.addMarker(new MarkerOptions().position(userLocation).title("Home")); // string on marker when user clicks
        googleMap.moveCamera(centre);
        try {
            googleMap.setMyLocationEnabled(true);
            googleMap.animateCamera(zoom);
        } catch (SecurityException securityException) {
            securityException.printStackTrace();
        }
    }

    /**
     * Method handles the action when a user long presses on the map to set their location.
     * When we recieve this input, we toast as a confirmation to the user and close the activity
     * because we do not require them to commit more than one home location.
     *
     * @param latLng
     */
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

    /**
     * As the value of the location is stored as a latLng, we need to append it before storing it
     * as a unique value. We use a StringBuilder to split the values in half at the comma mark so we
     * can extract the values in a usable state. By doing this, we use the prefs editor to commit
     * the new values to the preferences file once the user has long pressed to state their home
     * location.
     *
     * @param latLng
     */
    private void appendLocationString(LatLng latLng) {
        StringBuilder builder = new StringBuilder();
        builder.append(latLng.latitude);
        builder.append(","); // split the latLng in the middle
        builder.append(latLng.longitude);
        editor = sharedPrefs.edit();
        editor.putString("location", builder.toString());
        editor.apply();
        prefsLocation = sharedPrefs.getString("location", "");
    }
}