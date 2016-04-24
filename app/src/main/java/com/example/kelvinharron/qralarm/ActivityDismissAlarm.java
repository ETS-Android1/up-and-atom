package com.example.kelvinharron.qralarm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;


/**
 * Created by Hannah Butler (40004276)
 * <p/>
 * Class called when a scheduled alarm activates. The activity inflates the
 * dialog_dismiss_layout.xml, The alarm Id is extracted from the Bundle, passed to the
 * AlarmSQLite database to read/extract the scheduled alarm object. This provides
 * the title, an indication of the item to be scanned and the URI of the Ringtone.
 * <p/>
 * The TextViews for the title and Memo/Item to be scanned instantiated are gets the bundles
 * alarm id, the MediaPlayer plays a looping alarm indicated by URI passed from database and
 * instantiates the buttons.
 * <p/>
 * If the User is located in their home location set in the app setting, the Scan button is enabled
 * which requires a QR code to dismiss the alarm (stop playing alarm and dismiss dialog_activity).
 * If the User is not located in their home location, then the 3rd button is set to be a simple
 * dismiss button.
 */
public class ActivityDismissAlarm extends AppCompatActivity implements OnCompleteListener,
        LocationListener {
    // SQLite database adapter used to retrieve the alarm object for a given alarmId.
    private SQLiteHelperAlarm SQLiteHelperAlarm;
    // Alarm object which indicates the title, memo (indication of item to be scanned), the QR code
    // string location (URI) of the alarm sound to be played.
    private Alarm alarm;
    // TextView to present the Title of the Alarm
    TextView title;
    // TextView of a memo/note or indication of the item required to be scanned.
    TextView scanItem;
    // MediaPlayer used to control the playback of audio or in this case alarm file
    MediaPlayer mediaPlayer;
    // Used to play the ringtone in the event the media player fails
    Ringtone ring;
    // Utility class which helps ease integration with Barcode Scanner via {@link Intent}s
    private IntentIntegrator integrator;
    // Retains the result of the IntentIntegrator or Barcode/QR code. This is compared against the
    // QR code entered when the alarm was setup and retrieved from trhe database.
    String qrResult;
    // Object used to access the SharedPreferences of the app.
    SharedPreferences preferences;
    // The overrideCode set in app settings. Used to dismiss the alarm in the event that the QR
    // code is unexpectedly unavailable.
    String prefOverrideCode;
    // The latitude and longitude (with comma (",") as separator) set in shared preferences. This
    // value is set in app settings but by default is set to Queens University.
    String prefLatLng;
    // Button used to dismiss the alarm and reschedule another in 5 minutes
    Button snooze;
    // Button used to start scanner (QR code/Barcode). If codes match, the alarm is dismissed. If
    // not the alarm asks the user to try again. In the event that the user is outside their
    // pref. location then the button will simply dismiss the alarm.
    Button scan;
    // Button which starts a dialog to get request an override from the user.
    Button override;
    // Provides the class with access to the users location when the alarm goes off.
    private LocationManager locationManager;
    // Location object which represents the geographic location. In this case, the app uses the
    // latitude and longitude properties
    public Location location;
    public double longitude;
    public double latitude;
    // Retains which device location provider produced the current location
    String provider;
    // Indicates which locations service was enables or available
    public boolean isGPSEnabled;
    public boolean isNetworkEnabled;
    public boolean locationServiceAvailable;
    // Constant used to indicat the location permission request code
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    // Constant which indicates the margin for error when comparing the current user permission to
    // the pref. location provided by settings.
    public static double LOCATION_MARGIN = 100;
    // The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute
    // Ignore time
    private static final int IGNORE_TIME = 5;


    /**
     * Overrides Activity onCreate method, calls methods to instantiate buttons and TextViews,
     * instantiates the SQLite class, retrieves the bundled alarmID and uses it to retrieve the
     * scheduled alarm object.
     * <p/>
     * A mediaplayer object is used to play the alarm sound set when the alarm was added.
     */
    public void onCreate(Bundle savedInstanceState) {
        // call the parent onCreate method
        super.onCreate(savedInstanceState);
        // inflate activity_main xml
        setContentView(R.layout.dialog_dismiss_layout);
        // instantiate the alarm SQLite database
        SQLiteHelperAlarm = new SQLiteHelperAlarm(getApplicationContext());
        // get the alarm id bundled with the intent
        Bundle extras = getIntent().getExtras();
        long alarmId = extras.getLong("alarmID");
        // pass the alarmId to the readAlarm method which returns the scheduled alarm object
        alarm = SQLiteHelperAlarm.readAlarm(alarmId);
        // instantiate the Title TextView setting the value/text to the alarm name
        title = (TextView) findViewById(R.id.dismiss_title);
        title.setText(alarm.getName());
        // instantiate the Memo TextView setting the value/text to the alarm memo. This indicates
        // what item was scanned when the alarm was created
        scanItem = (TextView) findViewById(R.id.scan_item);
        scanItem.setText(alarm.getMemo());
        // check if the alarm is a recurring alarm. If not, update the alarm to be off so it is
        // not rescheduled without being manually set by the user
        if (!alarm.isRecurring()) {
            alarm.setOn(false);
            // update the database to indicate the alarm is off
            SQLiteHelperAlarm.update(alarm);
        }
        // Check if a mediaPlayer object already exists. In some instances this causes
        // mediaplayer streams can occur at once if this check is not inplace.
        if (mediaPlayer != null) {
            // release the resource if already in use
            mediaPlayer.release();
            mediaPlayer = null;
        }
        // Instantiate a Mediaplayer
        mediaPlayer = new MediaPlayer();
        // use try-catch to handle an error in the event that the file/alarm sound hasn't been
        // set
        try {
            // setup the mediaplayer to play the set alarm sound set when the alarm was created
            mediaPlayer.setDataSource(this, alarm.getSound());
            // get the audio system service
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            // check if the volume is not muted
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                // set what type of sound is going to be played
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                // loop the alarm
                mediaPlayer.setLooping(true);
                // prepare the play for synchronous playback
                mediaPlayer.prepare();
                // start the sound
                mediaPlayer.start();
            }
            // play it as a ringtone if the mediaplayer is unable to find the sound file
        } catch (IOException iOException) {
            ring = RingtoneManager.getRingtone(getApplicationContext(), alarm.getSound());
            ring.play();
        }
        // setup the snooze button which is used to disable the current alarm and reschedule
        // the alarm 5 minutes later. Implemented to allow the user to delay the current alarm.
        setupSnooze();
        // method used to retrieve the override code to enable user to dismiss the alarm in the
        // event that the code is unavailable
        getOverridePreference();
        // setup the override button so that the user can dismiss the alarm in the event that
        // they are unable to source the correct item.
        setupOverride();
        // check if the user is in their home location, if so enable the button to scan qr codes to
        // stop the alarm else use the setupDismiss method, which changes the function of the
        // button to stop the ringtone and dismiss the activity
        if (isPrefLocation()) {
            setupScan();
        } else {
            setupDismiss();
        }
    }

    /**
     * Retrieves the override code from the app preferences.
     */
    private void getOverridePreference() {
        // instantiate the preferences manager to enable the code to be retrieved
        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        // using override as a key set the prefOverrideCode for the activity
        prefOverrideCode = preferences.getString("override", "");

    }

    /**
     * Retrieves the user's home location set in shared preferences as a LatLng object.
     *
     * @return the user's specified home location as a LatLng object
     */
    private LatLng getLocationPreference() {
        // instantiate the preferences manager to enable the code to be retrieved
        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        // using location as a key set the prefOverrideCode for the activity
        prefLatLng = preferences.getString("location", "");
        // log the prefLatLng
        Log.e(prefLatLng, "SPLIT STRING");
        // split the prefLatLng String into an array
        String[] latLng = prefLatLng.split(",");
        // convert the String lat and long to double
        double lat = new Double(latLng[0]);
        double lng = new Double(latLng[1]);
        // log conversion
        Log.e(String.valueOf(lat), "LATITUDE AFTER SPLIT");
        Log.e(String.valueOf(lng), "LONGTITUDE AFTER SPLIT");
        // return the latitude and longitude as a LatLng object
        return new LatLng(lat, lng);
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
            // initialise latitude and longitude
            this.longitude = 0.0;
            this.latitude = 0.0;
            // instantiate locationmanager by getting the system service
            this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            // step through each of the location service providers
            if (!isNetworkEnabled && !isGPSEnabled) {
                // if none are available set the locationServiceAvailable to false, this will set
                // the Scan but to be Dismiss only.
                this.locationServiceAvailable = false;
            } else {
                // otherwise set the service available to true
                this.locationServiceAvailable = true;
                // if the network is available to use it to get the location
                if (isNetworkEnabled) {
                    // get the current location
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    // if not null set the last known location
                    if (locationManager != null) {
                        // extract location from the manager
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        // set the provider for use in the isPrefLocation method which sets the
                        // dismiss/scan buttons depending on location
                        provider = LocationManager.NETWORK_PROVIDER;
                    }
                }
                // if GPS is available use it the get the user current location
                if (isGPSEnabled) {
                    // get the current location
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        // extract location from the manager
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        // set the provider for use in the isPrefLocation method which sets the
                        // dismiss/scan buttons depending on location
                        provider = LocationManager.GPS_PROVIDER;
                    }
                }
            }
        }
    }
    /**
     * Method instantiates the snooze button and sets up the onClickEvent handler and listener
     */
    public void setupSnooze() {
        // instantiate snooze Button and attach view by the id set in the xml which if pressed
        // dismisses the current alarm and schedules another 5 min later by default.
        snooze = (Button) findViewById(R.id.snooze_button);
        snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlarmScheduler().ignoreAlarm(getApplicationContext(), IGNORE_TIME);
                stop();
            }
        });
    }


    /**
     * Method instantiates the scan button and sets up the onClickEvent handler and listener
     */
    public void setupScan() {
        // instantiate scan Button and attach view by the id set in the xml which if pressed
        // calls the QRCode/Barcode Scanner. Result is handled by onActivityResult method
        scan = (Button) findViewById(R.id.scan_button);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prompt = "Scan QR Code";
                startScanning(prompt);
            }
        });
    }

    /**
     * Method instantiates the dismiss button and sets up the onClickEvent handler and listener
     */
    public void setupDismiss() {
        // instantiate the scan button as a dismiss Button and attach view by the id set in the xml
        // which if pressed calls the stop() method which dismissed the alarm
        scan = (Button) findViewById(R.id.scan_button);
        scan.setText("Dismiss");
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
    }

    /**
     * Handles the result of other intent objects in the case of this activity, it handles the
     * result of QRCode/Barcode Scanner. If the scanned code matches the code stored in the alarm
     * object the alarm is dismissed. Otherwise, it request the code or anther code to be scanned.
     * @param requestCode - request code originally supplied to startActivity
     * @param resultCode - integer result code returned by child actvity
     * @param intent - the intent object which contains additional data.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // parse the results of the activity
        IntentResult scanResult = integrator.parseActivityResult(requestCode, resultCode, intent);
        try {
            // if result or contains of result is not null convert the data array into a string
            if (scanResult != null) {
                if (scanResult.getContents() != null) {
                    // split the contents using \n newline separator
                    String data[] = scanResult.getContents().split("\n");
                    // instantiate the stringbuilder object to convert the array into a single
                    // string
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String string : data) {
                        stringBuilder.append(string);
                    }
                    // set the builder value to the qrResult
                    qrResult = stringBuilder.toString();
                    // if it equals the store alarm qr code dismiss the alarm
                    if (qrResult.equals(alarm.getQrResult())) {
                        // make a toast to indicate to the user that the alarm is being dismissed
                        Toast.makeText(this, "Dismissing Alarm", Toast.LENGTH_SHORT).show();
                        // call the stop method which stops the ringtone and releases the resource
                        stop();
                    } else {
                        // if it doesn't equal the stored value, retry the scan
                        String prompt = "Wrong QR Code - Please Try Again";
                        startScanning(prompt);
                    }
                }
            }
            // catch the event that the detected code is null
        } catch (NullPointerException nullPointerException) {
            // make a toast indicating the result was null
            Toast.makeText(this, "Code not detected", Toast.LENGTH_SHORT).show();
        }
    }

    public void setupOverride() {
        override = (Button) findViewById(R.id.override_button);
        override.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.support.v4.app.FragmentManager manager = getSupportFragmentManager(); // fragment manager allows us to instantiate the dialog fragment
                final DialogAlarmOverride dialogFragment = new DialogAlarmOverride(); // create an object of the dialogfragment that will allow us to display it once a button is pressed.
                dialogFragment.show(manager, "fragment");
            }
        });
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        } else if (ring != null) {
            ring.stop();
        }
        finish();
    }

    public void startScanning(String prompt) {
        integrator = new IntentIntegrator(ActivityDismissAlarm.this);
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
        LatLng latLng = getLocationPreference();
        getMyLocation();
        // checking if location can be found
        Location localLocation = location;
        Location prefLocation = new Location(provider);
        prefLocation.setLatitude(latLng.latitude);
        prefLocation.setLongitude(latLng.longitude);
        if (localLocation.distanceTo(prefLocation) < LOCATION_MARGIN && this.locationServiceAvailable) {
            atPrefLocation = true;
        } else {
            atPrefLocation = false;
        }
        return atPrefLocation;
    }

    /**
     * Location is only required when alarm first goes off therefore
     * onStatusChanged/onProviderEnabled/Disabled has null implementation.
     */
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