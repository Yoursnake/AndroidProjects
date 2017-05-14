package com.example.shengliyi.photogallery.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shengliyi.photogallery.R;

/**
 * Created by shengliyi on 2017/5/14.
 */

public class PictureDialog extends DialogFragment {

    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_picture, container, false);
        mImageView = (ImageView) view.findViewById(R.id.dialog_picture_image_view);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        mImageView.setImageBitmap(bitmap);
        return view;
    }
}
