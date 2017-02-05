package com.example.shengliyi.hellomoon.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import com.example.shengliyi.hellomoon.Model.AudioPlayer;
import com.example.shengliyi.hellomoon.R;

/**
 * Created by shengliyi on 2017/2/5.
 */

public class HelloMoonFragment extends Fragment {

    private AudioPlayer player = new AudioPlayer();
    public static Button playButton;
    private Button stopButton;

    public static int count = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hello_moon, null);

        playButton = (Button)view.findViewById(R.id.hellomoon_playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count%2 == 0) {
                    player.play(getActivity());
                    count++;
                    playButton.setText("Pause");
                }else{
                    player.pause();
                    count++;
                    playButton.setText("Play ");
                }
//                VideoView videoView = new VideoView(getActivity());
//                Uri resourceUri = Uri.parse("android.resource://" +
//                        "com.example.shengliyi.hellomoon/raw/apollo_17_stroll");
//                videoView.setVideoURI(resourceUri);
//                videoView.start();
            }
        });


        stopButton = (Button)view.findViewById(R.id.hellomoon_stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }
}
