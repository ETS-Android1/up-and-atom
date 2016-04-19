package com.example.kelvinharron.qralarm;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/*TODO <application
android:allowBackup="true">
        ...
        <service android:name=".GeofenceTransitionsIntentService"/>
        <application/>
*/

/**
 * Created by Conor on 19/04/2016.
 */
public class GeofenceController {

    private final String TAG = GeofenceController.class.getName();

    private Context context;
    private GoogleApiClient googleApiClient;
    private Gson gson;
    private SharedPreferences preferences;
    private List<GeoAlarm> geoAlarms;
    public List<GeoAlarm> getGeoAlarms() {
        return geofences;
    }

    private List<GeoAlarm> geoAlarmsToRemove;

    private Geofence geofencetoAdd;
    private GeoAlarm geoAlarmToAdd;

    private static GeofenceController INSTANCE;

    public static GeofenceController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GeofenceController();
        }
        return INSTANCE;
    }

    public void init(Context context){
        this.context = context.getApplicationContext();
        gson = new Gson();
        geoAlarms = new ArrayList<>();
        geoAlarmsToRemove = new ArrayList<>();
        preferences = this.context.getSharedPreferences(Constants.SharefPrefs.Geofences, Context.MODE_PRIVATE);

    }


}
