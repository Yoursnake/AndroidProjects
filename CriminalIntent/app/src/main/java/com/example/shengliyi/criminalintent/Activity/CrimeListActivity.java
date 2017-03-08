package com.example.shengliyi.criminalintent.Activity;


import android.support.v4.app.Fragment;

import com.example.shengliyi.criminalintent.Fragment.CrimeListFragment;

/**
 * Created by shengliyi on 2017/1/20.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
