package com.example.kelvinharron.qralarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * UNUSED CLASS FILE
 * Not used as we did not implement Geo Alarms.
 * <p/>
 * Class is used to inflate the alarm fragment view on our interface.
 * Created by kelvinharron on 04/04/2016.
 */
public class FragmentAlarmLocation extends Fragment {

    private View layout;

    /**
     * Default class constructor.
     */
    public FragmentAlarmLocation() {
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
        layout = inflater.inflate(R.layout.activity_new_alarm_location, container, false);

        return layout;
    }

}