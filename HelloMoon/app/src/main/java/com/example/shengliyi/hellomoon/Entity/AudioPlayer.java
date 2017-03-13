package com.example.shengliyi.hellomoon.Entity;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.shengliyi.hellomoon.Fragment.HelloMoonFragment;
import com.example.shengliyi.hellomoon.R;

/**
 * Created by shengliyi on 2017/2/5.
 */

public class AudioPlayer {

    private MediaPlayer player;

    public boolean isOver(){
        if (player == null) {
            return true;
        }else {
            return false;
        }
    }

    public void stop(){
        if (!isOver()) {
            player.release();
            player = null;       //停止时销毁player对象
            //停止的时候，如果此时是待暂停状态，则把按钮的"pause"改成"play"，自增count表示此时为待播放状态
            if (HelloMoonFragment.count%2 == 1){
                HelloMoonFragment.count++;
                HelloMoonFragment.playButton.setText(R.string.hellomoon_play);
            }

        }
    }

    public void play(Context context){

        //如果音频已经放完了，则player重新获取对象，否则player对象不变
        if (isOver()) {
            player = MediaPlayer.create(context, R.raw.one_small_step);
        }

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            //音频一旦放完的时候，处于待暂停状态，要将其重新变为待播放状态，并销毁对象
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
                HelloMoonFragment.count++;
                HelloMoonFragment.playButton.setText(R.string.hellomoon_play);
            }
        });

        player.start();
    }

    public void pause(){
        player.pause();
    }

}
