package com.romeotamizh.MusicPlayer;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity;
import com.romeotamizh.MusicPlayer.Helpers.SeekbarWithFavourites;
import com.romeotamizh.MusicPlayer.Helpers.TimeFormat;

import static com.romeotamizh.MusicPlayer.Activities.MainActivity.isFromMainActivity;
import static com.romeotamizh.MusicPlayer.Activities.MainActivity.onReturnListener;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.isBackPressed;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayerDuration;


public class SeekBarWithFavouritesHelper {

    private SeekbarWithFavourites seekBar;
    private TextView currentPositionTextView;
    private TextView maxLengthTextView;
    private String context;
    private ImageView playPause;


    public SeekBarWithFavouritesHelper(final SeekbarWithFavourites seekBar, final TextView currentPositionTextView, final TextView maxLengthTextView, final ImageView playPause, final String context) {

        this.seekBar = seekBar;
        this.currentPositionTextView = currentPositionTextView;
        this.maxLengthTextView = maxLengthTextView;
        this.playPause = playPause;
        this.context = context;

    }


    public void seekBarListener() {

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
                    if (mediaPlayer.isPlaying())
                        playPause.setImageResource(R.mipmap.pause);


                }
            });


            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    playPause.setImageResource(R.mipmap.play);

                    seekBar.setProgress(seekBar.getMax());


                }
            });
        }


    }

    public void seekBarOperations() {
        maxLengthTextView.setText(TimeFormat.formatTime(mediaPlayerDuration));
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setmFavouriteBitmap(R.mipmap.red_play);

        Log.d("mpdurations", String.valueOf(mediaPlayerDuration));

        PlayScreenActivity.seekBarMax = seekBar.getMax();


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
                        onReturnListener.callBack();
                        Thread.currentThread().interrupt();
                        return;

                    }
                }


            }
        }).start();


    }

    public void playPausePress() {
        if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {
                playPause.setImageResource(R.mipmap.play);
                mediaPlayer.pause();
            } else {
                playPause.setImageResource(R.mipmap.pause);
                mediaPlayer.start();
            }
        }


    }


}
