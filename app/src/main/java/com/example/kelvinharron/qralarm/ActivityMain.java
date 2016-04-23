package com.example.kelvinharron.qralarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

/**
 * This class is the entry point to the application where we will start our application home screen and associated behavior.
 */
public class ActivityMain extends AppCompatActivity {


    /**
     * Used globally to create transactions for adding our fragments into view when appropriate.
     */
    private FragmentManager fragmentManager;

    /**
     * Global variable that allows us to implement a tab based browsing experience with the tabbed layout.
     */
    private ViewPager viewPager;

    /**
     * Global variable that allows us access to the apps SQLite database that we use to...
     */
    private SQLiteHelperAlarm SQLiteHelperAlarm;

    /**
     * This is the start of the application lifecycle. Sets the main content layout while calling/initialising methods to setup UI elements and
     * checking the status of the database objects.
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
        TabLayout tabLayout;
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    /**
     * As part of the application lifecycle, we need to refresh the view of our alarm object elements if there are any when the user goes out and into the applicaiton.
     */
    @Override
    protected void onResume() {
        super.onResume();
        setupViewPager(viewPager);
    }

    /**
     *
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
        }

        //SQLiteHelperAlarm.onUpgrade(SQLiteHelperAlarm.getWritableDatabase(),0,1);
        // Update the s+hared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Method handles the action bar quick access to settings and about page.
     * We control the view
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Method for the famous floating action button. Includes an inner onclick method.
     * Once clicked, it will open the new alarm intent allowing a user to create an alarm.
     */
    private void setupFAB() {
        FloatingActionButton addAlarmFAB = (FloatingActionButton) findViewById(R.id.floating_action_button);
        addAlarmFAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //showNewAlarmDialog();
                intentNewAlarm();

            }
        });
    }

    /**
     * Simple method that once called, will create an intent for launching the activity settings view.
     */
    private void intentSettings() {
        Intent launchNewIntent = new Intent(this, ActivitySettings.class);
        startActivity(launchNewIntent);
    }

    /**
     * Simple method that once called, will create an intent for launching the add new alarm view.
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
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentTimeAlarm(), "Alarms");
        adapter.addFragment(new FragmentGettingStarted(), "Getting Started");
        viewPager.setAdapter(adapter);
    }


    /**
     * DEPRECIATED method.
     * Allows us to establish a link to the DialogChooseAlarm.
     * This displays a prompt to the user when the FAB is clicked allowing them to choose the type of alarm they want to create.
     */
    private void showNewAlarmDialog() {
        fragmentManager = getSupportFragmentManager();
        DialogChooseAlarm dialogChooseAlarm = new DialogChooseAlarm();
        dialogChooseAlarm.show(fragmentManager, "Add New Alarm");
    }

    /**
     * ,
     * Inner class method that handles the setup of the view pager adapter which allows us to link the fragments to the adapter for the tabbed/viewpager view.
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
