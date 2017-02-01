package com.example.shengliyi.criminalintent.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.shengliyi.criminalintent.R;
import com.example.shengliyi.criminalintent.fragment.CrimeFragment;
import com.example.shengliyi.criminalintent.model.Crime;
import com.example.shengliyi.criminalintent.model.CrimeLab;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by shengliyi on 2017/2/1.
 */

public class CrimePagerActivity extends FragmentActivity{

    ViewPager viewPager;
    ArrayList<Crime> crimes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);    //在res/values/ids.xml中注册了id
        setContentView(viewPager);

        crimes = CrimeLab.getInstance(this).getCrimes();
        FragmentManager fm = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = crimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return crimes.size();
            }
        });
    }
}
