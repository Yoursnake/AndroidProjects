package com.example.shengliyi.criminalintent.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.shengliyi.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by shengliyi on 2017/2/2.
 */

public class DatePickerFragment extends DialogFragment {

    private static final String EXTRA_DATE = "com.example.shengliyi.criminalintent.date";

    private Date date;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        date = (Date)getArguments().getSerializable(EXTRA_DATE);

        //Create a Calendar to get the year, month, and day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_date,null);
//        DatePicker dp = new DatePicker(getActivity());
//        TimePicker tp = new TimePicker(getActivity());

        DatePicker datePicker = (DatePicker)view.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //用calendar得到的year，month，day重新转换成一个Date对象
                date = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
                //防止旋转导致的数据丢失
                getArguments().putSerializable(EXTRA_DATE, date);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.data_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }


}
