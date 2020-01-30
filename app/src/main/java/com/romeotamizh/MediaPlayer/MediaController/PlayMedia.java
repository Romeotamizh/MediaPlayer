package com.romeotamizh.MediaPlayer.MediaController;

import android.media.MediaPlayer;
import android.net.Uri;

import com.romeotamizh.MediaPlayer.Helpers.Context;

import java.util.ArrayList;

import static com.romeotamizh.MediaPlayer.Helpers.MyApplication.getContext;

public class PlayMedia {
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public static int mediaPlayerDuration = 0;
    private static ArrayList<OnPlayMediaListener> onPlayMediaListeners = new ArrayList<>();
    private static int videoRatio;

    public static boolean mpPrepared = false;
    private static ArrayList<OnAddToRecentListListener> onAddToRecentListListeners = new ArrayList<>();
    private static ArrayList<OnMediaCompletedListener> onMediaCompletedListeners = new ArrayList<>();

    public static void setPlayMediaListener(OnPlayMediaListener listener) {
        PlayMedia.onPlayMediaListeners.add(listener);

    }

    public static void callBack(int id, Context.MEDIATYPE mediaType) {
        for (OnPlayMediaListener onPlayMediaListener : onPlayMediaListeners)
            onPlayMediaListener.playMedia(id, mediaType);

    }

    public static void setOnAddToRecentListListener(OnAddToRecentListListener onAddToRecentListListener) {
        onAddToRecentListListeners.add(onAddToRecentListListener);
    }

    public static void callBack(int id, Uri uri, CharSequence title, Context.MEDIATYPE mediaType) {
        for (OnAddToRecentListListener listener : onAddToRecentListListeners)
            listener.addToRecentList(id, uri, title, mediaType);
    }

    public static void playMedia(final Uri uri, final Context.MEDIATYPE mediaType) {
        if (mediaPlayer != null) {
            mpPrepared = false;
                    mediaPlayer.stop();
                    mediaPlayer.reset();

            mediaPlayer = MediaPlayer.create(getContext(), uri);

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mpPrepared = true;

                        mediaPlayer.setVolume(1.0f, 1.0f);
                        mediaPlayerDuration = mediaPlayer.getDuration();
                        mediaPlayer.start();

                    }
                });



        }

    }

    public static void callBack(Context.MEDIATYPE mediaType) {
        for (OnMediaCompletedListener onMediaCompletedListener : onMediaCompletedListeners)
            onMediaCompletedListener.onCompleted(mediaType);
    }

    private static void setOnMediaCompletedListener(OnMediaCompletedListener listener) {
        onMediaCompletedListeners.add(listener);

    }

    public interface OnAddToRecentListListener {
        void addToRecentList(int id, Uri uri, CharSequence title, Context.MEDIATYPE mediaType);
    }

    public interface OnPlayMediaListener {
        void playMedia(int id, Context.MEDIATYPE mediaType);
    }

    public interface OnMediaCompletedListener {
        void onCompleted(Context.MEDIATYPE mediaType);
    }


}
