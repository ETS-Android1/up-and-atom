package com.example.kelvinharron.qralarm;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Class is used to inflate the alarm fragment view on our interface.
 * Created by kelvinharron on 04/04/2016.
 */
public class AlarmsFragment extends Fragment {
    /**
     * Default class constructor, empty as standard
     */
    public AlarmsFragment() {
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
        return inflater.inflate(R.layout.alarm_fragment, container, false);
    }
}
