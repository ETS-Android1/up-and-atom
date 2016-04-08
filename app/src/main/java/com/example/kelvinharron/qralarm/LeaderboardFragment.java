package com.example.kelvinharron.qralarm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kelvinharron.qralarm.R;

/**
 * lass is used to inflate the alarm fragment view on our interface.
 * Created by kelvinharron on 04/04/2016.
 * TODO: Replace hardcoded leaderboard fragment and view with a dynamic system that pulls from web server. Conor suggests using AWS for web storage.
 */
public class LeaderboardFragment extends Fragment {
    /**
     * Default class constructor, empty as standard
     */
    public LeaderboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This method inflates the leaderboard fragment view object so that it displays on our activity interface.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.leaderboard_fragment, container, false);
    }
}

