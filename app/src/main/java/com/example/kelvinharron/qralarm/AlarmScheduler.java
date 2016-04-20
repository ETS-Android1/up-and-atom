package com.example.kelvinharron.qralarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.os.SystemClock;

import java.util.Calendar;

/**
 * Created by Hannah on 12/04/2016.
 */
public class AlarmScheduler {


    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public void setAlarm(Context context, Alarm alarm) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("alarmID", alarm.getId());

        if (alarm.isOn()) {

            if (alarm.isRecurring()) {
                for (Integer day : alarm.getDays()) {
                    //multiply by 10 to uniquely identify the intent for each day or ends with 0 if not recurring
                    alarmIntent = PendingIntent.getBroadcast(context, alarm.getId() * 10 + day, intent, 0);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.DAY_OF_WEEK, day);
                    calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
                    calendar.set(Calendar.MINUTE, new Integer(alarm.getMin()));
                    calendar.set(Calendar.SECOND, 0);

                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY * 7, alarmIntent);
                }
            } else {

                //uniquely identify the intent for each day and ends with 0 if not recurring
                alarmIntent = PendingIntent.getBroadcast(context, alarm.getId() * 10, intent, 0);

                Calendar calNow = Calendar.getInstance();
                calNow.setTimeInMillis(System.currentTimeMillis());
                Calendar calSet = (Calendar) calNow.clone();

                calSet.set(Calendar.HOUR_OF_DAY, alarm.getHour());
                calSet.set(Calendar.MINUTE, new Integer(alarm.getMin()));
                calSet.set(Calendar.SECOND, 0);

                if (calSet.compareTo(calNow) <= 0) {
                    //Today Set time passed, count to tomorrow
                    calSet.add(Calendar.DATE, 1);
                }
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP,
                        calSet.getTimeInMillis(), alarmIntent);
            }
        }
    }

    public void cancelAlarm(Context context, Alarm alarm) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("alarm", (Parcelable) alarm);

        if (alarm.isRecurring()) {
            for (Integer day : alarm.getDays()) {
                //multiply by 10 to uniquely identify the intent for each day or ends with 0 if not recurring
                alarmIntent = PendingIntent.getBroadcast(context, alarm.getId() * 10 + day, intent, 0);
                alarmMgr.cancel(alarmIntent);
            }
        } else {
            //multiply by 10 to uniquely identify the intent for each day or ends with 0 if not recurring
            alarmIntent = PendingIntent.getBroadcast(context, alarm.getId() * 10, intent, 0);
            alarmMgr.cancel(alarmIntent);
        }
    }

    public void ignoreAlarm(Context context, int minutes) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        minutes * 60 * 1000, alarmIntent);

    }
}



