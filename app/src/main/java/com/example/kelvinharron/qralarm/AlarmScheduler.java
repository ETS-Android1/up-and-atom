package com.example.kelvinharron.qralarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Schedules when a passed alarm with activate whether as a one off alarm or if it is recurring.
 * Additionally, cancels given alarm and schedules an alarm for a given number of minutes after the
 * current time (enables the snooze function in the dismissAlarmActivity.
 * <p/>
 * Created by Hannah Butler (40004276) on 12/04/2016.
 * Edited by Conor Taggart (40164305)
 */
public class AlarmScheduler {

    // provides access to the system alarm services which handle the scheduling of tasks or
    // applications.
    private AlarmManager alarmMgr;
    // used in the case of scheduling alarms to setup the activity (predefined code) that will be
    // called (i.e. alarm receiver) at a later time.
    private PendingIntent alarmIntent;

    /**
     * Schedules a given alarm for the time indicated in the Alarm object. If it is recurring,
     * the alarm is repeated at 7 day intervals for each indicated day.
     *
     * @param context - application context
     * @param alarm   - alarm object which indicates time, days and if the alarm will activate.
     */
    public void setAlarm(Context context, Alarm alarm) {
        // instantiate the system alarm service manager
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // instantiate an intent for the AlarmReciever
        Intent intent = new Intent(context, AlarmReceiver.class);
        // pass the alarm id as an extra to be extracted in the receivier
        intent.putExtra("alarmID", alarm.getId());
        // check if the alarm is on
        if (alarm.isOn()) {
            // if so check if it is recurring
            if (alarm.isRecurring()) {
                // for each day stipulated in the alarm, schedule an new alarm, each alarm id will
                // be multiplied with 10 and then an integer representation of each day will be
                // added i.e. alarm id = 10 and days a Sun = 1, Mon = 2... Sat = 7 therefore
                // sun alarm id = 101, Mon alarm id = 102... Sat alarm id = 107.
                for (Integer day : alarm.getDays()) {
                    //multiply by 10 to uniquely identify the intent for each day or ends with 0 if not recurring
                    alarmIntent = PendingIntent.getBroadcast(context, new BigDecimal(alarm.getId() * 10).intValueExact() + day, intent, 0);
                    // instantiate a calander object
                    Calendar calendar = Calendar.getInstance();
                    // set to the current system time
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    // set the calender day to day i.e. sun = 1, mon = 2 etc...
                    calendar.set(Calendar.DAY_OF_WEEK, day);
                    // set the calendar hour to alarm hour
                    calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
                    // as per hour, set to alarm minutes
                    calendar.set(Calendar.MINUTE, new Integer(alarm.getMin()));
                    // set seconds to 0 alarm activates close to 0th second of minute
                    calendar.set(Calendar.SECOND, 0);
                    // as alarm is recurring schedule the alarm to repeat every 7 day from set time
                    // specified by alarm set calendar object
                    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY * 7, alarmIntent);
                }
            } else {
                // if the alarm is not recurring
                // uniquely identify the intent for each day and ends with 0 if not recurring
                alarmIntent = PendingIntent.getBroadcast(context, new BigDecimal(alarm.getId() * 10).intValueExact(), intent, 0);
                // get a calendar object
                Calendar calNow = Calendar.getInstance();
                // set the time to current system time
                calNow.setTimeInMillis(System.currentTimeMillis());
                // get a second instance of calendar
                Calendar calSet = (Calendar) calNow.clone();
                // set the time of the calendar object to the alarm specified time
                calSet.set(Calendar.HOUR_OF_DAY, alarm.getHour());
                calSet.set(Calendar.MINUTE, new Integer(alarm.getMin()));
                calSet.set(Calendar.SECOND, 0);
                // check if the alarm specified time is set to before the current time
                if (calSet.compareTo(calNow) <= 0) {
                    //Today Set time passed, count to tomorrow
                    calSet.add(Calendar.DATE, 1);
                }
                // set the alarm to activate at once at the calendar specified time
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP,
                        calSet.getTimeInMillis(), alarmIntent);
            }
        }
    }

    /**
     * Cancels the passed alarm object for occurring removing alarm from the scheduled time.
     *
     * @param context - application context
     * @param alarm   - alarm object which indicates time, days and if the alarm will activate.
     */
    public void cancelAlarm(Context context, Alarm alarm) {
        // instantiate the system alarm service manager
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // instantiate an intent for the AlarmReciever
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("alarmId", alarm.getId());
        // check if the alarm is recurring, if it is each days alarm is canceled by multiplying
        // the id by 10 and adding the integer representation of each day
        if (alarm.isRecurring()) {
            for (Integer day : alarm.getDays()) {
                //multiply by 10 to uniquely identify the intent for each day or ends with 0 if not recurring
                alarmIntent = PendingIntent.getBroadcast(context, new BigDecimal(alarm.getId()).intValueExact() * 10 + day, intent, 0);
                // cancel the scheduled alarm for the intent
                alarmMgr.cancel(alarmIntent);
            }
            // if it isn't recurring, just cancel the one alarm
        } else {
            //multiply by 10 to uniquely identify the intent for each day or ends with 0 if not recurring
            alarmIntent = PendingIntent.getBroadcast(context, new BigDecimal(alarm.getId()).intValueExact() * 10, intent, 0);
            // cancel the scheduled alarm for the intent
            alarmMgr.cancel(alarmIntent);
        }
    }

    /**
     * Schedules an alarm for a given number of minutes after the current time (enables the snooze
     * function in the dismissAlarmActivity.
     *
     * @param context - application context
     * @param minutes - int of the number of minutes to schedule an alarm for
     */
    public void ignoreAlarm(Context context, int minutes) {
        // instantiate the system alarm service manager
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // instantiate an intent for the AlarmReciever
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // set an alarm (wakeup device) with specified pending intent at pass number of mins
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        minutes * 60 * 1000, alarmIntent);

    }
}



