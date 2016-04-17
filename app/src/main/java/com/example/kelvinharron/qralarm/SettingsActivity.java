package com.example.kelvinharron.qralarm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Currently a stub class that might not be needed. Was attempting to create a settings tab but relegated it to the soon to be created menu.
 * The settings menu will likely rely on a PreferencesActivity or Fragment.
 * Created by kelvinharron on 04/04/2016.
 */
public class SettingsActivity extends Activity {

    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_settings);

        String[] SETTINGS = {"Time Style", "Home Location", "Alarm Override", "Alarm Volume", "Leaderboard Refresh", "About"};
        ListAdapter settingsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SETTINGS);
        ListView settingsListView = (ListView) findViewById(R.id.list);
        settingsListView.setAdapter(settingsAdapter);
    }
}
