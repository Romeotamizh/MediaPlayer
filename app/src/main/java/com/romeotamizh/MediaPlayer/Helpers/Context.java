package com.romeotamizh.MediaPlayer.Helpers;

import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

public final class Context {

    public enum CONTEXT {MAIN, PLAY, NOTIFICATION, VIDEO_SCREEN}

    public enum MEDIATYPE {AUDIO, VIDEO}

    public enum REPEATMODE {SINGLE, ALBUM, NONE, FULL, FAVOURITES, PLAYED_ALREADY}

    public enum SIGNAL {DEFAULT}

    public enum GROUPBY {NOTHING, ALBUM, FOLDER}

    private static ArrayList<SetFragmentOnBackPressedListener> listeners = new ArrayList<>();
    private static ArrayList<SetFragmentOnOptionsMenuClickedListener> listeners1 = new ArrayList<>();

    public static void setOnBackPressed(SetFragmentOnBackPressedListener listener) {
        listeners.add(listener);
    }

    public static void setOnOptionsSelected(SetFragmentOnOptionsMenuClickedListener listener) {
        listeners1.add(listener);
    }

    public static void callBack() {
        for (SetFragmentOnBackPressedListener listener : listeners)
            listener.onBackPressed();
    }

    public static void callBack(MenuItem menuItem) {
        for (SetFragmentOnOptionsMenuClickedListener listener : listeners1)
            listener.onOptionSelected(menuItem);
        Log.d(String.valueOf(listeners1.size()), listeners1.toString());
    }

    public interface SetFragmentOnBackPressedListener {
        void onBackPressed();

    }

    public interface SetFragmentOnOptionsMenuClickedListener {
        void onOptionSelected(MenuItem menuItem);

    }


}
