package com.example.shengliyi.criminalintentclass.Activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.shengliyi.criminalintentclass.Entity.Crime;
import com.example.shengliyi.criminalintentclass.Entity.CrimeLab;
import com.example.shengliyi.criminalintentclass.Fragment.CrimeFragment;
import com.example.shengliyi.criminalintentclass.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by shengliyi on 2017/3/19.
 */

public class CrimePagerActivity extends FragmentActivity {

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
}
