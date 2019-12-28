package com.romeotamizh.MusicPlayer;

public class Counter {

    int count;

    public synchronized void incrementCount() {
        count++;
    }
}
