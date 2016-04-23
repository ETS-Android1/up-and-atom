package com.example.kelvinharron.qralarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by kelvinharron on 19/04/2016.
 */
public class SQLiteHelperGeoAlarm {
/*
    private static final String GEO_ALARMS = "Alarms";
    private static final String GEO_ALARM_ID = "_id";
    private static final String GEO_ALARM_NAME = "_name";
    private static final String GEO_ALARM_SOUND = "_sound";
    private static final String GEO_ALARM_VOLUME = "_volume";
    private static final String GEO_ALARM_RECURRING = "_recurring";
    private static final String GEO_ALARM_DAYS = "_days";
    private static final String GEO_ALARM_QR_CODE = "_qr_code";
    private static final String GEO_ALARM_ON = "_on";
    private static final String GEO_ALARM_LATITUDE = "_latitude";
    private static final String GEO_ALARM_LONGITUDE = "_longitude";
    private static final String GEO_ALARM_RADIUS = "_radius";
    private static final String[] COLUMNS = {GEO_ALARM_ID, GEO_ALARM_NAME, GEO_ALARM_SOUND, GEO_ALARM_VOLUME,
            GEO_ALARM_RECURRING, GEO_ALARM_DAYS, GEO_ALARM_LATITUDE, GEO_ALARM_LONGITUDE, GEO_ALARM_RADIUS, GEO_ALARM_QR_CODE, GEO_ALARM_ON};

    private static final String DATABASE_NAME = "Alarms.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table " + GEO_ALARMS
            + "(" + GEO_ALARM_ID + " integer primary key autoincrement, "
            + GEO_ALARM_NAME + " text not null, "
            + GEO_ALARM_SOUND + " text, "
            + GEO_ALARM_VOLUME + " int not null, "
            + GEO_ALARM_RECURRING + " numeric, "
            + GEO_ALARM_DAYS + " text, "
            + GEO_ALARM_LATITUDE + " latitude"
            + GEO_ALARM_LONGITUDE + " longitude"
            + GEO_ALARM_RADIUS + " radius"
            + GEO_ALARM_QR_CODE + " text, "
            + GEO_ALARM_ON + " numeric, "
            + ");";

    public SQLiteHelperGeoAlarm(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GEO_ALARMS);
        onCreate(db);
    }

    public void createAlarm(GeoAlarm geoAlarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GEO_ALARM_NAME, geoAlarm.getName());
        values.put(GEO_ALARM_SOUND, geoAlarm.getSound());
        values.put(GEO_ALARM_VOLUME, geoAlarm.getVolume());
        values.put(GEO_ALARM_RECURRING, geoAlarm.isRecurring());
        values.put(GEO_ALARM_DAYS, geoAlarm.storeDays());
        values.put(GEO_ALARM_LATITUDE, geoAlarm.getLatitude());
        values.put(GEO_ALARM_LONGITUDE, geoAlarm.getLongitude());
        values.put(GEO_ALARM_RADIUS, geoAlarm.getRadius());
        values.put(GEO_ALARM_QR_CODE, String.valueOf(geoAlarm.getItemList()));
        values.put(GEO_ALARM_ON, geoAlarm.isOn());
        db.insert(GEO_ALARMS, null, values);
        db.close();

        System.out.print("Alarm Created");
    }

    public GeoAlarm readAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(GEO_ALARMS, COLUMNS, " id = ?", new String[]{
                String.valueOf(id)
        }, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        GeoAlarm geoAlarm = new GeoAlarm();

        geoAlarm.setId(Integer.parseInt(cursor.getString(0)));
        geoAlarm.setTitle(cursor.getString(1));
        geoAlarm.setSound(cursor.getString(2));
        geoAlarm.setVolume(cursor.getInt(3));
        geoAlarm.setRecurring(cursor.getString(4));
        geoAlarm.setDays(cursor.getInt(5));
        geoAlarm.setLatitude(cursor.getDouble(6));
        geoAlarm.setLongitude(cursor.getDouble(7));
        geoAlarm.setRadius(cursor.getFloat(8));
        geoAlarm.setItemList(cursor.getList(9));
        geoAlarm.setOn(cursor.getInt(10));

        return geoAlarm;
    }

    public List getAllAlarms() {
        List geoAlarms = new LinkedList();

        String query = "SELECT * FROM " + GEO_ALARMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        GeoAlarm geoAlarm = null;

        if (cursor.moveToFirst()) {
            do {
                geoAlarm = new GeoAlarm();
                geoAlarm.setId(Integer.parseInt(cursor.getString(0)));
                geoAlarm.setTitle(cursor.getString(1));
                geoAlarm.setSound(cursor.getString(2));
                geoAlarm.setVolume(cursor.getInt(3));
                geoAlarm.setRecurring(cursor.getInt(4));
                geoAlarm.setDays(cursor.getString(5));
                geoAlarm.setLatitude(cursor.getDouble(6));
                geoAlarm.setLongitude(cursor.getDouble(7));
                geoAlarm.setRadius(cursor.getFloat(8));
                geoAlarm.setItemList(cursor.getList(9));
                geoAlarm.setOn(cursor.getInt(10));
                geoAlarms.add(geoAlarm);
            } while (cursor.moveToNext());
        }
        return geoAlarms;
    }

    public int update(GeoAlarm geoAlarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(GEO_ALARM_NAME, geoAlarm.getName());
        values.put(GEO_ALARM_SOUND, String.valueOf(geoAlarm.getSound()));
        values.put(GEO_ALARM_VOLUME, geoAlarm.getVolume());
        values.put(GEO_ALARM_RECURRING, geoAlarm.isRecurring());
        values.put(GEO_ALARM_DAYS, geoAlarm.storeDays());
        values.put(GEO_ALARM_LONGITUDE, geoAlarm.getLongitude());
        values.put(GEO_ALARM_LATITUDE, geoAlarm.getLatitude());
        values.put(GEO_ALARM_RADIUS, geoAlarm.getRadius());
        values.put(GEO_ALARM_QR_CODE, String.valueOf(geoAlarm.getItemList()));
        values.put(GEO_ALARM_ON, geoAlarm.isOn());

        int i = db.update(GEO_ALARMS, values, GEO_ALARM_ID + " = ?", new String[]
                {String.valueOf(geoAlarm.getId())});

        db.close();
        return i;
    }

    public void deleteAlarm(GeoAlarm geoAlarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(GEO_ALARMS, GEO_ALARM_ID + " = ?", new String[]{String.valueOf(geoAlarm.getId())});
        db.close();
    }
    */
}
