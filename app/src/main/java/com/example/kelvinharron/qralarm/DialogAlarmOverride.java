package com.example.kelvinharron.qralarm;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * This class is a fragment that when called, will allow the display of a custom dialog that is used in the ActivityAddNewAlarmTime class.
 * Subclass of DialogFragment allows us to utilise methods specific to interacting with a dialog.
 * Implementing View.OnClickListener forces us to use an onClick method that we need for registering button clicks.
 * Created by kelvinharron on 05/04/2016.
 */
public class DialogAlarmOverride extends DialogFragment implements View.OnClickListener {


    private OnCompleteListener listener;

    /**
     * EditText inside dialog where user enters a passcode that will be used to override the QR alarm in exceptional circumstance.
     */
    private EditText passcodeEditText;

    /**
     * Button inside dialog used to dismiss the dialog cancelling the passcode entry.
     */
    private Button cancelButton;

    /**
     * Button inside dialog used to confirm the passcode entry and then dismiss the dialog.
     */
    private Button confirmButton;

    private String overridePassword;


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
        View viewFragment = inflater.inflate(R.layout.dialog_override_create, container);
        cancelButton = (Button) viewFragment.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        confirmButton = (Button) viewFragment.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);
        setCancelable(false); // prevents a tap outside of screen or back button from cancelling the dialog
        passcodeEditText = (EditText)viewFragment.findViewById(R.id.editTextPasscode);
        return viewFragment;
    }

    /**
     * Handles the input of the buttons in the dialog.
     * CURRENTLY CRASHES APP WHEN YOU CLICK SUBMIT
     *
     * @param viewFragment
     */
    public void onClick(View viewFragment) {

        if (viewFragment.getId() == R.id.confirmButton) {
            //ToDO: fix app crash when confirm button is clicked + get the passcode toString and send back to Activity/database
            this.listener.onComplete(passcodeEditText.getText().toString());
            // SOURCE OF ISSUE
            dismiss(); // Dismisses dialog from view, returning focus to activity
        }

        if (viewFragment.getId() == R.id.cancelButton) {
            dismiss(); // Dismisses dialog from view, returning focus to activity
        }
    }
    /**
     * Attaches the fragment to the activity
     *
     * @param dismissAlarmActvity - in this instance the ActivityAddNewAlarmTime activity
     */
    @Override
    public void onAttach(Activity dismissAlarmActvity ) {
        super.onAttach(dismissAlarmActvity );
        try {
            this.listener = (OnCompleteListener)dismissAlarmActvity ;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(dismissAlarmActvity .toString() + " must implement OnCompleteListener");
        }
    }
}
