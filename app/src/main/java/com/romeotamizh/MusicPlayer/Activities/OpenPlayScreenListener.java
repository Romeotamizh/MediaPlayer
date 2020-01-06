package com.romeotamizh.MusicPlayer.Activities;

public class OpenPlayScreenListener {
    private OpenPlayScreenListenerInterface listener;

    public void setListener(OpenPlayScreenListenerInterface listener) {
        this.listener = listener;

    }

    public void callBack() {
        if (listener != null)
            listener.openPlayScreen();
    }

    public interface OpenPlayScreenListenerInterface {
        void openPlayScreen();
    }
}
