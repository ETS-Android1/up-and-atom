package com.example.kelvinharron.qralarm;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Peter on 16/04/2016.
 * This screen allows the user to set a new alarm based on location
 */
public class AddNewLocationAlarm extends AppCompatActivity implements OnMapReadyCallback {

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
     * Alarm memo - time alarm only
     */
    private EditText location_memo;

    /**
     * Sets whether the alarm repeats or not
     */

    /**
     * ArrayList for checkbox repetitions
     */
    ArrayList<Integer> dayArray = new ArrayList<>();

    private boolean recurringTimeAlarm;

    private CheckBox sunCB;
    private CheckBox monCB;
    private CheckBox tueCB;
    private CheckBox thuCB;
    private CheckBox friCB;
    private CheckBox satCB;
    private CheckBox wedCB;

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

    AlarmSQLiteHelper db;
    public static final float VOLUME_MODIFIER = 10;


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

        db = new AlarmSQLiteHelper(this);

        setLocationAlarmName();
        setLocationAlarmMemo();
        // setAlarm();
        setScanQR();
        setRingtone();
        //setSeekbar();
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
                //tester.setText(dayArray.toString());
                //System.out.print();
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
                //tester.setText(dayArray.toString());

//                System.out.print(Arrays.toString(dayArray.toArray()));
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
                //tester.setText(dayArray.toString());

//                System.out.print(Arrays.toString(dayArray.toArray()));
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
                //tester.setText(dayArray.toString());

//                System.out.print(Arrays.toString(dayArray.toArray()));
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
               // tester.setText(dayArray.toString());

//                System.out.print(Arrays.toString(dayArray.toArray()));
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
                //tester.setText(dayArray.toString());

//                System.out.print(Arrays.toString(dayArray.toArray()));
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
                //tester.setText(dayArray.toString());

//                System.out.print(Arrays.toString(dayArray.toArray()));
            }
        });

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

    /**
     * Set the alarm name
     */
    private void setLocationAlarmName() {
        location_name = (EditText) findViewById(R.id.location_alarm_name);
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
     *
     */

    /*
    * Button for bringing up QR scanner
    */

    private void setScanQR() {
        scanQR = (Button) findViewById(R.id.QRButton);

        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                integrator = new IntentIntegrator(AddNewLocationAlarm.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
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

    private void getAvailableRingtones(){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        startActivityForResult(intent, 5);
    }

    /*
    private void setSeekbar() {

        volumeControl = (SeekBar) findViewById(R.id.volume_bar);
        volume = volumeControl.getProgress();
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
*/

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

        Button button = (Button) findViewById(R.id.confirmButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlarm();
                storeAlarm();
                scheduleAlarm();
                Toast.makeText(getApplicationContext(), "Alarm created", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
