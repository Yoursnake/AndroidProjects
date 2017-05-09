package com.example.shengliyi.criminalintentclass.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.shengliyi.criminalintentclass.entity.Crime;
import com.example.shengliyi.criminalintentclass.entity.CrimeLab;
import com.example.shengliyi.criminalintentclass.fragment.CrimeFragment;
import com.example.shengliyi.criminalintentclass.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by shengliyi on 2017/3/19.
 */

public class CrimePagerActivity extends AppCompatActivity
        implements CrimeFragment.Callbacks {

    private static final String EXTRA_CRIME_ID = "com.example.shengliyi.criminalintentclass.Activity.CrimePagerActivity.extra_crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_view_pager);
        mCrimes = CrimeLab.getInstance(this).getCrimes();
        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId))
                mViewPager.setCurrentItem(i);
        }
    }
    
    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    public void onCrimeUpdated(Crime crime) {

    }

    @Override
    public void onCrimeDeleted(Crime crime) {
        CrimeLab.getInstance(this).removeCrime(crime);
        finish();
    }
}
