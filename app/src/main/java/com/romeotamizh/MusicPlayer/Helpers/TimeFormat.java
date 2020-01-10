package com.romeotamizh.MusicPlayer.Helpers;

public class TimeFormat {

    public static String formatTime(int x) {
        int sec = (x / 1000) % 60;
        int min = (x / 60000) % 60;
        int hr = (x / 3600000);

        String s = sec < 10 ? "0" + sec : String.valueOf(sec);
        String m = min < 10 ? "0" + min + ":" : min + ":";
        String h = hr + ":";

        return (min == 0 && sec == 0 ? "00:01" : hr == 0 ? m + s : h + m + s);


    }
}
