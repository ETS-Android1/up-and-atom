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
 * Class is used to inflate the alarm fragment view on our interface. Here we employ the use of the
 * RecyclerView, the custom AdapterTimeAlarm and LinearLayout to create our alarm card view objects.
 * <p/>
 * Created by kelvinharron on 04/04/2016.
 */
public class FragmentTimeAlarm extends Fragment {

    /**
     * The view widget we use to efficiently display multiple instances of unique alarms efficiently.
     * This widget coupled with the LayoutManager means we can easily customise the look and layout
     * of our alarms should we wish.
     */
    private RecyclerView recyclerView;

    /**
     * Creates a reference to our custom adapter where we bind the data of alarm objects to the views
     * held inside the card layout.
     */
    private AdapterTimeAlarm adapter;

    /**
     * Used as a means to draw the layout file where our recycler view is defined in XML.
     */
    private View layout;

    /**
     * Used as a means to get the context of the application activity.
     */
    private Context context;

    /**
     * On create of the Fragment, where we instantiate the context of the associated activity.
     *
     * @param savedInstanceState
     */
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
     * @return the recycler view layout
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
        adapter = new AdapterTimeAlarm(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        // Setting the layout to a simple linear layout allowing vertical display of the cards
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * Gets a reference to the alarm data currently stored meaning we can populate the recycler
     * view with any existing alarms.
     *
     * @return all alarms currently existing.
     */
    public List<Alarm> getData() {
        return new SQLiteHelperAlarm(context).getAllAlarms();
    }
}