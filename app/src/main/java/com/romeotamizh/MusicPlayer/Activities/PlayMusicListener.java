package com.romeotamizh.MusicPlayer.Activities;

public class PlayMusicListener {
    private PlayMusicListenerInterface listener;

    public void setListener(PlayMusicListenerInterface listener) {
        this.listener = listener;

    }

    public void callBack() {
        if (listener != null)
            listener.playMusic();
    }

    public interface PlayMusicListenerInterface {
        void playMusic();
    }
}
