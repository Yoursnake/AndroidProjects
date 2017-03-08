package com.example.shengliyi.criminalintent.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.shengliyi.criminalintent.R;
import com.example.shengliyi.criminalintent.Activity.CrimeListActivity;
import com.example.shengliyi.criminalintent.Activity.CrimePagerActivity;
import com.example.shengliyi.criminalintent.Entity.Crime;
import com.example.shengliyi.criminalintent.Entity.CrimeLab;

import java.util.Date;
import java.util.UUID;

/**
 * Created by shengliyi on 2017/1/18.
 */

public class CrimeFragment extends Fragment {

    private Crime crime;
    private String content;

    private EditText crimeTitle;
    private Button dateButton;
    private Button timeButton;
    private CheckBox solvedCheckBox;

    public static final String EXTRA_CRIME_ID =
            "com.example.shengliyi.criminalintent.crime_id";

    private static final String CRIME_TITLE = "com.example.shengliyi.criminalintent.crime_title";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UUID crimeId = (UUID)getActivity().getIntent()
//                .getSerializableExtra(EXTRA_CRIME_ID);

        // 开启操作栏必要的手段
        setHasOptionsMenu(true);

        // 得到通过 argument 存储在本碎片中的数据
        UUID crimeId = (UUID)getArguments()
                .getSerializable(EXTRA_CRIME_ID);
        crime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);

        /* content为 EditText 中填写的数据，在翻转的时候数据会丢失，可使用 savedInstanceState 保存
         * 数据 */
        if(savedInstanceState != null){
            content = savedInstanceState.getString(CRIME_TITLE);
        }

    }

    @TargetApi(11)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime,container,false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                CrimePagerActivity activity = (CrimePagerActivity) getActivity();
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true); //在CrimePagerActivity的操作栏设置HOME键
            }
        }

        /*crime Title*/
        crimeTitle = (EditText)view.findViewById(R.id.crime_title);
        crimeTitle.setText(crime.getTitle());
        /*设置监听器在 EditText 文本变化的时候，改变 crime 对象对应的 title 属性*/
        crimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*date Button*/
        dateButton = (Button)view.findViewById(R.id.crime_date_button);
        updateDate();
//        dateButton.setEnabled(false);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先要得到 FragmentManager 对象
                FragmentManager fm = getActivity().getSupportFragmentManager();
                //将 crime 的日期传入 DatePickerFragment 碎片
                DatePickerFragment dateDialog = DatePickerFragment.newInstance(crime.getDate());
                //建立 CrimeFragment 和 DatePickerFragment 之间的联系
                // TargetRequestCode 设置为 REQUEST_DATE，也就是 ActivityResult 中的 RequestCode
                dateDialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dateDialog.show(fm, DIALOG_DATE);
            }
        });

        /*time Button*/
        timeButton = (Button)view.findViewById(R.id.crime_time_button);
        updateTime();

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment timeDialog = TimePickerFragment.newInstance(crime.getDate());
                timeDialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timeDialog.show(fm, DIALOG_TIME);
            }
        });

        /*solved CheckBox*/
        solvedCheckBox = (CheckBox)view.findViewById(R.id.crime_solved_checkbox);
        solvedCheckBox.setChecked(crime.isSolved());
        // 通过设置 CheckBox 的监听器，在 CheckBox 发生变化时，同时改变 Crime 对象的 isSolved 属性
        solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
            }
        });

        // 在保存数据 content 不为空时，在 EditText 中填入 content
        if (content != null){
            crimeTitle.setText(content);
        }
        // 在 EditText 中数据不为空时，更新 content 为 EditText 中的数据
        if (crimeTitle.getText().toString() != null) {
            content = crimeTitle.getText().toString();
        }
        return view;
    }

    //设置操作栏的视图
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_list_item_context, menu);
    }

    //设置操作栏视图的功能
    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.menu_item_delete_crime:
                Intent intent = new Intent(getActivity(), CrimeListActivity.class);
                // 先将 Crime 对象删除，然后使用 intent 跳转到 CrimeListActivity
                CrimeLab.getInstance(getActivity()).deleteCrime(crime);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CRIME_TITLE,content);
    }

    // 通过 argument 保存 crime 对象的 id
    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public void returnResult(){
        getActivity().setResult(Activity.RESULT_OK, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_DATE:{
                // 从 DatePickerFragment 得到新的日期数据，并更新当前日期
                Date date = (Date)data
                        .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                crime.setDate(date);
                updateDate();break;
            }
            case REQUEST_TIME:{
                Date time = (Date)data
                        .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
                crime.setDate(time);
                updateTime();break;
            }
            default:
        }


    }

    // 更新当前日期
    public void updateDate(){
        DateFormat dateFormat = new DateFormat();
        dateButton.setText(dateFormat.format("E,MM dd,yyyy",crime.getDate()));
    }
    public void updateTime(){
        DateFormat dateFormat = new DateFormat();
        timeButton.setText(dateFormat.format("HH:mm",crime.getDate()));
    }

    // 在 Pause 状态保存 crimes 列表
    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).saveCrimes();
    }
}
