package com.romeotamizh.MusicPlayer;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.romeotamizh.MusicPlayer.Activities.MainActivity;
import com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity;
import com.romeotamizh.MusicPlayer.Helpers.SeekbarWithFavourites;
import com.romeotamizh.MusicPlayer.Helpers.TimeFormat;

import static com.romeotamizh.MusicPlayer.Activities.MainActivity.isFromMainActivity;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.isBackPressed;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayerDuration;


public class SeekBarWithFavouritesHelper {


    public static void seekBarListener(final SeekbarWithFavourites seekBar, final TextView currentPositionTextView, final String context) {

        if ((!isBackPressed && context.equals("play")) || (isFromMainActivity && context.equals("main"))) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                    currentPositionTextView.setText(TimeFormat.formatTime(mediaPlayer.getCurrentPosition()));


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.pause();

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    currentPositionTextView.setText(TimeFormat.formatTime(seekBar.getProgress()));

                    mediaPlayer.seekTo(seekBar.getProgress());
                    mediaPlayer.start();
                    //seekBarOperations( seekBar , maxLengthTextView , context);
                    if (mediaPlayer.isPlaying())
                        if (context.equals("play"))
                            PlayScreenActivity.playPause.setImageResource(R.mipmap.pause);
                        else if (context.equals("main"))
                            MainActivity.playPause.setImageResource(R.mipmap.pause);


                }
            });


            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    if (context.equals("play"))
                        PlayScreenActivity.playPause.setImageResource(R.mipmap.play);
                    else if (context.equals("main"))
                        MainActivity.playPause.setImageResource(R.mipmap.play);

                    seekBar.setProgress(seekBar.getMax());


                }
            });
        }


    }

    public static void seekBarOperations(final SeekbarWithFavourites seekBar, final TextView maxLengthTextView, final String context) {
        maxLengthTextView.setText(TimeFormat.formatTime(mediaPlayerDuration));
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setmFavouriteBitmap(R.mipmap.red_play);

        Log.d("mpdurations", String.valueOf(mediaPlayerDuration));

        PlayScreenActivity.seekBarMax = seekBar.getMax();
        /*if (mediaPlayerDuration < 1000)
            seekBar.setProgress(seekBar.getMax());
        else {*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                int currentPosition = 0;
                while (true) {

                    if (mediaPlayer.isPlaying()) {
                        currentPosition = mediaPlayer.getCurrentPosition();
                        currentPosition++;
                        seekBar.setProgress(currentPosition);
                    }

                    if (currentPosition >= mediaPlayerDuration) {
                        currentPosition = 0;

                    }

                    if (!isFromMainActivity && context.equals("main")) {
                        Thread.currentThread().interrupt();
                        return;

                    }
                    if (isBackPressed && context.equals("play")) {
                        MainActivity.onReturn();
                        Thread.currentThread().interrupt();
                        return;

                    }
                }


            }
        }).start();
        //}


    }

    public static void playPausePress(String context) {
        if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {
                if (context.equals("play"))
                    PlayScreenActivity.playPause.setImageResource(R.mipmap.play);
                else if (context.equals("main"))
                    MainActivity.playPause.setImageResource(R.mipmap.play);
                mediaPlayer.pause();
            } else {
                if (context.equals("play"))
                    PlayScreenActivity.playPause.setImageResource(R.mipmap.pause);
                else if (context.equals("main"))
                    MainActivity.playPause.setImageResource(R.mipmap.pause);
                mediaPlayer.start();
            }
        }


    }


}
