package com.sasa.project.grad2;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.app.TimePickerDialog;


import java.util.Calendar;
public class Time_Fragments extends DialogFragment {
    private Activity activity;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        try {
            onTimeSetListener = (TimePickerDialog.OnTimeSetListener) activity;
        } catch (ClassCastException e) {
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(activity, onTimeSetListener, hour, minute,DateFormat.is24HourFormat(getActivity()));

    }
}
