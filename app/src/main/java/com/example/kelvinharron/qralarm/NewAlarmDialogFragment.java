package com.example.kelvinharron.qralarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by kelvinharron on 17/04/2016.
 */
public class NewAlarmDialogFragment extends DialogFragment implements View.OnClickListener {

    private Button timeAlarmButton;

    private Button locationAlarmButton;

    /**
     * Attaches the fragment to the activity
     *
     * @param mainActivity - in this instance the mainActivity activity
     */
    @Override
    public void onAttach(Activity mainActivity) {
        super.onAttach(mainActivity);
    }

    /**
     * Creates the dialog view and instantiates the button views.
     *
     * @param inflater           - instantiates the xml file into the view objects
     * @param container          - holds all the view objects
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewFragment = inflater.inflate(R.layout.dialog_new_alarm, container);
        getDialog().setTitle("Add new Alarm");
        timeAlarmButton = (Button) viewFragment.findViewById(R.id.newTimeAlarm);
        timeAlarmButton.setOnClickListener(this);
        setCancelable(true); // prevents a tap outside of screen or back button from cancelling the dialog
        return viewFragment;
    }


    @Override
    public void onClick(View viewFragment) {
        if (viewFragment.getId() == R.id.newTimeAlarm) {
            //TODO add Peters activity
            Intent openActivity = new Intent(getContext(), AddNewAlarm.class);
            startActivity(openActivity);
            dismiss();
        }
    }

}
