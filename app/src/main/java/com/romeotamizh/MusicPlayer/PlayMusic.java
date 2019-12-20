package com.romeotamizh.MusicPlayer;

import android.media.MediaPlayer;

import java.io.IOException;

import static com.romeotamizh.MusicPlayer.MainActivity.mediaPlayer;
import static com.romeotamizh.MusicPlayer.PlayScreenActivity.mediaPlayBackListener;

public class PlayMusic {

    public static void playMusic(final String mData, final String mTitle) {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setVolume(1.0f, 1.0f);
            mediaPlayer.setDataSource(mData);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    mediaPlayBackListener();

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
