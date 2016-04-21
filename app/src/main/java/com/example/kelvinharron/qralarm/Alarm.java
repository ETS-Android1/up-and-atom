package com.example.kelvinharron.qralarm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: UPDATE DAYS SETTER AND GETTER
 * Created by Conor on 08-Apr-16.
 */
public class Alarm {

    private long id;
    private String name;
    private String memo;
    private boolean recurring;
    private Uri sound;
    private float volume;
    private Integer[] days;
    private int hour;
    private int min;
    private String qrResult;
    private boolean on;

    public static final int MIN_HOUR = 0;
    public static final int MAX_HOUR = 23;
    public static final int MIN_MINUTE = 0;
    public static final int MAX_MINUTE = 23;
    public static final int DEFAULT_HOUR = 8;
    public static final int DEFAULT_MIN = 30;
    public static final float MIN_VOLUME = 0.0f;
    public static final float MAX_VOLUME = 1.0f;

    public static String strSeparator = ",";

    /**
     * Default Constructor
     */
    public Alarm() {
    }

    public Alarm(long id, String name, String memo, boolean recurring, String sound, float volume, Integer[] days, int hour, int min, String qrResult, boolean on) {
        this.id = id;
        this.name = name;
        this.memo = memo;
        this.recurring = recurring;
        setSound(sound);
        setVolume(volume);
        this.days = days;
        this.hour = hour;
        this.min = min;
        this.qrResult = qrResult;
        this.on = on;
    }

    /**
     * DOES NOT HAVE ALL THE ARGUMENTS, ONLY TESTED ENOUGH TO DISPLAY ON CARD VIEW
     *
     * @param name
     * @param memo
     * @param days
     * @param hour
     * @param min
     * @param on
     */
    public Alarm(String name, String memo, Integer[] days, int hour, int min, boolean on) {
        this.name = name;
        this.memo = memo;
        this.days = days;
        this.hour = hour;
        this.min = min;
        this.on = on;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(int recurring) {
        this.recurring = (recurring == 1);
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public void setSound(String soundFile) {
        try {
            sound = Uri.parse(soundFile);
            // setting an exception in case parsing fails or soundFile not set
        } catch (NullPointerException | UnsupportedOperationException exception) {
            sound = defaultSound();
        }
    }

    public Uri getSound() {
        return sound;
    }

    public void setDays(String daysDB) {

        System.out.print("printing stored days " + daysDB);

        if (daysDB != null && daysDB.length() > 0) {
            String[] days = daysDB.split(strSeparator);

            this.days = new Integer[days.length];

            for (int count = 0; count < days.length; count++) {
                if (!days[count].equals("") || days[count].equals(null)) {
                    this.days[count] = new Integer(days[count]);
                }
            }
        } else {
            this.days = new Integer[1];
            this.days[0] = new Integer(0);
        }
    }

    public void setDays(Integer[] days) {
        this.days = days;
    }

    public Integer[] getDays() {
        return this.days;
    }

    public void setQrResult(String qrResult) {
        this.qrResult = qrResult;
    }

    public String getQrResult() {
        return this.qrResult;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) throws IllegalArgumentException {

        if (hour >= MIN_HOUR && hour <= MAX_HOUR) {
            this.hour = hour;
        } else {
            this.hour = DEFAULT_HOUR;
            throw new IllegalArgumentException("Hours should be between " + MIN_HOUR + " & " + MAX_HOUR + ". Defaulted to 8 hours");
        }
    }

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

    public void setMin(int min) throws IllegalArgumentException {

        if (hour >= MIN_HOUR && hour <= MAX_HOUR) {
            this.min = min;
        } else {
            this.min = DEFAULT_MIN;
            throw new IllegalArgumentException("Minutes should be between " + MIN_MINUTE + " & " + MAX_MINUTE + ". Defaulted to 30 minutes");
        }
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) throws IllegalArgumentException {

        if (volume >= MIN_VOLUME && volume <= MAX_VOLUME) {
            this.volume = volume;
        } else {
            throw new IllegalArgumentException("Volume should be between +" + MIN_VOLUME * 100 + "% & " + MAX_VOLUME * 100 + "%.");
        }
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public void setOn(int on) {
        this.on = (on == 1);
    }

    public int storeRecurring() {
        int isRecurring;
        if (this.recurring) {
            isRecurring = 1;
        } else {
            isRecurring = 0;
        }
        return isRecurring;
    }


    public String storeSound() {
        if (sound != null) {
            return sound.toString();
        } else {
            return defaultSound().toString();
        }
    }

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

    //TODO turn into string builder
    public String storeDays() {
        String str = "";
        for (int count = 0; count < days.length; count++) {
            str += days[count];
            if (count < days.length - 1) {
                str += strSeparator;
            }
        }
        return str;
    }

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