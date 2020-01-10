package com.romeotamizh.MusicPlayer.Helpers;

import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.romeotamizh.MusicPlayer.R;

import static com.romeotamizh.MusicPlayer.Activities.MainActivity.isSlideCollapsed;
import static com.romeotamizh.MusicPlayer.Activities.MainActivity.isSlideExpanded;
import static com.romeotamizh.MusicPlayer.Activities.MainActivity.seekBarMax;
import static com.romeotamizh.MusicPlayer.Helpers.Context.CONTEXT;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayerDuration;


public class SeekBarWithFavouritesHelper implements SeekBar.OnSeekBarChangeListener {


    private SeekBarWithFavourites seekBar;
    private TextView currentPositionTextView;
    private TextView maxLengthTextView;
    private CONTEXT context;
    private ImageView playPause;


    public SeekBarWithFavouritesHelper(final SeekBarWithFavourites seekBar, final TextView currentPositionTextView, final TextView maxLengthTextView, final ImageView playPause, final CONTEXT context) {

        this.seekBar = seekBar;
        this.currentPositionTextView = currentPositionTextView;
        this.maxLengthTextView = maxLengthTextView;
        this.playPause = playPause;
        this.context = context;

    }




    public void seekBarOperations() {
        seekBar.setOnSeekBarChangeListener(this);
        maxLengthTextView.setText(TimeFormat.formatTime(mediaPlayerDuration));
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setFavouriteBitmap(R.mipmap.red_play);

        Log.d("mpdurations", mediaPlayerDuration + context.name());
        seekBarMax = seekBar.getMax();
        if (mediaPlayer.isPlaying())
            playPause.setImageResource(R.mipmap.pause);
        else
            playPause.setImageResource(R.mipmap.play);



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

                    if (context == CONTEXT.MAIN && isSlideExpanded) {
                        Thread.currentThread().interrupt();
                        return;

                    }
                    if (context == CONTEXT.PLAY && isSlideCollapsed) {
                        Thread.currentThread().interrupt();
                        return;

                    }
                }


            }
        }).start();


    }


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
        SeekBarWithFavourites.callBack(seekBar.getProgress());
        mediaPlayer.seekTo(seekBar.getProgress());
        mediaPlayer.start();
        if (mediaPlayer.isPlaying())
            playPause.setImageResource(R.mipmap.pause);

    }
}
