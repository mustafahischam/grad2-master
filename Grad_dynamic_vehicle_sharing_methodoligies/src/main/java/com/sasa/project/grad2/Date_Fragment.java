package com.sasa.project.grad2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class Date_Fragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {//// floating window
   private int year;
   private int month;
  private int day;
    private Activity activity;

    private DatePickerDialog.OnDateSetListener onclick;

    public void onAttach(Activity activity) {/// host activity in Create_post_activity onlistner
        super.onAttach(activity); // must use on click listner
        this.activity = activity;
        try {
            onclick = (DatePickerDialog.OnDateSetListener) activity;
        } catch (ClassCastException e) {

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
         year = calendar.get(Calendar.YEAR);
         month = calendar.get(Calendar.MONTH);
         day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(activity, onclick, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
      //  **Log.w("DatePicker","Date = " + year);**
    }
}
