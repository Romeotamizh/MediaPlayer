package com.romeotamizh.MusicPlayer;

import android.media.MediaPlayer;

import java.io.IOException;

public class PlayMusic {
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public static int mediaPlayerDuration = 0;
    private static PlayMusicListener listener;

    public static void playMusic(final String mData, final String mTitle) {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
                mediaPlayer.reset();
                mediaPlayer.setVolume(1.0f, 1.0f);
                mediaPlayer.setDataSource(mData);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayerDuration = mediaPlayer.getDuration();
                        mediaPlayer.start();

                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static void setPlayMusicListener(PlayMusicListener listener) {
        PlayMusic.listener = listener;

    }

    public static void callBack(int position) {
        if (listener != null)
            listener.playMusic(position);
    }

    public interface PlayMusicListener {
        void playMusic(int position);
    }
}
