package com.example.kelvinharron.qralarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

/**
 * UNUSED CLASS FILE
 * <p/>
 * Draws and handles the logic behind the use of the dialog drawn when a user clicks the FAB.
 * A user should be able to choose the type of alarm they wish to create.
 * Created by kelvinharron on 17/04/2016.
 */
public class DialogChooseAlarm extends DialogFragment implements View.OnClickListener {

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
        locationAlarmButton = (Button) viewFragment.findViewById(R.id.newTimeAlarm);
        locationAlarmButton.setOnClickListener(this);
        setCancelable(true);
        return viewFragment;
    }


    @Override
    public void onClick(View viewFragment) {
        if (viewFragment.getId() == R.id.newTimeAlarm) {
            intentNewTimeAlarm();
            dismiss();
        }
        if (viewFragment.getId() == R.id.newLocationAlarm) {
            intentNewLocationAlarm();
            dismiss();
        }
    }

    private void intentNewTimeAlarm() {
        Intent openActivity = new Intent(getContext(), ActivityAddNewAlarmTime.class);
        startActivity(openActivity);
    }

    private void intentNewLocationAlarm() {
        Intent openActivity = new Intent(getContext(), ActivityAddNewAlarmLocation.class);
        startActivity(openActivity);
    }

}
