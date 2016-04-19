package com.example.kelvinharron.qralarm;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Peter on 16/04/2016.
 * This screen allows the user to set a new alarm based on location
 */
public class AddNewLocationAlarm extends AppCompatActivity implements OnMapReadyCallback {

    private Toolbar toolbar;

    /**
     * Default Constructor
     */

    public AddNewLocationAlarm() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location_alarm);

        // Displays the toolbar, its title & the back button
        toolbar = (Toolbar) findViewById(R.id.anlToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Links the map fragment to the XML
        MapFragment mp = (MapFragment) getFragmentManager().findFragmentById(R.id.location_map);
        mp.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
/*
        GoogleMap moveMap;
        moveMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.nlamap)).getMap();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        moveMap.setMyLocationEnabled(true);
        moveMap.getUiSettings().setZoomControlsEnabled(true);
        moveMap.getUiSettings().setMyLocationButtonEnabled(true);
        moveMap.getUiSettings().setZoomGesturesEnabled(true);
*/
       // Test Data - Remove
        LatLng sydney = new LatLng(-33.867, 151.206);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 3));
        map.animateCamera(CameraUpdateFactory.zoomIn());
        map.animateCamera(CameraUpdateFactory.zoomTo(18),2000,null);
        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
    }
}
