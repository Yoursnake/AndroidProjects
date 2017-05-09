package com.example.shengliyi.criminalintentclass.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

import com.example.shengliyi.criminalintentclass.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by shengliyi on 2017/3/19.
 */

public class TimePickerFragment extends DialogFragment {
    public static final String EXTRA_DIALOG_TIME = "extra_dialog_time";
    private static final String ARG_TIME = "argument_time";

    private TimePicker mTimePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) view.findViewById(R.id.dialog_time_time_picker);

        Date date = (Date) getArguments().getSerializable(ARG_TIME);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

//        final int second = calendar.get(Calendar.SECOND);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.time_picker_title)
                .setView(view)
                .setPositiveButton(R.string.time_picker_positive_button, new DialogInterface.OnClickListener() {
                    @TargetApi(android.os.Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            int hour = mTimePicker.getHour();
                            int minute = mTimePicker.getMinute();
                            Date date = new GregorianCalendar(year,month,day,hour,minute).getTime();
                            sendResult(Activity.RESULT_OK, date);
                        }
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DIALOG_TIME, date);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public static TimePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
