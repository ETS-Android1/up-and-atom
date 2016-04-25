package com.example.kelvinharron.qralarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * SQLite Helper which persistently retains alarm objects. Consists of an onCreate method to
 * generate the initial tables, onUpgrade to drop the current tables and replace them with a
 * new revision. A createAlarm method to add new rows or objects to the table, readAlarm to return
 * an alarm object for a specified alarm Id, getAllAlarms to retreive all stored alarms, an update
 * method to edit existing alarms (provision for future iterations of the app) and a delete
 * method to remove alarms from the database.
 *
 * Created by Conor Taggart (40164305) on 08-Apr-16.
 */
public class SQLiteHelperAlarm extends SQLiteOpenHelper {

    // column or attribute titles
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
    // array of the attribute names
    private static final String[] COLUMNS = {ALARM_ID, ALARM_NAME, ALARM_MEMO, ALARM_SOUND, ALARM_VOLUME,
            ALARM_RECURRING, ALARM_DAYS, ALARM_HOUR, ALARM_MIN, ALARM_QR_CODE, ALARM_ON};
    // name of the database
    private static final String DATABASE_NAME = "Alarms.db";
    // version number
    private static final int DATABASE_VERSION = 1;
    // SQLite create table statement
    private static final String DATABASE_CREATE = "CREATE TABLE " + ALARMS
            + " ( " + ALARM_ID + " integer primary key autoincrement, "
            + ALARM_NAME + " text not null, "
            + ALARM_MEMO + " text not null, "
            + ALARM_SOUND + " text, "
            + ALARM_VOLUME + " integer, "
            + ALARM_RECURRING + " numeric, "
            + ALARM_DAYS + " integer, "
            + ALARM_HOUR + " integer, "
            + ALARM_MIN + " integer, "
            + ALARM_QR_CODE + " text, "
            + ALARM_ON + " numeric"
            + ");";
    // Default constructor to contruct a SQLiteHelperAlarm object consisting of application context,
    // the name of the database and version
    public SQLiteHelperAlarm(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Generate the database on first installation of the app
     * @param db - a SQLiteDatabase object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // execute the DATABASE_CREATE constant
        db.execSQL(DATABASE_CREATE);
    }

    /**
     * In the event that the current database requires replacement, it can be revisioned upward.
     * Note: existing tables and as a result information/alarms will be lost if this method is
     * called.
     * @param db - SQLiteDatabase object
     * @param oldVersion - current version of the database (int)
     * @param newVersion - new or upcoming version number (int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop or remove the current tables if they exist on device (table call ALARMS constant)
        db.execSQL("DROP TABLE IF EXISTS " + ALARMS);
        // execute the onCreate method with current database
        onCreate(db);
    }

    /**
     * Add a new alarm to the alarm database, specified in the passed argument. The database will
     * return a unique id value for the alarm id as a long value. Note: the alarm name and memo
     * properties must not be null otherwise an exception will be thrown.
     * @param alarm - Alarm object
     * @return - unique alarm id of type long.
     */
    public long createAlarm(Alarm alarm) {
        // get a writable db object for the SQLiteHelperAlarm class
        SQLiteDatabase db = this.getWritableDatabase();
        // instantiate a contentValues object to retain the properties of the alarm to created
        // until the statement can be executed
        ContentValues values = new ContentValues();
        // set each of the alarm values
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
        // insert the alarm returning the generated id
        long id = db.insert(ALARMS, null, values);
        // close the database resource to prevent db leak
        db.close();
        // return the alarm id
        return id;
    }

    /**
     * Returns an alarm object for a given or passed unique alarm identifier.
     * @param id - unique alarm identifier previously generated by the SQLite db
     * @return alarm - Alarm.class instance for a given id
     */
    public Alarm readAlarm(long id) {
        // get a readable SQLiteDatabase object for the SQLiteHelperDatabase
        SQLiteDatabase db = this.getReadableDatabase();
        // get the returned cursor object from the result of the executed query for the given
        // alarms table, for all columns, for a given alarm id
        Cursor cursor = db.query(ALARMS, COLUMNS, " id = ?", new String[]{
                String.valueOf(id)
        }, null, null, null, null);
        // if the returned cursor is not empty move to the first resultant row
        if (cursor != null && cursor.moveToNext()) {
            cursor.moveToFirst();
        }
        // create a new instance of alarm
        Alarm alarm = new Alarm();
        // set each of the object vars
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
        // release the database
        db.close();
        // return the alarm
        return alarm;
    }

    /**
     * Retrieves all instances of alarm objects from the SQLiteHelperAlarm database.
      * @return List of Alarm objects
     */
    public List getAllAlarms() {
        // create a linked list
        List alarms = new LinkedList();
        // create SQLite query to retrieve all alarms from Alarms table
        String query = "SELECT * FROM " + ALARMS;
        // get the db object
        SQLiteDatabase db = this.getWritableDatabase();
        // execute the query returning the resultset as a cursor
        Cursor cursor = db.rawQuery(query, null);
        // initialise list
        Alarm alarm = null;
        try {
            // for each returned alarm add the retrieved object to the list, setting each property
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
        } catch (Exception exception) {
            Log.e("error", "SQLite ReadAll Error");
        }
        // release the database
        db.close();
        // return the list of all alarms in the database
        return alarms;
    }

    /**
     * Updates the passed alarm. Unused in the current revision of the app.
     * @param alarm - alarm object with values to be updated in the SQLiteHelperAlarm database.
     * @return - the int number of rows affected by the update - unless an error has occurred value
     *  will be 1.
     */
    public int update(Alarm alarm) {
        // get the SQLiteHelperAlarm database as a writable object
        SQLiteDatabase db = this.getWritableDatabase();
        // contentvalues object to retain the alarm properties until the statement is executed
        ContentValues values = new ContentValues();
        // set each of the alarm properties to the contentvalues object
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
        // execute the statement on the database for a given alarm id
        int i = db.update(ALARMS, values, ALARM_ID + " = ?", new String[]
                {String.valueOf(alarm.getId())});
        // release the database
        db.close();
        // return the number of rows affected
        return i;
    }

    /**
     * Removes a specified alarm object from the database. Uses the alarm id property of the object
     * to specify which element is removed from the database.
     * @param alarm - the Alarm to be removed from the database
     */
    public void deleteAlarm(Alarm alarm) {
        // get a writable object of the SQLiteHelperAlarm database
        SQLiteDatabase db = this.getWritableDatabase();
        // execute a query to remove the alarm from the database with the specified id
        db.delete(ALARMS, ALARM_ID + " = ?", new String[]{String.valueOf(alarm.getId())});
        // release the database object
        db.close();
    }
}
