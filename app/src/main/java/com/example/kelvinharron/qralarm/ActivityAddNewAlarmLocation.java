package com.example.kelvinharron.qralarm;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * UNUSED CLASS FILE
 * As we did not implement geo alarms, we have disabled this activity from being accessible by the user.
 * <p/>
 * Created by Peter on 16/04/2016.
 * This class allows the user to set a new alarm based on location. Activated by the user pressing
 * the floating action button on the main menu
 */
public class ActivityAddNewAlarmLocation extends AppCompatActivity implements OnMapReadyCallback {

    /**
     * Alarm id which is passed from the parent activity based on the number of alarms
     */
    private long alarmId;

    /**
     * Alarm object which is created by Activity
     */
    private Alarm alarm;
    /**
     * Name of the alarm
     */
    private EditText location_name;

    /**
     * Alarm memo
     */
    private EditText location_memo;

    /**
     * ArrayList for checkbox repetitions
     */
    ArrayList<Integer> dayArray = new ArrayList<>();

    /**
     * Sets whether the alarm repeats or not
     */

    private boolean recurringTimeAlarm;

    /**
     * Checkboxes representing the days of the week
     * The alarm will repeat on days that are checked
     */

    private CheckBox sunCB;
    private CheckBox monCB;
    private CheckBox tueCB;
    private CheckBox thuCB;
    private CheckBox friCB;
    private CheckBox satCB;
    private CheckBox wedCB;

    /**
     * Allows us to set the toolbar
     */
    private Toolbar toolbar;

    /**
     * Button to show the QR Scanner
     */
    private Button scanQR;

    /**
     * Button to set Alarm Sound
     */
    private TextView alarmSound;
    private TextView alarmTitle;
    private String qrResult;
    private TextView tester;
    /**
     *
     */
    private IntentIntegrator integrator;

    private Uri chosenRingtone;

    //private SeekBar volumeControl;
    private float volume;

    SQLiteHelperAlarm db;
    public static final float VOLUME_MODIFIER = 10;

    /**
     * Start of activity lifecycle. Sets the view of the ActivityAddNewAlarmLocation activity
     * and calls the methods enabling behavior.
     *
     * @param savedInstanceState
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm_location);

        // Displays the toolbar, its title & the back button
        toolbar = (Toolbar) findViewById(R.id.anlToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Links the map fragment to the XML
        MapFragment mp = (MapFragment) getFragmentManager().findFragmentById(R.id.location_map);
        mp.getMapAsync(this);

        db = new SQLiteHelperAlarm(this);

        setLocationAlarmName();
        setLocationAlarmMemo();
        // setAlarm();
        setScanQR();
        setRingtone();
        confirmAlarm();

        dayArray = new ArrayList<>();
        sunCB = (CheckBox) findViewById(R.id.locationSunday);
        sunCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sunCB.isChecked()) {
                    dayArray.add(Integer.valueOf(1));
                } else {
                    dayArray.remove(Integer.valueOf(1));
                }
            }
        });

        monCB = (CheckBox) findViewById(R.id.locationMonday);
        monCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monCB.isChecked()) {
                    dayArray.add(Integer.valueOf(2));
                } else {
                    dayArray.remove(Integer.valueOf(2));
                }
            }
        });

        tueCB = (CheckBox) findViewById(R.id.locationTuesday);
        tueCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tueCB.isChecked()) {
                    dayArray.add(Integer.valueOf(3));
                } else {
                    dayArray.remove(Integer.valueOf(3));
                }
            }
        });

        wedCB = (CheckBox) findViewById(R.id.locationWednesday);
        wedCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wedCB.isChecked()) {
                    dayArray.add(Integer.valueOf(4));
                } else {
                    dayArray.remove(Integer.valueOf(4));
                }
            }
        });

        thuCB = (CheckBox) findViewById(R.id.locationThursday);
        thuCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thuCB.isChecked()) {
                    dayArray.add(Integer.valueOf(5));
                } else {
                    dayArray.remove(Integer.valueOf(5));
                }
            }
        });

        friCB = (CheckBox) findViewById(R.id.locationFriday);
        friCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friCB.isChecked()) {
                    dayArray.add(Integer.valueOf(6));
                } else {
                    dayArray.remove(Integer.valueOf(6));
                }
            }
        });

        satCB = (CheckBox) findViewById(R.id.locationSaturday);
        satCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (satCB.isChecked()) {
                    dayArray.add(Integer.valueOf(7));
                } else {
                    dayArray.remove(Integer.valueOf(7));
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {

        LatLng sydney = new LatLng(-33.867, 151.206);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 3));
        map.animateCamera(CameraUpdateFactory.zoomIn());
        map.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
    }

    /**
     * Set the alarm name
     */
    private void setLocationAlarmName() {
        location_name = (EditText) findViewById(R.id.time_alarm_name);
        location_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                location_name.setText(v.getText().toString());
                return false;
            }
        });
    }

    /**
     * Set the alarm memo
     */
    private void setLocationAlarmMemo() {
        location_memo = (EditText) findViewById(R.id.location_alarm_memo);
        location_memo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                location_memo.setText(v.getText().toString());
                return false;
            }
        });
    }

    /**
     * Button for bringing up QR scanner
     */
    private void setScanQR() {
        scanQR = (Button) findViewById(R.id.QRButton);

        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                integrator = new IntentIntegrator(ActivityAddNewAlarmLocation.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan QR Code");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    /**
     * Method for setting the alarm ringtone
     */
    private void setRingtone() {
        alarmTitle = (TextView) findViewById(R.id.alarm_sound);
        alarmTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAvailableRingtones();
            }
        });
        alarmSound = (TextView) findViewById(R.id.get_ringtone);
        chosenRingtone = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(this, chosenRingtone);
        alarmSound.setText(ringtone.getTitle(getApplicationContext()));

        alarmSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAvailableRingtones();
            }
        });
    }

    private void getAvailableRingtones() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        startActivityForResult(intent, 5);
    }

    private void createAlarm() {

        alarm = new Alarm();
        //alarmId = 1;
        //alarmId = db.getNewId();
        alarm.setName(location_name.getText().toString());
        alarm.setMemo(location_name.getText().toString());

        // setting the days on which the alarm occurs
        Integer[] days = new Integer[dayArray.size()];
        Collections.sort(dayArray);
        dayArray.toArray(days);
        alarm.setDays(days);

        if (dayArray.isEmpty() || dayArray.contains(Integer.valueOf(0))) {
            alarm.setRecurring(false);
        } else {
            alarm.setRecurring(true);
        }

        //alarm.setHour();
        //alarm.setMin();
        alarm.setSound(chosenRingtone.toString());
        alarm.setVolume(volume / VOLUME_MODIFIER);
        alarm.setQrResult(qrResult);
        alarm.setOn(true);

        //return alarm;
    }

    public void scheduleAlarm() {
        AlarmScheduler alarmScheduler = new AlarmScheduler();
        alarm.setId(alarmId);
        alarmScheduler.setAlarm(getApplicationContext(), this.alarm);
    }

    public void storeAlarm() {
        alarmId = db.createAlarm(this.alarm);
    }

    /**
     * Confirm the alarm settings & push to DB
     */

    private void confirmAlarm() {

        FloatingActionButton button = findViewById(R.id.confirmButton);
        button.setOnClickListener(v -> {
            createAlarm();
            storeAlarm();
            scheduleAlarm();
            Toast.makeText(getApplicationContext(), "Alarm created", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}