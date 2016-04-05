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
 * Created by kelvinharron on 05/04/2016.
 */
public class AlarmOverrideDialogFragment extends DialogFragment implements View.OnClickListener {


    EditText passcodeEditText;
    Button cancelButton, confirmButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.override_dialog_fragment, container);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        confirmButton = (Button) view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);
        setCancelable(false);
        return view;
    }


    public void onClick(View view) {

        if (view.getId() == R.id.confirmButton) {
            //ToDO: add validation for int, agree passcode length with team
            passcodeEditText.getText().toString();
            dismiss();
        }

        if (view.getId() == R.id.cancelButton) {
            dismiss();
        }

    }

}

