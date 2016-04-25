package com.example.kelvinharron.qralarm;

import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import java.util.Arrays;

/**
 * The Alarm.class describes the basic state and methods that define the characteristics of an alarm.  It consists of:
 *	a unique identifier
 *	name or title
 *	an indication of the item that was scanned or memo
 *	the alarm sound or URI to the media file to be played when it activates
 *	the time of day hours and minutes the alarm is activated at
 *	if the alarm is recurring and if so the days it activates on
 *	whether the alarm is active or inactive
 *	the QR code or barcode that was scanned
 *	the volume the alarm will activate at (unused in this version of the app but provision put in place for future iteration).
 *  Created by Conor Taggart (40164305) on 08-Apr-16.
 */
public class Alarm {
    // alarm properties
    // unique identifier
    private long id;
    // required name for alarm
    private String name;
    // require note or indicate of scanned item which will dismill alarm
    private String memo;
    // set if the alarm is a recurring one
    private boolean recurring;
    // set the media file to be played when the alarm activates
    private Uri sound;
    // unused currently, will specify the volume the alarm sound is played at when the alarm
    // activates
    private float volume;
    // integer representation of the days of the week the alarm repeats on
    private Integer[] days;
    // the hour the alarm is set to activate at
    private int hour;
    // the minute the alarm is set to activate
    private int min;
    // a string representation of the scanned QR/Barcode
    private String qrResult;
    // indicates if the alarm is set of go activate of off
    private boolean on;
    // constants for boundary conditions for alarm properties
    public static final int MAX_NAME_LENGTH = 20;
    public static final int MAX_MEMO_LENGTH = 30;
    public static final int MIN_HOUR = 0;
    public static final int MAX_HOUR = 23;
    public static final int MIN_MINUTE = 0;
    public static final int MAX_MINUTE = 23;
    public static final int DEFAULT_HOUR = 8;
    public static final int DEFAULT_MIN = 30;
    // value/separator used to separate array values that are converted to a single string. Comma
    // by default
    public static String strSeparator = ",";

    /**
     * Default Constructor
     */
    public Alarm() {
    }

    /**
     * Constructor containing all properties of Alarm class
     *
     * @param id        - unique alarm id
     * @param name      - required alarm name
     * @param memo      - required indicator of scanned qr/barcode item
     * @param recurring - indicate if alarm is recurring
     * @param sound     - string representation of URI of media file to be played when alarm activates
     * @param volume    - volume alarm will sound at (to be impletemented)
     * @param days      - integer array representing days of week
     * @param hour      - hour of day alarm will activate at
     * @param min       - min of hour alarm will activate at
     * @param qrResult  - scanned QR code string value
     * @param on        - indicate if alarm is on
     */
    public Alarm(long id, String name, String memo, boolean recurring, String sound, float volume, Integer[] days, int hour, int min, String qrResult, boolean on) {
        this.id = id;
        setName(name);
        setMemo(memo);
        this.recurring = recurring;
        setSound(sound);
        setVolume(volume);
        setDays(days);
        setHour(hour);
        setMin(min);
        this.qrResult = qrResult;
        this.on = on;
    }

    /**
     * Constructor for card views specifying required elements to be viewed in each card.
     *
     * @param name
     * @param name - required alarm name
     * @param memo - required indicator of scanned qr/barcode item
     * @param hour - hour of day alarm will activate at
     * @param min  - min of hour alarm will activate at
     * @param on   - indicate if alarm is on
     */
    public Alarm(String name, String memo, Integer[] days, int hour, int min, boolean on) {
        this.name = name;
        this.memo = memo;
        this.days = days;
        this.hour = hour;
        this.min = min;
        this.on = on;
    }

    /**
     * Returns alarm unique id
     *
     * @return - alarm id of type long
     */
    public long getId() {
        return id;
    }

    /**
     * Set alarm's unique id
     *
     * @param id - alarm id of type long
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Retrieves the alarm name
     *
     * @return name String name of alarm
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the alarm. Required value of less that 20 characters otherwise
     * IllegalArgumentException thrown.
     *
     * @param name - String of alarm name
     * @throws IllegalArgumentException - thrown if null of greater than 20 characters
     */
    public void setName(String name) throws IllegalArgumentException {
        if (TextUtils.isEmpty(name) || (name.equals(null))) {
            throw new IllegalArgumentException("Alarm name cannot be null");
        } else if (name.length() <= MAX_NAME_LENGTH) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Alarm name should be less than 20 characters");
        }
    }

    /**
     * Retrieves the alarm memo or indication of the item QR/Barcode that was scanned
     *
     * @return memo - String value of memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * Set the memo of the alarm. Required value of less that 30 characters otherwise
     * IllegalArgumentException thrown.
     *
     * @param memo - String of alarm memo
     * @throws IllegalArgumentException - thrown if null of greater than 30 characters
     */
    public void setMemo(String memo) {
        if (TextUtils.isEmpty(memo) || (memo.equals(null))) {
            throw new IllegalArgumentException("Alarm name cannot be null");
        } else if (memo.length() <= MAX_MEMO_LENGTH) {
            this.memo = memo;
        } else {
            throw new IllegalArgumentException("Alarm name should be less than 30 characters");
        }
    }

    /**
     * Indicates if the alarm is recurring (true) or not.
     *
     * @return recurring - boolean indicating if alarm is recurring
     */
    public boolean isRecurring() {
        return recurring;
    }

    /**
     * Set if the alarm is recurring (1) or not.
     *
     * @param recurring - int either 1 (true) or 0 (false) indicate if alarm is recurring.
     */
    public void setRecurring(int recurring) {
        this.recurring = (recurring == 1);
    }

    /**
     * Set if the alarm is recurring (true) or not.
     *
     * @param recurring - boolean either (true) or (false) indicate if alarm is recurring.
     */
    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    /**
     * Sets the String value of the URI that points to the alarm sound that will be played when
     * the alarm activates by a MediaPlayer service. If null the device default value is set.
     *
     * @param soundFile - String value of the URI pointing the alarm sound
     */
    public void setSound(String soundFile) {
        try {
            // converts the passed String back to the URI value
            sound = Uri.parse(soundFile);
            // setting an exception in case parsing fails or soundFile not set
        } catch (NullPointerException | UnsupportedOperationException exception) {
            // calls a method which sets the sound the the default value in the event that
            // the requested value does not exist
            sound = defaultSound();
        }
    }

    /**
     * Returns the URI of the alarm sound to be played when the alarm activates
     *
     * @return sound - URI pointing to set alarm sound
     */
    public Uri getSound() {
        return sound;
    }

    /**
     * Set the days the alarm will reoccur on each week.
     * <p/>
     * Overloaded method that converts a String value containing comma (",") separated values
     * of integers representing the days of the week i.e.
     * Sun = 1
     * Mon = 2
     * ...
     * there String daysDB = "1,2,5" will be converted to integer array [1,2,5] which represents
     * Sun, Mon, Thur.
     * <p/>
     * Used as the array of days is stored as comma separated String value in database.
     *
     * @param daysDB - comma separated String values representing days of week.
     */
    public void setDays(String daysDB) {
        // check if string is null
        if (daysDB != null && daysDB.length() > 0) {
            // split the String into an arry of String values delimited by comma
            String[] days = daysDB.split(strSeparator);
            // instantiate the days array with length of the String days array
            this.days = new Integer[days.length];
            // iterate throw array adding each value using new Integer value
            for (int count = 0; count < days.length; count++) {
                if (!days[count].equals("") || days[count].equals(null)) {
                    this.days[count] = new Integer(days[count]);
                }
            }
        } else {
            // if it is null set the array to contain 1 value, an integer contain 0
            this.days = new Integer[1];
            this.days[0] = new Integer(0);
        }
    }

    /**
     * Set the days the alarm will reoccur on each week based on their integer representation.
     * Sun = 1
     * Mon = 2
     * ...
     * Sat = 7
     *
     * @param days - Integer[] representing the days of the week the alarm will recur on
     */
    public void setDays(Integer[] days) {
        this.days = days;
    }

    /**
     * Returns the days the alarm will reoccur on each week based on their integer representation.
     * Sun = 1
     * Mon = 2
     * ...
     * Sat = 7
     *
     * @return days - Integer[] representing the days of the week the alarm will recur on
     */
    public Integer[] getDays() {
        return this.days;
    }

    /**
     * Set the QR/Barcode that will dismiss or stop the alarm.
     *
     * @param qrResult - String value of the scanned QR/Barcode
     */
    public void setQrResult(String qrResult) {
        this.qrResult = qrResult;
    }

    /**
     * Get the QR/Barcode that will dismiss or stop the alarm.
     *
     * @return qrResult - String value of the scanned QR/Barcode
     */
    public String getQrResult() {
        return this.qrResult;
    }

    /**
     * Retrieve the hour the alarm will activate on
     *
     * @return hour - int representation of hour of day alarm will activate on
     */
    public int getHour() {
        return hour;
    }

    /**
     * Set the hour the alarm wil activate on. Value must be between 0 and 23 due to number of hours
     * in each day
     *
     * @param hour - hour alarm will occur on
     * @throws IllegalArgumentException - thrown if hour less that 0 or greater than 23. if outside
     *                                  the range set to 7.
     */
    public void setHour(int hour) throws IllegalArgumentException {
        if (hour >= MIN_HOUR && hour <= MAX_HOUR) {
            this.hour = hour;
        } else {
            this.hour = DEFAULT_HOUR;
            throw new IllegalArgumentException("Hours should be between " + MIN_HOUR +
                    " & " + MAX_HOUR + ". Defaulted to 8 hours");
        }
    }

    /**
     * Get the min the alarm wil activate on. Returns the String value appending a 0 if min on or
     * between 0 and 9.
     *
     * @return min - String value of min alarm will occur on
     */
    public String getMin() {
        int digits = 2;
        String result = "";
        if (this.min <= 9) {
            result = String.format("%0" + digits + "d", this.min);
        } else {
            result = new Integer(this.min).toString();
        }
        return result;
    }

    /**
     * Set the min the alarm wil activate on. Value must be between 0 and 59 due to number of hours
     * in each day
     *
     * @param min - min alarm will occur on
     * @throws IllegalArgumentException - thrown if min less that 0 or greater than 59. if outside
     *                                  the range set to 30.
     */
    public void setMin(int min) throws IllegalArgumentException {

        if (hour >= MIN_HOUR && hour <= MAX_HOUR) {
            this.min = min;
        } else {
            this.min = DEFAULT_MIN;
            throw new IllegalArgumentException("Minutes should be between " + MIN_MINUTE + " & " + MAX_MINUTE + ". Defaulted to 30 minutes");
        }
    }

    /**
     * Return volume alarm is set at. Unused in current version of app.
     * @return volume - float value of alarm volume
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Sets the alarm volume. Unused in current version of app.
     * @param volume - float value of alarm volume
     */
    public void setVolume(float volume) {
        this.volume = volume;
    }

    /**
     * Indicated is alarm is active (true) or not.
     * @return on - boolean var indicate if alarm is active
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Overloaded method to set if the alarm is active or off.
     * @param on - boolean indicate alarm status
     */
    public void setOn(boolean on) {
        this.on = on;
    }

    /**
     * Overloaded method indicating if alarm is active or off. If 1 alarm is set to be activate
     * otherwise the alarm is off.
     * @param on - int indicating alarm status
     */
    public void setOn(int on) {
        this.on = (on == 1);
    }

    /**
     * Method used by SQLiteHelpAlarm class to store the value pointing the alarm sound file to be
     * played when the alarm activates
     * @return sound - String representation of the URI indicate alarm sound file location
     */
    public String storeSound() {
        if (sound != null) {
            return sound.toString();
        } else {
            // if alarm file not found or null return the String value of the default device sound
            return defaultSound().toString();
        }
    }

    /**
     * Method used to retrieve default device sound. In event alarm default sound not set, the
     * notification sound will be used, else the Ringtone will be used.
     * @return
     */
    private Uri defaultSound() {
        // using system default alarm
        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        // if that is null, use the default notification sound
        if (defaultUri == null) {
            defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // In case notification is null also using ringtone as second backup
            if (defaultUri == null) {
                defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return defaultUri;
    }

    /**
     * Converts the Integer array representing the days the alarm recurs on into a String value
     * with comma separators
     * @return days - String representation of days the alarm is set to recur on
     */
    public String storeDays() {
        // initialise string
        String str = "";
        // loop through each day in the integer array
        for (int count = 0; count < days.length; count++) {
            // add each day to the string
            str += days[count];
            // if not the last value append the string separator, a comma
            if (count < days.length - 1) {
                str += strSeparator;
            }
        }
        // return the string value
        return str;
    }

    /**
     * Override the toString method to display the String representation of the alarm class.
     * @return alarm - String value of the each of the alarm properties
     */
    @Override
    public String toString() {
        return "ID : " + this.id + "\n" +
                "Name : " + "\n" + this.name + "\n" +
                "Memo : " + "\n" + this.memo + "\n" +
                "Recurring : " + "\n" + this.recurring + "\n" +
                "Days : " + "\n" + Arrays.toString(this.days) + "\n" +
                "Hour : " + "\n" + +this.hour + "\n" +
                "Min : " + "\n" + this.min + "\n" +
                "Volume : " + "\n" + +this.volume + "\n" +
                "Sound URI : " + "\n" + this.sound + "\n" +
                "Alarm On  : " + "\n" + this.on + "\n" +
                "QR String  : " + "\n" + this.qrResult;
    }
}