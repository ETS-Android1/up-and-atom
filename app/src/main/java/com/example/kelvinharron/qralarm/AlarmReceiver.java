package com.example.kelvinharron.qralarm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

/**
 * Receiver for each scheduled alarm as stipulated in the app manifest.  It creates a toast to
 * indicate an alarm is about to activated, creates an intent for the ActivityDismissAlarm.class
 * Extracts the data (alarm id) from the passed intent and attaches the alarm id to the
 * ActivityDismissAlarm intent.
 *
 * It flags how the intent will behave when started i.e. start as new task and clear other
 * activities and starts the activity.
 *
 * Created by Hannah Butler (40004276) on 12/04/2016.
 * Edited by Conor Taggart (40164305)
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        // Toast to indicate alarm is starting
        Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show();
        // instantiate the dismissAlarmIntent
        Intent dismissAlarmIntent = new Intent(context.getApplicationContext(), ActivityDismissAlarm.class);
        // get the alarm id passed with the received Pendingintent
        long alarmID = intent.getLongExtra("alarmID", 0);
        // add the id as extra data with the intent
        dismissAlarmIntent.putExtra("alarmID", alarmID);
        // flag how the activity will behave when it activates
        dismissAlarmIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        // start the activity
        context.startActivity(dismissAlarmIntent);
    }

}
