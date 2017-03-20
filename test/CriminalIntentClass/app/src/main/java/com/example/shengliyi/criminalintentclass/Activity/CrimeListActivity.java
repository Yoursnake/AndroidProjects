package com.example.shengliyi.criminalintentclass.Activity;

import android.support.v4.app.Fragment;

import com.example.shengliyi.criminalintentclass.Fragment.CrimeListFragment;

/**
 * Created by shengliyi on 2017/3/13.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
