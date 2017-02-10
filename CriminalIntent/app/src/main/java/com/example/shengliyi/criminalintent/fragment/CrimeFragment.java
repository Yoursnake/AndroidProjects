package com.example.shengliyi.criminalintent.fragment;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.shengliyi.criminalintent.R;
import com.example.shengliyi.criminalintent.activity.CrimePagerActivity;
import com.example.shengliyi.criminalintent.model.Crime;
import com.example.shengliyi.criminalintent.model.CrimeLab;

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

        setHasOptionsMenu(true);

        UUID crimeId = (UUID)getArguments()
                .getSerializable(EXTRA_CRIME_ID);
        crime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);


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
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        /*crime Title*/
        crimeTitle = (EditText)view.findViewById(R.id.crime_title);
        crimeTitle.setText(crime.getTitle());
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
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dateDialog = DatePickerFragment.newInstance(crime.getDate());
                dateDialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);   //建立CrimeFragment和DatePickerFragment之间的联系
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
        solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
            }
        });


        if (content != null){
            crimeTitle.setText(content);
        }
        if (crimeTitle.getText().toString() != null) {
            content = crimeTitle.getText().toString();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CRIME_TITLE,content);
    }

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

    public void updateDate(){
        DateFormat dateFormat = new DateFormat();
        dateButton.setText(dateFormat.format("E,MM dd,yyyy",crime.getDate()));
    }
    public void updateTime(){
        DateFormat dateFormat = new DateFormat();
        timeButton.setText(dateFormat.format("HH:mm",crime.getDate()));
    }

    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).saveCrimes();
    }
}
