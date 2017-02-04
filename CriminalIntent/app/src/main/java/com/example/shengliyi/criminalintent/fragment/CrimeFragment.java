package com.example.shengliyi.criminalintent.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.shengliyi.criminalintent.R;
import com.example.shengliyi.criminalintent.model.Crime;
import com.example.shengliyi.criminalintent.model.CrimeLab;

import java.util.UUID;

/**
 * Created by shengliyi on 2017/1/18.
 */

public class CrimeFragment extends Fragment {

    private Crime crime;
    private String content;

    private EditText crimeTitle;
    private Button dateButton;
    private CheckBox solvedCheckBox;

    public static final String EXTRA_CRIME_ID =
            "com.example.shengliyi.criminalintent.crime_id";

    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UUID crimeId = (UUID)getActivity().getIntent()
//                .getSerializableExtra(EXTRA_CRIME_ID);

        UUID crimeId = (UUID)getArguments()
                .getSerializable(EXTRA_CRIME_ID);
        crime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);


        if(savedInstanceState != null){
            content = savedInstanceState.getString("Crime Title");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime,container,false);

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
        DateFormat dateFormat = new DateFormat();
        dateButton.setText(dateFormat.format("E,MM dd,yyyy",crime.getDate()));
//        dateButton.setEnabled(false);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(crime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
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
        outState.putString("Crime Title",content);
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
}
