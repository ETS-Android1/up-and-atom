
// TODO: Checkboxes
// TODO: Tidy stuff up & Comment

package com.example.kelvinharron.qralarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

/**
 * This Activity handles the behavior for adding a new alarm. Appears once the floating action button is pressed.
 * Extending AppCompatActivity allows us to target older android devices.
 */
public class AddNewAlarm extends AppCompatActivity {

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
    private EditText time_name;

    /**
     * Alarm memo - time alarm only
     */
    private EditText time_memo;

    /**
     * The hour value taken from the timepicker
     */
    private int tpHour;

    /**
     * The minute value taken from the timepicker
     */
    private int tpMinute;

    /**
     * Sets whether the alarm repeats or not
     */
    private boolean recurringTimeAlarm;

    private CheckBox sunCB;
    private CheckBox monCB;
    private CheckBox tueCB;
    private CheckBox thuCB;
    private CheckBox friCB;
    private CheckBox satCB;
    private CheckBox wedCB;
    /**
     * ArrayList for checkbox repetitions
     */
    ArrayList<Integer> dayArray = new ArrayList<>();

    // TEST - REMOVE
    private TextView tester;


    /**
     * Creates a reference to the calendar allowing us to get the hour and minute as integers.
     */
    private Calendar calendar = Calendar.getInstance();

    /**
     *
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

    /**
     *
     */
    private IntentIntegrator integrator;

    private Uri chosenRingtone;

    private SeekBar volumeControl;
    private float volume;

    AlarmSQLiteHelper db;
    public static final float VOLUME_MODIFIER = 10;

    /**
     * Start of activity lifecycle. Sets the view of the AddNewAlarm activity and calls the methods enabling behavior.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_time_alarm);

        //Bundle extras = getIntent().getExtras();
        //alarmId = extras.getInt("alarmID");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        db = new AlarmSQLiteHelper(this);

        setTimeAlarmName();
        setTimeAlarmMemo();
        setAlarm();
        setScanQR();
        setRingtone();
        setSeekbar();
        confirmAlarm();

        dayArray = new ArrayList<>();
        sunCB = (CheckBox) findViewById(R.id.timeSunday);
        sunCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sunCB.isChecked()) {
                    dayArray.add(Integer.valueOf(1));
                } else {
                    dayArray.remove(Integer.valueOf(1));
                }
                tester.setText(dayArray.toString());
                //System.out.print();
            }
        });
        monCB = (CheckBox) findViewById(R.id.timeMonday);
        monCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monCB.isChecked()) {
                    dayArray.add(Integer.valueOf(2));
                } else {
                    dayArray.remove(Integer.valueOf(2));
                }
                tester.setText(dayArray.toString());

//                System.out.print(Arrays.toString(dayArray.toArray()));
            }
        });

        tueCB = (CheckBox) findViewById(R.id.timeTuesday);
        tueCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tueCB.isChecked()) {
                    dayArray.add(Integer.valueOf(3));
                } else {
                    dayArray.remove(Integer.valueOf(3));
                }
                tester.setText(dayArray.toString());

//                System.out.print(Arrays.toString(dayArray.toArray()));
            }
        });
        wedCB = (CheckBox) findViewById(R.id.timeWednesday);
        wedCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wedCB.isChecked()) {
                    dayArray.add(Integer.valueOf(4));
                } else {
                    dayArray.remove(Integer.valueOf(4));
                }
                tester.setText(dayArray.toString());

//                System.out.print(Arrays.toString(dayArray.toArray()));
            }
        });
        thuCB = (CheckBox) findViewById(R.id.timeThursday);
        thuCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thuCB.isChecked()) {
                    dayArray.add(Integer.valueOf(5));
                } else {
                    dayArray.remove(Integer.valueOf(5));
                }
                tester.setText(dayArray.toString());

//                System.out.print(Arrays.toString(dayArray.toArray()));
            }
        });
        friCB = (CheckBox) findViewById(R.id.timeFriday);
        friCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friCB.isChecked()) {
                    dayArray.add(Integer.valueOf(6));
                } else {
                    dayArray.remove(Integer.valueOf(6));
                }
                tester.setText(dayArray.toString());

//                System.out.print(Arrays.toString(dayArray.toArray()));
            }
        });
        satCB = (CheckBox) findViewById(R.id.timeSaturday);
        satCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (satCB.isChecked()) {
                    dayArray.add(Integer.valueOf(7));
                } else {
                    dayArray.remove(Integer.valueOf(7));
                }
                tester.setText(dayArray.toString());

//                System.out.print(Arrays.toString(dayArray.toArray()));
            }
        });

    }

    /**
     * Set the alarm name
     */
    private void setTimeAlarmName() {
        time_name = (EditText) findViewById(R.id.time_alarm_name);
        time_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                time_name.setText(v.getText().toString());
                return false;
            }
        });
    }

    /**
     * Set the alarm memo
     */
    private void setTimeAlarmMemo() {
        time_memo = (EditText) findViewById(R.id.time_alarm_memo);
        time_memo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                time_memo.setText(v.getText().toString());
                return false;
            }
        });
    }

    /**
     * Sets the time of the alarm using a timepicker
     */
    private void setAlarm() {
        final TimePicker timePicker = (TimePicker) findViewById(R.id.timeAlarmTimepicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                tpHour = hourOfDay;
                tpMinute = minute;
            }
        });
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = integrator.parseActivityResult(requestCode, resultCode, intent);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (scanResult != null) {
                String data[] = scanResult.getContents().split("\n");
                StringBuilder stringBuilder = new StringBuilder();
                for (String string : data) {
                    stringBuilder.append(string);
                }
                qrResult = stringBuilder.toString();
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == 5) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri != null) {
                this.chosenRingtone = uri;
            } else {
                this.chosenRingtone = null;
            }
            Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
            alarmSound.setText(ringtone.getTitle(getApplicationContext()));
        }

    }


    /**
     * Button for bringing up QR scanner
     */

    private void setScanQR() {
        scanQR = (Button) findViewById(R.id.QRButton);

        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                integrator = new IntentIntegrator(AddNewAlarm.this);
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

    private void createAlarm() {

        alarm = new Alarm();
        //alarmId = 1;
        //alarmId = db.getNewId();
        alarm.setName(time_name.getText().toString());
        alarm.setMemo(time_memo.getText().toString());

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

        alarm.setHour(tpHour);
        alarm.setMin(tpMinute);
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