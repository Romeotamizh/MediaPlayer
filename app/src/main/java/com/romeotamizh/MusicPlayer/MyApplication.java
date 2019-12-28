package com.romeotamizh.MusicPlayer;

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
}
