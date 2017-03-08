package com.example.shengliyi.criminalintent.Activity;


import android.support.v4.app.Fragment;

import com.example.shengliyi.criminalintent.Fragment.CrimeFragment;
import com.example.shengliyi.criminalintent.Entity.Crime;

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
