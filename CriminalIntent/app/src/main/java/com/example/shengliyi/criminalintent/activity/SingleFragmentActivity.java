package com.example.shengliyi.criminalintent.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.shengliyi.criminalintent.R;
import com.example.shengliyi.criminalintent.fragment.CrimeFragment;

/**
 * Created by shengliyi on 2017/1/20.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.crime_title_layout);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .replace(R.id.crime_title_layout,fragment)
                    .commit();
        }

    }
}
