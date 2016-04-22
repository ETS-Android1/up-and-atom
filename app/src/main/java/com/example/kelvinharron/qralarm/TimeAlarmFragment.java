package com.example.kelvinharron.qralarm;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Class is used to inflate the alarm fragment view on our interface.
 * Created by kelvinharron on 04/04/2016.
 */
public class TimeAlarmFragment extends Fragment {


    private RecyclerView recyclerView;
    private TimeAlarmAdapter adapter;
    private View layout;
    private Context context;

    /**
     * Default class constructor, empty as standard
     */
    public TimeAlarmFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    /**
     * This method inflates the alarm fragment view object so that it displays on our activity interface.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_alarm_time, container, false);
        createRecyclerView();
        return layout;
    }

    /**
     * Method instanties our recyclerview view object which is a very efficient means of displaying lists of data.
     * Instantiates our adapter class which binds data properties to a view object.
     * LinearLayoutManager is set by default to display data vertically, but can also show grids or staggered views of data.
     */
    private void createRecyclerView() {
        recyclerView = (RecyclerView) layout.findViewById(R.id.time_alarm_recycler_view);
        adapter = new TimeAlarmAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * Test data class. Local arraylists provide test data
     * TODO: HOUR AND MIN SHOW ONE DIGIT INSTEAD OF TWO, RELATED TO PHONE TIME SETTINGS?
     * TOOD: alarmDays SHOWS NULL ON CARD VIEWS
     *
     * @return
     */
    public List<Alarm> getData() {
        return new AlarmSQLiteHelper(context).getAllAlarms();
    }
}