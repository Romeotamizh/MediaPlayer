package com.romeotamizh.MediaPlayer.Helpers;

import android.app.Application;
import android.content.Context;

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

    public static SetFragmentOnBackPressedListener listener;

    public static void setOnBackPressed(SetFragmentOnBackPressedListener listener) {
        MyApplication.listener = listener;
    }

    public static void callBack() {
        MyApplication.listener.onBackPressed();
    }

    public interface SetFragmentOnBackPressedListener {
        void onBackPressed();

    }






}
