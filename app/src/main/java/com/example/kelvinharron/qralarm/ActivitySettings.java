package com.example.kelvinharron.qralarm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This class file allows us to setup an activity through an intent to access the standard
 * android preferences screen defined in preferences.xml. This allows us to store values using a key
 * and away out of normal view as traditionally expected by users.
 * <p/>
 * Created by kelvinharron on 21/04/2016.
 */
public class ActivitySettings extends AppCompatActivity {

    /**
     * The preferences framework takes responsibility here as it draws dialog boxes and handles the
     * intent inside the preferences file meaning we simply need to set it up using this activity
     * as a foundation to do so.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        setupPreferencesFragment();
    }

    /**
     * Creates the preference fragment and a generic content UI is inflated. Replaces the fragment
     * and commits then drawn on screen to display our typical android like settings screen.
     */
    private void setupPreferencesFragment() {
        getFragmentManager().beginTransaction().replace(android.R.id.content, new FragmentPreferences()).commit();
    }
}

