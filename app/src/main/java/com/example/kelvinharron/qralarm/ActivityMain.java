package com.example.kelvinharron.qralarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * The ActivityMain.class describes the entry point to the application where in the foreground,
 * we present the first view of the application that is made up of:
 * a tab layout to allow a tabbed layout,
 * a view pager to control view of selected tabs,
 * a toolbar with overflow menu access to settings and about page,
 * floating action button, easy access to adding a new alarm.
 */
public class ActivityMain extends AppCompatActivity {

    /**
     * Used to create transactions for adding our fragments into view when appropriate.
     */
    private FragmentManager fragmentManager;

    /**
     * Used to implement a tab based browsing experience with the tabbed layout.
     * Allows user to quickly browse between different screens while in the same activity.
     */
    private ViewPager viewPager;

    /**
     * Used to provide a layout for our tabs. Allows us to show more than one horizontal view per tab.
     */
    private TabLayout tabLayout;

    /**
     * Used by the application to access  the apps SQLite database that we use to store alarms.
     */
    private SQLiteHelperAlarm SQLiteHelperAlarm;

    /**
     * This is the start of the application lifecycle.
     * Sets the main content layout while initialising methods to setup UI elements and
     * checking the status of the database and preference object values.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolBar(); // setup the toolbar
        setupFAB(); // setup floating action button
        // setting up database
        checkFirstRun();
        // runtime error if viewpager is segregated into its own method and called in main
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // setup the tabbed layout, doesn't show if segregated into its own method and called in main
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * As part of the application lifecycle, we need to refresh the view of our alarm object elements
     * if there are any when the user goes out and into the application.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Method checks the status of the database and preference objects to ensure the reliability of
     * both alarm objects if created and location if not changed from default.
     */
    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;
        // setting up database
        SQLiteHelperAlarm = new SQLiteHelperAlarm(this);
        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;
        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);
        // Check for first run - want db to be persistent even on activity upgrade as re-adding alarms on each update would be irritating to user
        if (savedVersionCode == DOESNT_EXIST) {
            SQLiteHelperAlarm.onUpgrade(SQLiteHelperAlarm.getWritableDatabase(), 0, 1);

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor;
            // Error with preferences means default location is null
            // We define the defualt location to Queen's University using the prefs editor
            StringBuilder builder = new StringBuilder();
            builder.append(54.58487776321973);
            builder.append(",");
            builder.append(-5.935247428715229);
            editor = sharedPrefs.edit();
            editor.putString("location", builder.toString());
            editor.apply();
        }
        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();
    }

    /**
     * Method inflates the menu_main layout so when accessed by the overflow menu,
     * it draws the two options we have currently defined.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Method handles the action bar quick access to settings and about page.
     * Settings and about page are typically held in this area traditionally
     * so it made sense to keep them here instead of creating additional tabs.
     * Menu setting intents are held in individual methods.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            intentSettings();
            return true;
        }
        if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Methods gets a reference to the toolbar view and sets it to our activity view.
     */
    private void setupToolBar() {
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Method for the famous floating action button. Includes an inner onclick method.
     * We choose this as its unique to android and is an example of how our app is attempting to
     * extend the look and function of stock android applications. Opens create new alarm activity.
     */
    private void setupFAB() {
        FloatingActionButton addAlarmFAB = findViewById(R.id.floating_action_button);
        addAlarmFAB.setOnClickListener(v -> intentNewAlarm());
    }

    /**
     * Method used for the main menu that once called, launches the Settings Activity intent.
     */
    private void intentSettings() {
        Intent launchNewIntent = new Intent(this, ActivitySettings.class);
        startActivity(launchNewIntent);
    }

    /**
     * Method used for the main menu that once called, launches the Add New Time Alarm Activity intent.
     */
    private void intentNewAlarm() {
        Intent openActivity = new Intent(this, ActivityAddNewAlarmTime.class);
        startActivity(openActivity);
    }

    /**
     * Simple method that once called in the menu toolbar, will allow a user to view the about dialog for the developers of the app.
     */
    private void showAboutDialog() {
        fragmentManager = getSupportFragmentManager();
        DialogAbout dialogAbout = new DialogAbout();
        dialogAbout.show(fragmentManager, "About");
    }


    /**
     * Method responsible for creating our tabbed fragments and allowing us to display them.
     * Fragments are given a string at the end allowing us to reference them later if need be.
     *
     * @param viewPager used for setup of viewpager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentTimeAlarm(), "Alarms");
        adapter.addFragment(new FragmentGettingStarted(), "Getting Started");
        viewPager.setAdapter(adapter);
    }


    /**
     * UNUSED method because we have one type of alarm currently.
     * Allows us to establish a link to the DialogChooseAlarm.
     * This displays a prompt to the user when the FAB is clicked allowing them to choose the type of alarm they want to create.
     */
    private void showNewAlarmDialog() {
        fragmentManager = getSupportFragmentManager();
        DialogChooseAlarm dialogChooseAlarm = new DialogChooseAlarm();
        dialogChooseAlarm.show(fragmentManager, "Add New Alarm");
    }

    /**
     * Inner class of the ActivityMain.class that handles the setup of the view pager adapter which allows us to link the fragments to the adapter for the tabbed/viewpager view.
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
