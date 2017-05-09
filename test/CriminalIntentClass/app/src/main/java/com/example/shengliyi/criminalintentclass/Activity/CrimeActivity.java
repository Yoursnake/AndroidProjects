package com.example.shengliyi.criminalintentclass.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.shengliyi.criminalintentclass.fragment.CrimeFragment;

import java.util.UUID;

/**
 * Created by shengliyi on 2017/3/13.
 */

public class CrimeActivity extends SingleFragmentActivity {

    public static final String EXTRA_CRIME_ID = "com.example.shengliyi.criminalintentclass.extra_crime_id";

    @Override
    protected Fragment createFragment() {

        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }
}
