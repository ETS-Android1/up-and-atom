package com.example.kelvinharron.qralarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Conor on 08-Apr-16.
 */
public class AlarmSQLiteHelper extends SQLiteOpenHelper {

    private static final String ALARMS = "alarm";
    private static final String ALARM_ID = "id";
    private static final String ALARM_NAME = "name";
    private static final String ALARM_MEMO = "memo";
    private static final String ALARM_SOUND = "sound";
    private static final String ALARM_VOLUME = "volume";
    private static final String ALARM_RECURRING = "recurring";
    private static final String ALARM_DAYS = "days";
    private static final String ALARM_HOUR = "hour";
    private static final String ALARM_MIN = "min";
    private static final String ALARM_QR_CODE = "qr_code";
    private static final String ALARM_ON = "alarm_on";
    private static final String[] COLUMNS = {ALARM_ID, ALARM_NAME, ALARM_MEMO, ALARM_SOUND, ALARM_VOLUME,
            ALARM_RECURRING, ALARM_DAYS, ALARM_HOUR, ALARM_MIN, ALARM_QR_CODE, ALARM_ON};

    private static final String DATABASE_NAME = "Alarms.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE " + ALARMS
            + " ( " + ALARM_ID + " integer primary key autoincrement, "
            + ALARM_NAME + " text not null, "
            + ALARM_MEMO + " text not null, "
            + ALARM_SOUND + " text, "
            + ALARM_VOLUME + " integer not null, "
            + ALARM_RECURRING + " numeric, "
            + ALARM_DAYS + " integer, "
            + ALARM_HOUR + " integer, "
            + ALARM_MIN + " integer, "
            + ALARM_QR_CODE + " text, "
            + ALARM_ON + " numeric"
            + ");";

    public AlarmSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALARMS);
        onCreate(db);
    }

    public long createAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(ALARM_ID, null);
        values.put(ALARM_NAME, alarm.getName());
        values.put(ALARM_MEMO, alarm.getMemo());
        values.put(ALARM_SOUND, alarm.storeSound());
        values.put(ALARM_VOLUME, alarm.getVolume());
        values.put(ALARM_RECURRING, alarm.isRecurring());
        values.put(ALARM_DAYS, alarm.storeDays());
        values.put(ALARM_HOUR, alarm.getHour());
        values.put(ALARM_MIN, alarm.getMin());
        values.put(ALARM_QR_CODE, alarm.getQrResult());
        values.put(ALARM_ON, alarm.isOn());
        long id = db.insert(ALARMS, null, values);
        db.close();

        System.out.print("Alarm Created");
        return id;
    }

    public Alarm readAlarm(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ALARMS, COLUMNS, " id = ?", new String[]{
                String.valueOf(id)
        }, null, null, null, null);

        if (cursor != null && cursor.moveToNext()) {
            cursor.moveToFirst();
        }
        Alarm alarm = new Alarm();

        alarm.setId(id);
        alarm.setName(cursor.getString(1));
        alarm.setMemo(cursor.getString(2));
        alarm.setSound(cursor.getString(3));
        alarm.setVolume(cursor.getInt(4));
        alarm.setRecurring(cursor.getInt(5));
        alarm.setDays(cursor.getString(6));
        alarm.setHour(cursor.getInt(7));
        alarm.setMin(cursor.getInt(8));
        alarm.setQrResult(cursor.getString(9));
        alarm.setOn(cursor.getInt(10));
        return alarm;
    }

    public List getAllAlarms() {
        List alarms = new LinkedList();

        String query = "SELECT * FROM " + ALARMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Alarm alarm = null;

        if (cursor.moveToFirst()) {
            do {
                alarm = new Alarm();
                alarm.setId(cursor.getInt(0));
                alarm.setName(cursor.getString(1));
                alarm.setMemo(cursor.getString(2));
                alarm.setSound(cursor.getString(3));
                alarm.setVolume(cursor.getInt(4));
                alarm.setRecurring(cursor.getInt(5));
                alarm.setDays(cursor.getString(6));
                alarm.setHour(cursor.getInt(7));
                alarm.setMin(cursor.getInt(8));
                alarm.setQrResult(cursor.getString(9));
                alarm.setOn(cursor.getInt(10));
                alarms.add(alarm);
            } while (cursor.moveToNext());
        }
        return alarms;
    }

    public int update(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ALARM_NAME, alarm.getName());
        values.put(ALARM_MEMO, alarm.getMemo());
        values.put(ALARM_SOUND, alarm.storeSound());
        values.put(ALARM_VOLUME, alarm.getVolume());
        values.put(ALARM_RECURRING, alarm.isRecurring());
        values.put(ALARM_DAYS, alarm.storeDays());
        values.put(ALARM_HOUR, alarm.getHour());
        values.put(ALARM_MIN, alarm.getMin());
        values.put(ALARM_QR_CODE, alarm.getQrResult());
        values.put(ALARM_ON, alarm.isOn());

        int i = db.update(ALARMS, values, ALARM_ID + " = ?", new String[]
                {String.valueOf(alarm.getId())});

        db.close();
        return i;
    }

    public int getNewId(){
        SQLiteDatabase db = this.getWritableDatabase();
        int maxID = 0;
        String query = "SELECT MAX(id) FROM " + ALARMS;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                maxID = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        return maxID + 1;
    }

    public void deleteAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ALARMS, ALARM_ID + " = ?", new String[]{String.valueOf(alarm.getId())});
        db.close();
    }
}
