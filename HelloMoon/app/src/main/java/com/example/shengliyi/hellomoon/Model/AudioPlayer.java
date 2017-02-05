package com.example.shengliyi.hellomoon.Model;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Button;

import com.example.shengliyi.hellomoon.Activity.HelloMoonActivity;
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
            player = null;
            if (HelloMoonFragment.count%2 == 1){
                HelloMoonFragment.count++;
                HelloMoonFragment.playButton.setText("Play ");
            }

        }
    }

    public void play(Context context){

        if (isOver()) {
            player = MediaPlayer.create(context, R.raw.one_small_step);
        }

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
                HelloMoonFragment.count++;
                HelloMoonFragment.playButton.setText("Play ");
            }
        });

        player.start();
    }

    public void pause(){
        player.pause();
    }

}
