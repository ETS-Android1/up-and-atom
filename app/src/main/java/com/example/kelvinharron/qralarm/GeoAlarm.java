package com.example.kelvinharron.qralarm;

import android.media.RingtoneManager;
import android.net.Uri;

import com.google.android.gms.location.Geofence;

import java.util.Arrays;

import java.util.HashMap;

/**
 * Created by Conor on 17/04/2016.
 */
public class GeoAlarm {

    private int id;
    private String name;
    private boolean recurring;
    private Integer[] days;
    private Uri sound;
    private float volume;
    private HashMap<String, ScanItem> itemList;
    private double latitude;
    private double longitude;
    private float radius;
    private boolean on;

    public static String strSeparator = ",";
    public static final float MIN_VOLUME = 0.0f;
    public static final float MAX_VOLUME = 1.0f;

    /**
     * Default Constructor
     */
    public GeoAlarm() {
        initialise();
    }


    public GeoAlarm(int id, String name, boolean recurring, Integer[] days, Uri sound,
                    float volume, HashMap<String, ScanItem> itemList, double latitude,
                    double longitude, float radius, boolean on) {
        initialise();
        this.id = id;
        this.name = name;
        this.recurring = recurring;
        this.days = days;
        this.sound = sound;
        this.volume = volume;
        this.itemList = itemList;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.on = on;
    }

    private void initialise() {
        itemList = new HashMap<>();
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

    public void setTitle(String name) {
        this.name = name;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public Integer[] getDays() {
        return days;
    }

    public void setDays(Integer[] days) {
        this.days = days;
    }

    public Uri getSound() {
        return sound;
    }

    public void setSound(String soundFile) {
        try {
            sound = Uri.parse(soundFile);
            // setting an exception in case parsing fails or soundFile not set
        } catch (NullPointerException | UnsupportedOperationException exception) {
            sound = defaultSound();
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

    public HashMap<String, ScanItem> getItemList() {
        return itemList;
    }

    public void setItemList(HashMap<String, ScanItem> itemList) {
        this.itemList = itemList;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
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

    public int storeRecurring() {
        int isRecurring;
        if (this.recurring) {
            isRecurring = 1;
        } else {
            isRecurring = 0;
        }
        return isRecurring;
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


    public void addItem(String item, String code) throws IllegalArgumentException {
        if (!itemList.containsKey(item)) {
            ScanItem scanItem = new ScanItem(code, true);
            itemList.put(item, scanItem);
        } else {
            throw new IllegalArgumentException("Item name already exists. Please enter another.");
        }
    }

    public void removeItem(String item) throws IllegalArgumentException {
        if (itemList.containsKey(item)) {
            itemList.remove(item);
        } else {
            throw new IllegalArgumentException("Item doesn't exist.");
        }
    }

    public Geofence generateGeofence() {
        return new Geofence.Builder()
                .setRequestId(new Integer(id).toString())
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(latitude, longitude, radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    @Override
    public String toString() {
        return "ID : " + this.id + "\n" +
                "Name : " + "\n" + this.name + "\n" +
                "Recurring : " + "\n" + this.recurring + "\n" +
                "Days : " + "\n" + Arrays.toString(this.days) + "\n" +
                "Volume : " + "\n" + +this.volume + "\n" +
                "Sound URI : " + "\n" + this.sound + "\n" +
                "Alarm On  : " + "\n" + this.on + "\n" +
                "Item List : " + this.itemList.toString();
    }

}
