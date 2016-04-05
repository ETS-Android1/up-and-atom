package com.example.kelvinharron.qralarm;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddNewAlarm extends AppCompatActivity {

    private TextView display;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newalarm);

        //setAlarmDialog();
        setAlarmType();
    }

    /**
     * Method for setting the time of an alarm
     */
    private void setAlarmDialog() {

        display = (TextView) findViewById(R.id.displayTimeText);
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
            display.setText("Chosen time is :" + hourOfDay + ":" + minute);

        }
    };


    /**
     * Method for Choosing the type of alarm
     */
    private void setAlarmType() {


        String[] timeLocation = {"Time", "Location"};

        final Spinner spinner = (Spinner) findViewById(R.id.chooseTimeSpinner);

        // Create array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, timeLocation);
        // Specify the layout to use
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinner.getSelectedItem().equals("Time")) {
                    setAlarmDialog();
                }
                if (spinner.getSelectedItem().equals("Location")) {
                    Intent mapIntent = new Intent(view.getContext(), MapsActivity.class);
                    startActivity(mapIntent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}







/*
   private void alarmTimeIntent(){
        Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR,true);
        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES,true);
        startActivity(alarmIntent);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker


        final Calendar c = Calendar.getInstance();
        hour_local = c.get(Calendar.HOUR_OF_DAY);
        minute_local = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour_local, minute_local,
                DateFormat.is24HourFormat(getActivity()));
    }
    */

