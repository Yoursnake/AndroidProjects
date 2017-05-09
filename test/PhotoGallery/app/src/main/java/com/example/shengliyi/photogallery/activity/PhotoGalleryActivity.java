package com.example.shengliyi.photogallery.activity;

import android.support.v4.app.Fragment;

import com.example.shengliyi.photogallery.fragment.PhotoGalleryFragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
