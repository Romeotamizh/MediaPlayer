package com.romeotamizh.MusicPlayer.Helpers;

public class TimeFormat {

    public static String formatTime(int x) {
        Integer m = (x / 1000) / 60;
        Integer s = (x / 1000) % 60;

        if (m == 0 && s == 0)
            return ("00:01");
        else {
            if (m < 10 && s < 10)
                return ("0" + m + ":" + "0" + s);

            else if (m < 10 && s >= 10)
                return ("0" + m + ":" + s);
            else if (m >= 10 && s < 10)
                return (m + ":" + "0" + s);
            else
                return (m + ":" + s);
        }
    }
}
