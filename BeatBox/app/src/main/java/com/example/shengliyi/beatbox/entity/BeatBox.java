package com.example.shengliyi.beatbox.entity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shengliyi on 16/04/2017.
 */

public class BeatBox {

    private final String TAG = "BeatBox";

    private final String SOUND_FOLDER = "sample_sounds";
    private final int MAX_SOUNDS = 3;

    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BeatBox(Context context) {
        super();
        mAssets = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);

//        mSoundPool = new SoundPool.Builder()
//                .setMaxStreams(MAX_SOUNDS)
//                .setAudioAttributes(new AudioAttributes.Builder()
//                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
//                        .build())
//                .build();
        loadSounds();
    }

    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = mAssets.list(SOUND_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException e) {
            Log.e(TAG, "Could not list assets", e);
            return ;
        }
        for (String soundName : soundNames) {
            try {
                String assertPath = SOUND_FOLDER + "/" + soundName;
                Sound sound = new Sound(assertPath);
                load(sound);
                mSounds.add(sound);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load " + soundName, ioe);
            }
        }
    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f); // id, 左声道，右声道，优先级
    }

    public void release() {
        mSoundPool.release();
    }

    public List<Sound> getSounds() {
        return mSounds;
    }

}
