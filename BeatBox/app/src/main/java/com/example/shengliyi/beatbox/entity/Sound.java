package com.example.shengliyi.beatbox.entity;

/**
 * Created by shengliyi on 16/04/2017.
 */

public class Sound {
    private String mAssetPath;
    private String mName;
    private Integer mSoundId;

    public Sound(String assetPath) {
        super();
        mAssetPath = assetPath;
        String[] components = assetPath.split("/");
        String filename = components[components.length - 1];
        mName = filename.replace(".wav", "");
    }

    public String getAssetPath() {
        return mAssetPath;
    }

    public String getName() {
        return mName;
    }

    public Integer getSoundId() {
        return mSoundId;
    }

    public void setSoundId(Integer soundId) {
        mSoundId = soundId;
    }
}
