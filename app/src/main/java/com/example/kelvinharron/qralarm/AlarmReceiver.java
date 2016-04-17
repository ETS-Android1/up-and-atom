package com.example.kelvinharron.qralarm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

/**
 * Created by Hannah on 12/04/2016.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show();
        Intent dismissAlarmIntent = new Intent(context.getApplicationContext(), DismissAlarm.class);
        int alarmID = intent.getIntExtra("alarmID", 0);
        dismissAlarmIntent.putExtra("alarmID", alarmID);
        dismissAlarmIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dismissAlarmIntent);
    }

}
