package com.example.shengliyi.beatbox.activity;

import android.support.v4.app.Fragment;

import com.example.shengliyi.beatbox.fragment.BeatBoxFragment;

public class BeatBoxActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return BeatBoxFragment.newInstance();
    }
}
