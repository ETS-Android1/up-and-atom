package com.example.kelvinharron.qralarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Fragment used by the ActivityPreferences class to acces the preferences file that we use for ready access
 * to both the user changing
 *
 * Created by kelvinharron on 04/04/2016.
 */
public class FragmentPreferences extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }
}
