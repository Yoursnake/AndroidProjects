package com.example.shengliyi.photogallery.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by shengliyi on 2017/5/26.
 */

public class PollService extends IntentService {

    private static final String TAG = "PollService";

    private static Context mContext;


    public PollService() {
        super(TAG);
    }

    public static Intent newIntent(Context context) {
        mContext = context;
        return new Intent(context, PollService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "Received an intent:" + intent);
    }
}
