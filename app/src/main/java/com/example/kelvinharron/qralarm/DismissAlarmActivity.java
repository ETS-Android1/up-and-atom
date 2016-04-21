package com.example.kelvinharron.qralarm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;


/**
 * Created by Hannah on 12/04/2016.
 */
public class DismissAlarmActivity extends AppCompatActivity implements OnCompleteListener {


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
    MediaPlayer mediaPlayer;

    //TODO sort out actual setting preferences to set overrideCode
    String prefOverrideCode = "1";

    Button snooze;
    Button scan;
    Button override;

    TextView title;
    TextView scanItem;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dismiss_layout);
        alarmSQLiteHelper = new AlarmSQLiteHelper(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        long alarmId = extras.getLong("alarmID");
        System.out.print("Outputting ID: " + alarmId + "\n");
        alarm = alarmSQLiteHelper.readAlarm(alarmId);

        title = (TextView) findViewById(R.id.dismiss_title);
        title.setText(alarm.getName());

        scanItem = (TextView) findViewById(R.id.scan_item);
        scanItem.setText(alarm.getMemo());

        if (!alarm.isRecurring()) {
            alarm.setOn(false);
            alarmSQLiteHelper.update(alarm);
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, alarm.getSound());
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (IOException iOException) {
            ring = RingtoneManager.getRingtone(getApplicationContext(), alarm.getSound());
            ring.play();
        }

        setupSnooze();
        setupScan();
        setupOverride();

    }

    public void setupSnooze() {
        snooze = (Button) findViewById(R.id.snooze_button);
        snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlarmScheduler().ignoreAlarm(getApplicationContext(), 5);
                stop();
            }
        });
    }

    public void setupScan() {
        scan = (Button) findViewById(R.id.scan_button);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    public void setupOverride() {
        override = (Button) findViewById(R.id.override_button);
        override.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.support.v4.app.FragmentManager manager = getSupportFragmentManager(); // fragment manager allows us to instantiate the dialog fragment
                final AlarmOverrideDialogFragment dialogFragment = new AlarmOverrideDialogFragment(); // create an object of the dialogfragment that will allow us to display it once a button is pressed.
                dialogFragment.show(manager, "fragment");
            }
        });
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        } else if (ring != null) {
            ring.stop();
        }
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

    @Override
    public void onComplete(String overrideCode) {
        if (prefOverrideCode.equals(overrideCode)) {
            stop();
        } else {
            Toast.makeText(getApplicationContext(), "Wrong override code, please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}

