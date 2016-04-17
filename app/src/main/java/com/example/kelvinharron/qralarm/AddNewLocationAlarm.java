package com.example.kelvinharron.qralarm;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by peter on 16/04/2016.
 */
public class AddNewLocationAlarm extends AppCompatActivity implements OnMapReadyCallback {

    private Toolbar toolbar;

    public AddNewLocationAlarm() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location_alarm);

        toolbar = (Toolbar) findViewById(R.id.anlToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MapFragment mp = (MapFragment) getFragmentManager().findFragmentById(R.id.nlamap);
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
       // Test Data
        LatLng sydney = new LatLng(-33.867, 151.206);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 3));
        map.animateCamera(CameraUpdateFactory.zoomIn());
        map.animateCamera(CameraUpdateFactory.zoomTo(18),5000,null);
        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));


    }

}
    /**
     * This method inflates the leaderboard fragment view object so that it displays on our activity interface.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return

  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_new_location_alarm, container, false);
    }*/



