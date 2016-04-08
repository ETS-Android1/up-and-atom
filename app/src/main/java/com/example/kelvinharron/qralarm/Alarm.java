package com.example.kelvinharron.qralarm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by Conor on 08-Apr-16.
 */
public class Alarm {

    private int id;
    private String name;
    private String memo;
    private boolean recurring;
    private Uri sound;
    private float volume;
    private Integer[] days;
    private int hour;
    private int min;
    private Bitmap qr;
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

    public Alarm() {

    }

    public void setRingtone(String soundFile) {
        try {
            sound = Uri.parse(soundFile);
            // setting an exception in case parsing fails or soundFile not set
        } catch (NullPointerException | UnsupportedOperationException exception) {
            sound = defaultSound();
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

    public Uri getRingtone() {
        return sound;
    }

    public String storeSound() {
        if (sound != null) {
            return sound.toString();
        } else {
            return defaultSound().toString();
        }
    }

    public void setDays(String daysDB) {
        String[] days = daysDB.split(strSeparator);

        this.days = new Integer[days.length];

        for (int count = 0; count < days.length; count++) {
            this.days[count] = new Integer(days[count]);
        }
    }

    public Integer[] getDays() {
        return this.days;
    }

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

    public void setQr(ImageView qrImage) {
        qrImage.buildDrawingCache();
        this.qr = qrImage.getDrawingCache();
    }

    public void setQr(Bitmap qrImage) {
        this.qr = qrImage;
    }

    public void setQr(byte[] qrBlob) {
        this.qr = BitmapFactory.decodeByteArray(qrBlob, 0, qrBlob.length);
    }

    public Bitmap getQr() {
        return qr;
    }

    public byte[] storeQr() {
        ByteArrayOutputStream qrBlob = new ByteArrayOutputStream();
        qr.compress(Bitmap.CompressFormat.PNG, 0, qrBlob);
        return qrBlob.toByteArray();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int storeRecurring() {
        int isRecurring;
        if (this.recurring) {
            isRecurring = 1;
        } else {
            isRecurring = 0;
        }
        return isRecurring;
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

    public int getMin() {
        return min;
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
        this.recurring = (on == 1);
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
                "Bitmap Saved : " + "\n" + ((this.qr != null));
    }
}
