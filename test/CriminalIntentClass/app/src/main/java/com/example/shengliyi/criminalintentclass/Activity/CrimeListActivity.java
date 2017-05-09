package com.example.shengliyi.criminalintentclass.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.shengliyi.criminalintentclass.entity.Crime;
import com.example.shengliyi.criminalintentclass.entity.CrimeLab;
import com.example.shengliyi.criminalintentclass.fragment.CrimeFragment;
import com.example.shengliyi.criminalintentclass.fragment.CrimeListFragment;
import com.example.shengliyi.criminalintentclass.R;

/**
 * Created by shengliyi on 2017/3/13.
 */

public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        updateList();
    }

    private void updateList() {
        CrimeListFragment listFragment = (CrimeListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    public void onCrimeDeleted(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) != null) {
            CrimeLab.getInstance(this).removeCrime(crime);
            updateList();
        }
    }
}
