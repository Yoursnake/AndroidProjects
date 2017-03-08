package com.example.shengliyi.criminalintent.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import com.example.shengliyi.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by shengliyi on 2017/2/2.
 */
//继承自 DialogFragment 表明这是一个对话框碎片
public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "com.example.shengliyi.criminalintent.date";

    private Date date;

    @NonNull
    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //通过在 CrimeFragment 使用 newInstance 存储的 argument 得到 crime 对象的数据
        date = (Date)getArguments().getSerializable(EXTRA_DATE);

        //Create a Calendar to get the year, month, and day
        //使用 Calendar 对象可以用来获取 Date 对象中准确的时间数据
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);     //注意：这里必须通过 setTime 方法设置日期参数
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);

        // 通过 dialog_date 的布局得到视图
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_date,null);
//        DatePicker dp = new DatePicker(getActivity());
//        TimePicker tp = new TimePicker(getActivity());
        // 通过布局中的 id 得到 DatePicker 对象
        DatePicker datePicker = (DatePicker)view.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //用calendar得到的year，month，day重新转换成一个Date对象
                date = new GregorianCalendar(year, monthOfYear, dayOfMonth, hour, minute).getTime();
                //防止旋转导致的数据丢失
                getArguments().putSerializable(EXTRA_DATE, date);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)      // 设置对话框的视图
                .setTitle(R.string.date_picker_title)   //设置标题
                .setPositiveButton(     //是指 PositiveButton 的文本和监听器
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();      //最后将对话框创建出来
    }

    // 将 Date 对象传回到 CrimeFragment 中
    private void sendResult(int resultCode){
        /*因为之前将 CrimeFragment 和 DatePickerFragment 设置为关联
        * 所以 getTargetFragment 指的就是 CrimeFragment
        * 如果没有 TargetFragment 则不执行任何代码*/
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    //通过 argument 存储 date
    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

}
