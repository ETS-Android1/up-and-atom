package com.example.kelvinharron.qralarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.kelvinharron.qralarm.AlarmSQLiteHelper;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * Created by Hannah on 12/04/2016.
 */
public class DismissAlarmActivity extends AppCompatActivity {


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


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        alarmSQLiteHelper = new AlarmSQLiteHelper(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        //if (extras != null) {
        int alarmId = extras.getInt("alarmID");
        System.out.print("Outputting ID: "+alarmId+"\n");
        alarm = alarmSQLiteHelper.readAlarm(alarmId);
        //uriAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        //ring = RingtoneManager.getRingtone(getApplicationContext(), uriAlarm);//alarm.getSound());
        ring = RingtoneManager.getRingtone(getApplicationContext(), alarm.getSound());
        //audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //prevVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        //audioManager.setStreamVolume(AudioManager.STREAM_ALARM, Math.round(alarm.getVolume() * audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)), AudioManager.FLAG_ALLOW_RINGER_MODES);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set title
        alertDialogBuilder.setTitle("Reminder");
        ring.play();

        if(!alarm.isRecurring()){
            alarm.setOn(false);
            alarmSQLiteHelper.update(alarm);
        }

        // set dialog message
        alertDialogBuilder.setMessage("Scan QR Code To Stop Alarm").setCancelable(false).setPositiveButton("Scan", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String prompt = "Scan QR Code";
                startScanning(prompt);
            }


            public void onActivityResult(int requestCode, int resultCode, Intent intent, DialogInterface dialog) {
                IntentResult scanResult = integrator.parseActivityResult(requestCode, resultCode, intent);
                if (scanResult != null) {
                    String data[] = scanResult.getContents().split("\n");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String string : data) {
                        stringBuilder.append(string);
                    }
                    qrResult = stringBuilder.toString();

                    if (qrResult == alarm.getQrResult()) {
                        dialog.dismiss();
                        stop();
                    } else {
                        String prompt = "Wrong QR Code - Please Try Again";
                        startScanning(prompt);
                    }
                }
            }
        }).setNegativeButton("Snooze", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                dialog.dismiss();
                new AlarmScheduler().ignoreAlarm(getApplicationContext(), 5);
                stop();
            }
        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    public void stop() {
        ring.stop();
        //audioManager.setStreamVolume(AudioManager.STREAM_ALARM, prevVolume, AudioManager.FLAG_ALLOW_RINGER_MODES);
        finish();
    }

    public void startScanning(String prompt) {
        integrator = new IntentIntegrator(DismissAlarmActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt(prompt);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }
}

