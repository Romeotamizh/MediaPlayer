package com.romeotamizh.MediaPlayer.Helpers;

import android.app.Application;
import android.content.Context;
import android.view.MenuItem;

import java.util.ArrayList;

public class MyApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return MyApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static ArrayList<SetFragmentOnBackPressedListener> listeners = new ArrayList<>();
    public static ArrayList<SetFragmentOnOptionsMenuClickedListener> listeners1 = new ArrayList<>();

    public static void setOnBackPressed(SetFragmentOnBackPressedListener listener) {
        MyApplication.listeners.add(listener);
    }

    public static void setOnOptionsSelected(SetFragmentOnOptionsMenuClickedListener listener) {
        MyApplication.listeners1.add(listener);
    }

    public static void callBack() {
        for (SetFragmentOnBackPressedListener listener : MyApplication.listeners)
            listener.onBackPressed();
    }

    public static void callBack(MenuItem menuItem) {
        for (SetFragmentOnOptionsMenuClickedListener listener : MyApplication.listeners1)
            listener.onOptionSelected(menuItem);
    }

    public interface SetFragmentOnBackPressedListener {
        void onBackPressed();

    }

    public interface SetFragmentOnOptionsMenuClickedListener {
        void onOptionSelected(MenuItem menuItem);

    }






}
