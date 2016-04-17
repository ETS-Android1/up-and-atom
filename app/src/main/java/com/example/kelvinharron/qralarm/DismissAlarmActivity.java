package com.example.kelvinharron.qralarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    private IntentIntegrator integrator;
    Uri uriAlarm;
    Ringtone ring;
    String QRResult;
    AlarmSQLiteHelper alarmSQLiteHelper = new AlarmSQLiteHelper(context);
    Alarm alarm;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        //if (extras != null) {
        int alarmId = extras.getInt("alarmID");
        alarm = alarmSQLiteHelper.readAlarm(alarmId);
        //}


    uriAlarm=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    ring=RingtoneManager
            .getRingtone(

    getApplicationContext(),alarm.getSound()

    );

    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
            context);


    // set title
    alertDialogBuilder.setTitle("Reminder");
    ring.play();

    // set dialog message
    alertDialogBuilder
            .setMessage("Scan QR Code To Stop Alarm")
            .

    setCancelable(false)

    .

    setPositiveButton("Scan",new DialogInterface.OnClickListener() {
        public void onClick (DialogInterface dialog,int id){

            integrator = new IntentIntegrator(DismissAlarmActivity.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan QR Code");
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.setBeepEnabled(false);
            integrator.initiateScan();
        }


    public void onActivityResult(int requestCode, int resultCode, Intent intent, DialogInterface dialog) {
        IntentResult scanResult = integrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String data[] = scanResult.getContents().split("\n");
            for (String string : data) {
                QRResult.equals(data);
            }

            if (QRResult == alarm.getQrResult()) ;
            ring.stop();
            dialog.dismiss();

        } else {
            integrator.setPrompt("Wrong QR Code - Please Try Again");



        }
    }


})
        .setNegativeButton("Snooze",new DialogInterface.OnClickListener(){
public void onClick(DialogInterface dialog,int id){
        // if this button is clicked, just close
        // the dialog box and do nothing
        ring.stop();
        dialog.dismiss();
        }
        });

        // create alert dialog
        AlertDialog alertDialog=alertDialogBuilder.create();

        // show it
        alertDialog.show();
        }
        }

