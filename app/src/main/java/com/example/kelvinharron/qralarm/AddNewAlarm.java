// TODO: Ringtone Spinner
// TODO: Create Alarm
// TODO: Confirm Alarm
// TODO: Checkboxes
// TODO: Tidy stuff up & Comment

package com.example.kelvinharron.qralarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
     *
     */
    private TextView QRTest;

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
     *
     */
    private IntentIntegrator integrator;

    /**
     * Start of activity lifecycle. Sets the view of the AddNewAlarm activity and calls the methods enabling behavior.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_time_alarm);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tester = (TextView)findViewById(R.id.tester2);
        QRTest = (TextView) findViewById(R.id.QRTest);

        setAlarm();
        setOverride();
        setScanQR();

    }

    /**
     * Set the alarm name
     */
    private void setTimeAlarmName(){
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
    private void setTimeAlarmMemo(){
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
    private void setAlarm(){
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
     * @param view
     */
    public ArrayList onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.timeMonday:
                if (checked) {
                    // necessary?
                    recurringTimeAlarm=true;
                    dayArray.add(2);
                }else {
                    // Do not set repeat
                }
                break;
            case R.id.timeTuesday:
                if (checked) {
                    recurringTimeAlarm=true;
                    dayArray.add(3);
                }else {
                    // Do not set repeat
                }
                break;
            case R.id.timeWednesday:
                if (checked) {
                    recurringTimeAlarm=true;
                    dayArray.add(4);
                }else {
                    // Do not set repeat
                }
                break;
            case R.id.timeThursday:
                if (checked) {
                    recurringTimeAlarm=true;
                    dayArray.add(5);
                }else {
                    // Do not set repeat
                }
                break;
            case R.id.timeFriday:
                if (checked) {
                    recurringTimeAlarm=true;
                    dayArray.add(6);
                }else {
                    // Do not set repeat
                }
                break;
            case R.id.timeSaturday:
                if (checked) {
                    recurringTimeAlarm=true;
                    dayArray.add(7);
                }else {
                    // Do not set repeat
                }
                break;
            case R.id.timeSunday:
                if (checked) {
                    recurringTimeAlarm=true;
                    dayArray.add(1);
                }else {
                    // Do not set repeat
                }
                break;
            default:recurringTimeAlarm = false;
        }
        return dayArray;
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = integrator.parseActivityResult(requestCode, resultCode,intent);
        if (scanResult !=null){
            String data[] = scanResult.getContents().split("\n");
            for (String string:data){
                QRTest.append(string);
            }
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

    private void setRingtone(){

        Spinner spinner = (Spinner)findViewById(R.id.ringtoneSpinner);
        // Insert Hannah's ringtone thing here
    }



    private Alarm createAlarm(int id) {

        Alarm thisAlarm = new Alarm();

        thisAlarm.setId(id);
        thisAlarm.setName(time_name.getText().toString());
        this.memo = time_memo;
        this.recurring = recurringTimeAlarm;
        setSound(sound);
        setVolume(volume);
        this.days = dayArray.toString();
        this.hour = tpHour;
        this.min = tpMinute;
        this.qrResult = qrResult;
        this.on = on;

        return thisAlarm;
    }

    /**
     * Confirm the alarm settings & push to DB
     */

    private void confirmAlarm(){

        Button button = (Button) findViewById(R.id.confirmButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}

// Create Alarm object, push to db and schedule an alarm

//Todo: implement alarm setter intent as below
/***
 * <p>
 * <p>
 * private void alarmTimeIntent(){
 * Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
 * alarmIntent.putExtra(AlarmClock.EXTRA_HOUR,true);
 * alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES,true);
 * startActivity(alarmIntent);}
 * <p>
 * public Dialog onCreateDialog(Bundle savedInstanceState) {
 * // Use the current time as the default values for the picker
 * <p>
 * <p>
 * final Calendar c = Calendar.getInstance();
 * hour_local = c.get(Calendar.HOUR_OF_DAY);
 * minute_local = c.get(Calendar.MINUTE);
 * <p>
 * // Create a new instance of TimePickerDialog and return it
 * return new TimePickerDialog(getActivity(), this, hour_local, minute_local,
 * DateFormat.is24HourFormat(getActivity()));}
 */