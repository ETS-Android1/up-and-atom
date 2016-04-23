package com.example.kelvinharron.qralarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Trace;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;

/**
 * Created by kelvinharron on 21/04/2016.
 */
public class ActivitySettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        setupPreferencesFragment();
    }

    private void setupPreferencesFragment() {
        getFragmentManager().beginTransaction().replace(android.R.id.content, new FragmentPreferences()).commit();
    }

}

