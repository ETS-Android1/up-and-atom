package com.example.kelvinharron.qralarm;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Fragment used by the ActivityPreferences class to access the preferences file that we use
 * for ready access to users for changing location and override codes from their default values.
 * The preferences framework takes care of loading the current values out of the app preferences
 * and writing them when changed.
 * Created by kelvinharron on 04/04/2016.
 */
public class FragmentPreferences extends PreferenceFragment {

    /**
     * Loads the preferences.xml data, which holds logic and view information in a unique way
     * compared to traditional xml layouts.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
