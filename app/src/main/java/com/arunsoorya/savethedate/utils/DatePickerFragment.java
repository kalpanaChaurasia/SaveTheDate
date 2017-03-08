package com.arunsoorya.savethedate.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by arunsoorya on 11/02/17.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private DateChangeListener dateChangeListener;
    private int[] dateArrayr;

    public static DatePickerFragment getInstance(int[] dateArray) {
        DatePickerFragment pickerFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putIntArray("data", dateArray);
        pickerFragment.setArguments(bundle);
        return pickerFragment;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();

    }

    public void setDateChangeListener(DateChangeListener dateChangeListener) {
        this.dateChangeListener = dateChangeListener;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateArrayr = getArguments().getIntArray("data");

        if (dateArrayr.length > 1) {
            year = dateArrayr[0];
            month = dateArrayr[1];
            day = dateArrayr[2];
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if (dateChangeListener != null) {
            dateChangeListener.onDateSet(year, month, day);
        }
    }
}
