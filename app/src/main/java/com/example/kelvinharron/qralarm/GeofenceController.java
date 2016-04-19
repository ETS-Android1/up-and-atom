package com.example.kelvinharron.qralarm;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;

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

    private List<Geofence> geofences;

    public List<Geofence> getGeofences() {
        return geofences;
    }

    private List<Geofence> geofencesToRemove;

    private Geofence geofencetoAdd;

    private static GeofenceController INSTANCE;

    public static GeofenceController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GeofenceController();
        }
        return INSTANCE;
    }


}
