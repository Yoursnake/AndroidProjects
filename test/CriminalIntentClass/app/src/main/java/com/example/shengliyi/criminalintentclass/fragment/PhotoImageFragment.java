package com.example.shengliyi.criminalintentclass.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;

import com.example.shengliyi.criminalintentclass.R;
import com.example.shengliyi.criminalintentclass.utils.PictureUtils;

import java.io.File;

/**
 * Created by shengliyi on 2017/4/8.
 */

public class PhotoImageFragment extends DialogFragment {

    private static final String ARG_PHOTO_FILE = "argument_photo_file";
    private ImageView mPhotoImageView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_photo, null);

        File photoFile = (File) getArguments().getSerializable(ARG_PHOTO_FILE);

        mPhotoImageView = (ImageView) view
                .findViewById(R.id.dialog_phone_image_view);

        if (photoFile == null || !photoFile.exists()) {
            mPhotoImageView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils
                    .getScaledBitmap(photoFile.getPath(), getActivity());
            mPhotoImageView.setImageBitmap(bitmap);
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.photo_image_title)
                .setView(view)
                .setPositiveButton(R.string.photo_image_positive_button, null)
                .create();
    }

    public static PhotoImageFragment newInstance(File photoFile) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO_FILE, photoFile);
        PhotoImageFragment fragment = new PhotoImageFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
