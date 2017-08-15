package edu.rosehulman.lix4.petlf;

import android.support.v4.app.DialogFragment;
import android.widget.NumberPicker;

import java.util.Date;

/**
 * Created by phillee on 8/14/2017.
 */

public class DatePickerFragment extends DialogFragment
        implements NumberPicker.OnValueChangeListener {
    private Date mDate;


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }
}
