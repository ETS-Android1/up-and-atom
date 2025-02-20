package com.example.kelvinharron.qralarm;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

/**
 * This Dialog is used to display the name of the app, the develoeprs and the logo. It is accessed
 * through the menu main controls from the ActivityMain.class view.
 * <p/>
 * A cool little dialog as a treat that quite a few applications have.
 * <p/>
 * Created by kelvinharron on 17/04/2016.
 */
public class DialogAbout extends DialogFragment implements View.OnClickListener {

    /**
     * Button is used to dismiss the dialog once the user is ready to leave.
     */
    private Button dismiss;

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
        View viewFragment = inflater.inflate(R.layout.dialog_about, container);
        getDialog().setTitle("About");
        dismiss = viewFragment.findViewById(R.id.dismissButton);
        dismiss.setOnClickListener(this);
        return viewFragment;
    }

    /**
     * When the user is ready to leave the screen, we simply dismiss the dialog from view and return
     * to the previous view/activity.
     *
     * @param viewFragment
     */
    @Override
    public void onClick(View viewFragment) {
        if (viewFragment.getId() == R.id.dismissButton) {
            dismiss();
        }
    }
}