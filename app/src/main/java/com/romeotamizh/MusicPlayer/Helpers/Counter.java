package com.romeotamizh.MusicPlayer.Helpers;

public class Counter {

    int count;

    public synchronized void incrementCount() {
        count++;
    }
}
