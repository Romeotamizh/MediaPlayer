package com.romeotamizh.MediaPlayer.MediaController;

import android.media.MediaPlayer;
import android.net.Uri;

import com.romeotamizh.MediaPlayer.Helpers.Context;

import java.io.IOException;
import java.util.ArrayList;

import static com.romeotamizh.MediaPlayer.Helpers.MyApplication.getContext;

public class PlayMedia {
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public static int mediaPlayerDuration = 0;
    private static ArrayList<OnPlayMediaListener> listeners = new ArrayList<>();
    private static int videoRatio;

    public static void playMedia(final Uri uri, final Context.MEDIATYPE mediaType) {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }

                mediaPlayer.reset();
                mediaPlayer.setDataSource(getContext(), uri);
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        if (mediaType == Context.MEDIATYPE.VIDEO) {
                            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                        }
                        mediaPlayer.setVolume(1.0f, 1.0f);
                        mediaPlayerDuration = mediaPlayer.getDuration();
                        mediaPlayer.start();

                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static void setPlayMusicListener(OnPlayMediaListener listener) {
        PlayMedia.listeners.add(listener);

    }

    public static void callBack(int position, Context.MEDIATYPE mediaType) {
        for (OnPlayMediaListener listenerItem : listeners)
            listenerItem.playMedia(position, mediaType);

    }

    public interface OnPlayMediaListener {
        void playMedia(int position, Context.MEDIATYPE mediaType);
    }
}
