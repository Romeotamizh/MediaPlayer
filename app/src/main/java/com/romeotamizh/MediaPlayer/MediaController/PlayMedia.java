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

    public static boolean mpPrepared = false;


    public static void setPlayMusicListener(OnPlayMediaListener listener) {
        PlayMedia.listeners.add(listener);

    }

    public static void callBack(int position, Context.MEDIATYPE mediaType) {
        for (OnPlayMediaListener listenerItem : listeners)
            listenerItem.playMedia(position, mediaType);

    }

    private static ArrayList<OnMediaCompletedListener> listeners1 = new ArrayList<>();

    public interface OnPlayMediaListener {
        void playMedia(int position, Context.MEDIATYPE mediaType);
    }

    public static void playMedia(final Uri uri, final Context.MEDIATYPE mediaType) {
        if (mediaPlayer != null) {
            mpPrepared = false;
                    mediaPlayer.stop();
                    mediaPlayer.reset();

            try {
                mediaPlayer.setDataSource(getContext(), uri);
                if (!mpPrepared)
                    try {
                        mediaPlayer.prepare();
                    } catch (IllegalStateException e) {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(getContext(), uri);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        return;
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }




                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mpPrepared = true;

                        if (mediaType == Context.MEDIATYPE.VIDEO) {
                            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);

                        }
                        mediaPlayer.setVolume(1.0f, 1.0f);
                        mediaPlayerDuration = mediaPlayer.getDuration();
                        mediaPlayer.start();

                    }
                });



        }

    }

    public static void callBack(Context.MEDIATYPE mediaType) {
        for (OnMediaCompletedListener listener : listeners1)
            listener.onCompleted(mediaType);
    }

    private static void setOnMediaCompletedListener(OnMediaCompletedListener listener) {
        listeners1.add(listener);

    }

    public interface OnMediaCompletedListener {
        void onCompleted(Context.MEDIATYPE mediaType);
    }


}
