package com.romeotamizh.MusicPlayer.Helpers;

public class OnReturnListener {

    private OnReturnListenerInterface listener;

    public void setListener(OnReturnListenerInterface listener) {
        this.listener = listener;

    }

    public void callBack() {
        if (listener != null)
            listener.onReturn();
    }

    public interface OnReturnListenerInterface {
        void onReturn();

    }
}