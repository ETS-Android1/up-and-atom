package com.example.kelvinharron.qralarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Boot Reciever reschedules alarms by reading each from the database and attempting to schedule
 * them via AlarmScheduler. If they are set to off in the database, the scheduler won't schedule
 * them.
 * <p/>
 * Created by Conor Taggart (40164305) on 13/04/2016.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // check if the passed intent equals BOOT_COMPLETED constant from intent.action
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // instatiate an alarm scheduler
            AlarmScheduler alarmScheduler = new AlarmScheduler();
            // instantiate the database
            SQLiteHelperAlarm sqLiteHelper = new SQLiteHelperAlarm(context);
            // get all the alarms stored in the database
            List<Alarm> alarms = sqLiteHelper.getAllAlarms();
            // pass each alarm to the alarmScheduler object
            for (Alarm alarm : alarms) {
                alarmScheduler.setAlarm(context, alarm);
            }
        }
    }
}
