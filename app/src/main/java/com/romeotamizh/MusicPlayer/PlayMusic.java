package com.romeotamizh.MusicPlayer;

import android.media.MediaPlayer;

import java.io.IOException;

import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.mediaPlayBackListener;

public class PlayMusic {
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public static int mediaPlayerDuration = 0;

    public static void playMusic(final String mData, final String mTitle) {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setVolume(1.0f, 1.0f);
                mediaPlayer.setDataSource(mData);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayerDuration = mediaPlayer.getDuration();
                        mediaPlayer.start();
                        mediaPlayBackListener();


                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
