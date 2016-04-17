package com.example.kelvinharron.qralarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;


/**
 * Created by Hannah on 12/04/2016.
 */
public class DismissAlarm extends Activity {

    final Context context = this;
    private Button button;
    private IntentIntegrator integrator;

    //Check out below for activities that look like dialog boxes
    //http://stackoverflow.com/questions/1979369/android-activity-as-a-dialog

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dismiss_layout);

        button = (Button) findViewById(R.id.buttonSetAlarm);
        Intent intent = getIntent();
        int value = intent.getIntExtra("alarm_id", 0);
        button.setText(new Integer(value).toString());
        // add button listener
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Your Title");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Scan QR Code TO MAKE IT STOP")
                        .setCancelable(false)
                        .setPositiveButton("Scan",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                    integrator = new IntentIntegrator(DismissAlarm.this);
                                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                                    integrator.setPrompt("Scan QR Code");
                                    integrator.setCameraId(0);  // Use a specific camera of the device
                                    integrator.setBeepEnabled(false);
                                    integrator.initiateScan();

                            }
                        })
                        .setNegativeButton("Snooze",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.dismiss();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }
}