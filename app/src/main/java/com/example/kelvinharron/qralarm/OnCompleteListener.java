package com.example.kelvinharron.qralarm;

/**
 * Interface for handling the the entry of the override code in the ActivityDismissAlarm.class file
 * to pass it back to the calling parent activity.
 * <p/>
 * Created by Conor Taggart (40164305) on 20/04/2016.
 */
public interface OnCompleteListener {
    void onComplete(String overrideCode);
}
