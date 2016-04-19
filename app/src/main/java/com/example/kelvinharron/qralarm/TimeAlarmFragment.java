package com.example.kelvinharron.qralarm;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Class is used to inflate the alarm fragment view on our interface.
 * Created by kelvinharron on 04/04/2016.
 */
public class TimeAlarmFragment extends Fragment {


    private RecyclerView recyclerView;
    private TimeAlarmAdapter adapter;
    private View layout;

    /**
     * Default class constructor, empty as standard
     */
    public TimeAlarmFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        layout = inflater.inflate(R.layout.time_alarm_fragment, container, false);
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
    public static List<Alarm> getData() {
        List<Alarm> alarmData = new ArrayList<>();

        String[] alarmNames = {"WAKE UP DAMNIT", "Good Morning", "Big day", "alarm"};
        String[] alarmMemos = {"Scan the coffee", "Scan the milk", "Eggs code", "memo"};
        int[] alarmTimeHour = {06, 07, 03, 05};
        int[] alarmTimeMin = {30, 00, 00, 30};
        Integer[] alarmDays = {4, 3, 7, 4};
        boolean[] alarmIsOn = {true, false, true, false};

        for (int loop = 0; loop < alarmNames.length; loop++) {
            Alarm current = new Alarm();
            current.setName(alarmNames[loop]);
            current.setMemo(alarmMemos[loop]);
            current.setHour(alarmTimeHour[loop]);
            current.setMin(alarmTimeMin[loop]);
            //current.setDays(alarmDays[loop]);
            current.setOn(alarmIsOn[loop]);
            alarmData.add(current);
        }
        return alarmData;
    }

    private void checkAdapterIsEmpty (View v) {
        TextView emptyView = (TextView) v.findViewById(R.id.empty_view);
        if (getData().size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }
}