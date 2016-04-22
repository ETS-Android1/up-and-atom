package com.example.kelvinharron.qralarm;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;


/**
 * Created by Hannah on 12/04/2016.
 */
public class DismissAlarmActivity extends AppCompatActivity implements OnCompleteListener, LocationListener {


    //set integers required for alarm activity
    final Context context = this;
    AudioManager audioManager;
    int prevVolume;
    private IntentIntegrator integrator;
    Uri uriAlarm;
    Ringtone ring;
    String qrResult;
    AlarmSQLiteHelper alarmSQLiteHelper;
    Alarm alarm;
    MediaPlayer mediaPlayer;
    //TODO sort out actual setting preferences to set overrideCode
    String prefOverrideCode = "1";

    public static double HOME_LATITUDE = 1;
    public static double HOME_LONGITUDE = 1;

    public static double LOCATION_MARGIN = 1;


    Button snooze;
    Button scan;
    Button override;

    TextView title;
    TextView scanItem;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied;
    GoogleApiClient googleApiClient;
    private LocationManager locationManager;
    public Location location;
    public double longitude;
    public double latitude;
    String provider;

    public boolean isGPSEnabled;
    public boolean isNetworkEnabled;
    public boolean locationServiceAvailable;


    //The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute

    private final static boolean forceNetwork = false;

    private static DismissAlarmActivity instance = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dismiss_layout);
        alarmSQLiteHelper = new AlarmSQLiteHelper(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        long alarmId = extras.getLong("alarmID");
        System.out.print("Outputting ID: " + alarmId + "\n");
        alarm = alarmSQLiteHelper.readAlarm(alarmId);

        title = (TextView) findViewById(R.id.dismiss_title);
        title.setText(alarm.getName());

        scanItem = (TextView) findViewById(R.id.scan_item);
        scanItem.setText(alarm.getMemo());

        if (!alarm.isRecurring()) {
            alarm.setOn(false);
            alarmSQLiteHelper.update(alarm);
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, alarm.getSound());
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (IOException iOException) {
            ring = RingtoneManager.getRingtone(getApplicationContext(), alarm.getSound());
            ring.play();
        }

        setupSnooze();
        setupOverride();

        if (isPrefLocation()) {
            setupScan();
        } else {
            setupDismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            getMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void getMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else {
            this.longitude = 0.0;
            this.latitude = 0.0;
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (forceNetwork) isGPSEnabled = false;

            if (!isNetworkEnabled && !isGPSEnabled) {
                // cannot get location
                this.locationServiceAvailable = false;
            }
            //else
            {
                this.locationServiceAvailable = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        provider = LocationManager.NETWORK_PROVIDER;
                    }
                }//end if

                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        provider = LocationManager.GPS_PROVIDER;
                    }
                }
            }
        }
    }

    public void setupSnooze() {
        snooze = (Button) findViewById(R.id.snooze_button);
        snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlarmScheduler().ignoreAlarm(getApplicationContext(), 5);
                stop();
            }
        });
    }

    public void setupScan() {
        scan = (Button) findViewById(R.id.scan_button);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prompt = "Scan QR Code";
                startScanning(prompt);
            }
        });
    }

    public void setupDismiss() {
        scan = (Button) findViewById(R.id.scan_button);
        scan.setText("Dismiss");
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = integrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String data[] = scanResult.getContents().split("\n");
            StringBuilder stringBuilder = new StringBuilder();
            for (String string : data) {
                stringBuilder.append(string);
            }
            qrResult = stringBuilder.toString();

            if (qrResult.equals(alarm.getQrResult())) {
                Toast.makeText(this, "Dismissing Alarm", Toast.LENGTH_SHORT).show();
                stop();
            } else {
                String prompt = "Wrong QR Code - Please Try Again";
                startScanning(prompt);
            }
        }
    }

    public void setupOverride() {
        override = (Button) findViewById(R.id.override_button);
        override.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.support.v4.app.FragmentManager manager = getSupportFragmentManager(); // fragment manager allows us to instantiate the dialog fragment
                final AlarmOverrideDialogFragment dialogFragment = new AlarmOverrideDialogFragment(); // create an object of the dialogfragment that will allow us to display it once a button is pressed.
                dialogFragment.show(manager, "fragment");
            }
        });
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        } else if (ring != null) {
            ring.stop();
        }
        //audioManager.setStreamVolume(AudioManager.STREAM_ALARM, prevVolume, AudioManager.FLAG_ALLOW_RINGER_MODES);
        finish();
    }

    public void startScanning(String prompt) {
        integrator = new IntentIntegrator(DismissAlarmActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(prompt);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    public void onComplete(String overrideCode) {
        if (prefOverrideCode.equals(overrideCode)) {
            stop();
        } else {
            Toast.makeText(getApplicationContext(), "Wrong override code, please try again.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    private boolean isPrefLocation() {
        boolean atPrefLocation;
        getMyLocation();
        Location localLocation = location;
        Location prefLocation = new Location(provider);
        prefLocation.setLatitude(HOME_LATITUDE);
        prefLocation.setLongitude(HOME_LONGITUDE);
        if (localLocation.distanceTo(prefLocation) < LOCATION_MARGIN && this.locationServiceAvailable){
            atPrefLocation = true;
        } else {
            atPrefLocation = false;
        }
        return atPrefLocation;
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

