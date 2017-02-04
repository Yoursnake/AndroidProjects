package com.example.shengliyi.criminalintent.activity;


import android.support.v4.app.Fragment;

import com.example.shengliyi.criminalintent.fragment.CrimeFragment;
import com.example.shengliyi.criminalintent.model.Crime;

import java.util.ArrayList;
import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    ArrayList<Crime> crimes;

    @Override
    protected Fragment createFragment() {
//        return new CrimeFragment();


        UUID crimeId = (UUID)getIntent()
                .getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);

        return CrimeFragment.newInstance(crimeId);
    }
}
