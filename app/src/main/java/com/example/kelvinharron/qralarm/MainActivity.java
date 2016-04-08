package com.example.kelvinharron.qralarm;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

/**
 * This class is the entry point to the application where we will start our application home screen and associated behavior.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Global variable that allows us to implement a tab based browsing experience with the tabbed layout.
     */
    private ViewPager viewPager;

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

        // runtime error if viewpager is segregated into its own method and called in main
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        // setup the tabbed layout, doesn't show if segregated into its own method and called in main
        TabLayout tabLayout;
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        //TODO: implement recycler view functionality via class and adapter
        RecyclerView recyclerview = (RecyclerView) findViewById(R.id.alarmListRecyclerView);
        //recyclerview.setHasFixedSize(true);
        //LinearLayoutManager llm = new LinearLayoutManager(context);
        //recyclerview.setLayoutManager(llm);

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
     * Once clicked, it will open the AddNewAlarm Activity and allow a user to create a new alarm.
     * TODO: explore use of startActivityForResult function instead of standard startActivity. Might be a better choice when creating a new object?
     */
    private void setupFAB() {
        FloatingActionButton addAlarmFAB = (FloatingActionButton) findViewById(R.id.floating_action_button);
        addAlarmFAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNewAlarm.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Method responsible for creating our tabbed fragments and allowing us to display them.
     * Fragments are given a string at the end allowing us to reference them later if need be.
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new com.example.kelvinharron.qralarm.AlarmsFragment(), "Alarm");
        adapter.addFragment(new com.example.kelvinharron.qralarm.LeaderboardFragment(), "Leaderboard");
        viewPager.setAdapter(adapter);
    }

    /**
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
/**
 * TODO: link with our local database alarm and enable storage, deletion and editing of alarm objects
 * RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
 * recyclerView.setHasFixedSize(true);
 * LinearLayoutManager llm = new LinearLayoutManager(this);
 * llm.setOrientation(LinearLayoutManager.VERTICAL);
 * recyclerView.setLayoutManager(llm);
 * <p>
 * AlarmAdapter alarmAdapter = new AlarmAdapter(createList(1));
 * recyclerView.setAdapter(alarmAdapter);
 * <p>
 * <p>
 * private List<AlarmInfo> createList(int size) {
 * List<AlarmInfo> result = new ArrayList<AlarmInfo>();
 * for (int loop = 1; loop <= size; loop++){
 * AlarmInfo alarms = new AlarmInfo();
 * alarms.title = AlarmInfo.TYPE_PREFIX + loop;
 * alarms.memo = AlarmInfo.TYPE_PREFIX + loop;
 * <p>
 * result.add(alarms);
 * }
 * return result;
 * }
 * }
 **/
