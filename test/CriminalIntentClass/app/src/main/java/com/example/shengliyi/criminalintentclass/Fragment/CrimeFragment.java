package com.example.shengliyi.criminalintentclass.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.shengliyi.criminalintentclass.Entity.Crime;
import com.example.shengliyi.criminalintentclass.Entity.CrimeLab;
import com.example.shengliyi.criminalintentclass.R;

import java.util.Date;
import java.util.UUID;

/**
 * Created by shengliyi on 2017/3/13.
 */

public class CrimeFragment extends Fragment {

    private static final String ARGUMENTS_CRIME_ID = "com.example.shengliyi.criminalintentclass.arguments_crime_id";
    private static final String DIALOG_DATE = "dialog_date";
    private static final String DIALOG_TIME = "dialog_time";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private Crime mCrime;
    private EditText mCrimeTitle;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckedBox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID crimeId = (UUID) getArguments().getSerializable(ARGUMENTS_CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mCrimeTitle = (EditText) view.findViewById(R.id.crime_title_edit_text);
        mDateButton = (Button) view.findViewById(R.id.crime_date_button);
        mTimeButton = (Button) view.findViewById(R.id.crime_time_button);
        mSolvedCheckedBox = (CheckBox) view.findViewById(R.id.crime_solved_check_box);

        mCrimeTitle.setText(mCrime.getTitle());
        mCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                datePickerFragment.show(fm, DIALOG_DATE);
            }
        });

        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getDate());
                timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timePickerFragment.show(fm, DIALOG_TIME);
            }
        });

        mSolvedCheckedBox.setChecked(mCrime.isSolved());
        mSolvedCheckedBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCrime.setSolved(mSolvedCheckedBox.isChecked());
            }
        });

        return view;
    }

    private void updateDate() {
        Date date = mCrime.getDate();
        mDateButton.setText(DateFormat.format("yyyy MM dd E", date));
    }

    private void updateTime() {
        Date date = mCrime.getDate();
        mTimeButton.setText(DateFormat.format("hh:mm a", date));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DATE:
                if (resultCode == Activity.RESULT_OK) {
                    Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DIALOG_DATE);
                    mCrime.setDate(date);
                    updateDate();
                }
            case REQUEST_TIME:
                if (resultCode == Activity.RESULT_OK) {
                    Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_DIALOG_TIME);
                    mCrime.setDate(date);
                    updateTime();
                }
        }
    }

    public static CrimeFragment newInstance(UUID id) {
        
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENTS_CRIME_ID, id);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
