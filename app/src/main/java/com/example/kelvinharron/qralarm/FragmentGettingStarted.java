package com.example.kelvinharron.qralarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * Fragment class used to display the information for getting started.
 * This fragment is controlled by the view pager and tab controller contained in the ActivityMain class file.
 * This allows us to easily create and implement more tabs to the main interface in future should we need to.
 * Created by kelvinharron on 22/04/2016.
 */
public class FragmentGettingStarted extends Fragment {


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
        View layout = inflater.inflate(R.layout.fragment_getting_started, container, false);
        return layout;
    }
}
