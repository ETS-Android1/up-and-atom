package com.example.kelvinharron.qralarm;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.Calendar;

/**
 * This Activity handles the behavior for adding a new alarm. Appears once the floating action button is pressed.
 * Extending AppCompatActivity allows us to target older android devices.
 */
public class AddNewAlarm extends AppCompatActivity {

    /**
     * Blank text view that once a user selects a time from the TimePicker dialog, it appears in the add new alarm activity.
     */
    private TextView displaySelectedTimeTextView;
    private TextView QRTest;

    /**
     * Creates a reference to the calendar allowing us to get the hour and minute as integers.
     */
    private Calendar calendar = Calendar.getInstance();

    /**
     *
     */
    private Toolbar toolbar;

    private Button scanQR;

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

        QRTest = (TextView) findViewById(R.id.QRTest);

        setAlarm();
        setOverride();
        setScanQR();
    }



    private void setAlarm(){
        final TimePicker timePicker = (TimePicker) findViewById(R.id.theTimepicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                // Set the alarm time here
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
                integrator = new IntentIntegrator(AddNewAlarm.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan QR Code");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(false);
                integrator.initiateScan();


            }
        });
    }

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
     * Method used to create an override when creating a new Alarm.
     * Calls the AlarmOverrideDialogFragment that displays after a button press a dialog box where the user can submit a passcode override.
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


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.Monday:
                if (checked) {
                    // Set alarm to repeat
                }else {
                    // Do not set repeat
                }
                break;
            case R.id.Tuesday:
                if (checked) {
                    // Set alarm to repeat
                }else {
                    // Do not set repeat
                }
                break;
            case R.id.Wednesday:
                if (checked) {
                    // Set alarm to repeat
                }else {
                    // Do not set repeat
                }
                break;
            case R.id.Thursday:
                if (checked) {
                    // Set alarm to repeat
                }else {
                    // Do not set repeat
                }
                break;
            case R.id.Friday:
                if (checked) {
                    // Set alarm to repeat
                }else {
                    // Do not set repeat
                }
                break;
            case R.id.Saturday:
                if (checked) {
                    // Set alarm to repeat
                }else {
                    // Do not set repeat
                }
                break;
            case R.id.Sunday:
                if (checked) {
                    // Set alarm to repeat
                }else {
                    // Do not set repeat
                }
                break;
        }
    }
}

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


/*

     Method for setting the time value of a time based alarm. Launched if spinner selection is set to time based alarm.

private void setAlarmDialog() {

    displaySelectedTimeTextView = (TextView) findViewById(R.id.displayTimeText);
    Button button = (Button) findViewById(R.id.timeButton);
    button.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            new TimePickerDialog(AddNewAlarm.this, onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        }
    });
}

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            displaySelectedTimeTextView.setText("Chosen time is :" + hourOfDay + ":" + minute);
        }
    };
 */

/*
         /**
     * Method for Choosing the type of alarm between time based or location.
     * Depending on user choice, carries out required behavior through method/intent call.

    private void setAlarmType() {

        String[] alarmTypes = {"Time", "Location"};
        final Spinner alarmTypeSpinner = (Spinner) findViewById(R.id.chooseTimeSpinner);

        // Create array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, alarmTypes);
        // Specify the layout to use
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        alarmTypeSpinner.setAdapter(adapter);
        alarmTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /**
             * Depending on what option the user chooses, launches necessary behavior.
             * @param parent
             * @param view
             * @param position
             * @param id

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (alarmTypeSpinner.getSelectedItem().equals("Time")) {
                    setAlarmDialog();
                }
                if (alarmTypeSpinner.getSelectedItem().equals("Location")) {
                    launchMapIntent(view);
                }
            }

            /**
             * Launches the map activity allowing a user to set their location for the alarm.
             * @param view

            private void launchMapIntent(View view) {
                Intent mapIntent = new Intent(view.getContext(), MapsActivity.class);
                startActivity(mapIntent);
            }

            /**
             * Method handles if spinner is blank. Currently spinner defaults to time.
             * @param parent

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
*/