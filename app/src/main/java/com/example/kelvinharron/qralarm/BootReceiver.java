package com.example.kelvinharron.qralarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by Conor on 13/04/2016.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmScheduler alarmScheduler = new AlarmScheduler();
            AlarmSQLiteHelper sqLiteHelper = new AlarmSQLiteHelper(context);
            List<Alarm> alarms = sqLiteHelper.getAllAlarms();
            for (Alarm alarm : alarms) {
                alarmScheduler.setAlarm(context, alarm);
            }
        }
    }
}
