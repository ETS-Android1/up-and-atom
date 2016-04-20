// TODO: Ringtone Spinner
// TODO: Create Alarm
// TODO: Confirm Alarm
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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This Activity handles the behavior for adding a new alarm. Appears once the floating action button is pressed.
 * Extending AppCompatActivity allows us to target older android devices.
 */
public class AddNewAlarm extends AppCompatActivity {

    /**
     * Alarm id which is passed from the parent activity based on the number of alarms
     */
    private int alarmId;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tester = (TextView) findViewById(R.id.tester2);


        db = new AlarmSQLiteHelper(this);

        setTimeAlarmName();
        setTimeAlarmMemo();
        setAlarm();
        setOverride();
        setScanQR();
        setRingtone();
        setSeekbar();
        confirmAlarm();

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
     * Method for setting repetition of the alarm - how to get data from it?
     *
     * @param view
     */
    //TODO confirm this is working as expected
    public ArrayList onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.timeMonday:
                if (checked) {
                    // necessary?
                    recurringTimeAlarm = true;
                    dayArray.add(2);
                } else {
                    // Do not set repeat
                }
                break;
            case R.id.timeTuesday:
                if (checked) {
                    recurringTimeAlarm = true;
                    dayArray.add(3);
                } else {
                    // Do not set repeat
                }
                break;
            case R.id.timeWednesday:
                if (checked) {
                    recurringTimeAlarm = true;
                    dayArray.add(4);
                } else {
                    // Do not set repeat
                }
                break;
            case R.id.timeThursday:
                if (checked) {
                    recurringTimeAlarm = true;
                    dayArray.add(5);
                } else {
                    // Do not set repeat
                }
                break;
            case R.id.timeFriday:
                if (checked) {
                    recurringTimeAlarm = true;
                    dayArray.add(6);
                } else {
                    // Do not set repeat
                }
                break;
            case R.id.timeSaturday:
                if (checked) {
                    recurringTimeAlarm = true;
                    dayArray.add(7);
                } else {
                    // Do not set repeat
                }
                break;
            case R.id.timeSunday:
                if (checked) {
                    recurringTimeAlarm = true;
                    dayArray.add(1);
                } else {
                    // Do not set repeat
                }
                break;
            default:
                recurringTimeAlarm = false;
                dayArray.add(0);
        }
        return dayArray;
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
     * Method used to create an override when creating a new Alarm.
     * Calls the AlarmOverrideDialogFragment that displays after a button press a
     * dialog box where the user can submit a passcode override.
     */
    private void setOverride() {

        final android.support.v4.app.FragmentManager manager = getSupportFragmentManager(); // fragment manager allows us to instantiate the dialog fragment
        final AlarmOverrideDialogFragment dialogFragment = new AlarmOverrideDialogFragment(); // create an object of the dialogfragment that will allow us to display it once a button is pressed.

        String passcode; //TODO: implement a way to get the passcode from the fragment and store & display it as a test

        final Switch overrideSwitch = (Switch) findViewById(R.id.overrideSwitch);
        final EditText passcodeEditText = (EditText) findViewById(R.id.editTextPasscode);
        overrideSwitch.setChecked(false);
        overrideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(AddNewAlarm.this, "CHECKED", Toast.LENGTH_SHORT).show();
                    dialogFragment.show(manager, "fragment");
                }
            }
        });
    }

    /**
     * Method for setting the alarm ringtone
     */

    private void setRingtone() {
        alarmSound = (TextView) findViewById(R.id.get_ringtone);
        chosenRingtone = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(this, chosenRingtone);
        alarmSound.setText(ringtone.getTitle(getApplicationContext()));

        alarmSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {        // Insert Hannah's ringtone thing here
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                startActivityForResult(intent, 5);
            }
        });
        //Spinner spinner = (Spinner) findViewById(R.id.ringtoneSpinner);
    }

    private void setSeekbar() {

        volumeControl = (SeekBar) findViewById(R.id.volume_bar);
        volume = volumeControl.getProgress();
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void createAlarm() {

        alarm = new Alarm();
        //TODO tie up with Kelvin to set the alarm id in the bundle that is extracted in the onCreate
        alarmId = 1;
        alarm.setId(alarmId);
        alarm.setName(time_name.getText().toString());
        alarm.setMemo(time_memo.getText().toString());
        alarm.setRecurring(recurringTimeAlarm);
        // setting the days on which the alarm occurs
        Integer[] days = new Integer[dayArray.size()];
        dayArray.toArray(days);
        alarm.setDays(days);
        alarm.setHour(tpHour);
        alarm.setMin(tpMinute);
        alarm.setSound(chosenRingtone.toString());
        alarm.setVolume(volume/VOLUME_MODIFIER);
        alarm.setQrResult(qrResult);
        alarm.setOn(true);

        //return alarm;
    }

    public void scheduleAlarm(){
        AlarmScheduler alarmScheduler = new AlarmScheduler();
        alarmScheduler.setAlarm(getApplicationContext(),this.alarm);
    }

    public void storeAlarm(){
       db.createAlarm(this.alarm);
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
                scheduleAlarm();
                storeAlarm();
                Toast.makeText(getApplicationContext(),"Alarm created",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}

// Create Alarm object, push to db and schedule an alarm

//Todo: implement alarm setter intent as below
/***
 * <p/>
 * <p/>
 * private void alarmTimeIntent(){
 * Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
 * alarmIntent.putExtra(AlarmClock.EXTRA_HOUR,true);
 * alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES,true);
 * startActivity(alarmIntent);}
 * <p/>
 * public Dialog onCreateDialog(Bundle savedInstanceState) {
 * // Use the current time as the default values for the picker
 * <p/>
 * <p/>
 * final Calendar c = Calendar.getInstance();
 * hour_local = c.get(Calendar.HOUR_OF_DAY);
 * minute_local = c.get(Calendar.MINUTE);
 * <p/>
 * // Create a new instance of TimePickerDialog and return it
 * return new TimePickerDialog(getActivity(), this, hour_local, minute_local,
 * DateFormat.is24HourFormat(getActivity()));}
 */