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
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * This class is the entry point to the application where we will start our application home screen and associated behavior.
 */
public class MainActivity extends AppCompatActivity {


    private FragmentManager fragmentManager;
    /**
     * Global variable that allows us to implement a tab based browsing experience with the tabbed layout.
     */
    private ViewPager viewPager;


    private AlarmSQLiteHelper alarmSQLiteHelper;
    /**
     * This is the start of the application lifecycle. Sets the main content layout while calling/initilisng methods to setup UI elements.
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

    @Override
    protected void onResume(){
        super.onResume();
        setupViewPager(viewPager);
    }

    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // setting up database
        alarmSQLiteHelper = new AlarmSQLiteHelper(this);

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run - want db to be persistent even on activity upgrade as re-adding alarms on each update would be irritating to user
       if (savedVersionCode == DOESNT_EXIST) {
            alarmSQLiteHelper.onUpgrade(alarmSQLiteHelper.getWritableDatabase(),0,1);
        }

        //alarmSQLiteHelper.onUpgrade(alarmSQLiteHelper.getWritableDatabase(),0,1);
        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Method handles the action bar quick access to settings and about page.
     * TODO: reimplement action bar functionality in XML
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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
             Intent launchNewIntent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivityForResult(launchNewIntent, 0);
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
     * Method for the famous floating action button. Once clicked.
     * Once clicked, it will open the NewAlarmDialogFragment allowing a user to choose the type of alarm to create.
     * TODO: explore use of startActivityForResult function instead of standard startActivity. Might be a better choice when creating a new object?
     */
    private void setupFAB() {
        FloatingActionButton addAlarmFAB = (FloatingActionButton) findViewById(R.id.floating_action_button);
        addAlarmFAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showNewAlarmDialog();
            }
        });
    }

    /**
     * Allows us to establish a link to the NewAlarmDialogFragment.
     * This displays a prompt to the user when the FAB is clicked allowing them to choose the type of alarm they want to create.
     */
    private void showNewAlarmDialog() {
        fragmentManager = getSupportFragmentManager();
        NewAlarmDialogFragment newAlarmDialogFragment = new NewAlarmDialogFragment();
        newAlarmDialogFragment.show(fragmentManager, "Add New Alarm");
    }

    private void showAboutDialog(){
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
        adapter.addFragment(new TimeAlarmFragment(), "Time");
        adapter.addFragment(new LocationAlarmFragment(), "Location");
        viewPager.setAdapter(adapter);
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
