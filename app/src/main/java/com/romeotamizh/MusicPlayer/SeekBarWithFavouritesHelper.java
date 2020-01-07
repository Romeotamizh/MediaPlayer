package com.romeotamizh.MusicPlayer;

import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.romeotamizh.MusicPlayer.Helpers.SeekbarWithFavourites;
import com.romeotamizh.MusicPlayer.Helpers.TimeFormat;

import static com.romeotamizh.MusicPlayer.Activities.MainActivity.isSlideCollapsed;
import static com.romeotamizh.MusicPlayer.Activities.MainActivity.isSlideExpanded;
import static com.romeotamizh.MusicPlayer.Activities.MainActivity.seekBarMax;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayerDuration;


public class SeekBarWithFavouritesHelper implements SeekBar.OnSeekBarChangeListener {

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

       /* if ((!isSlideCollapsed && context.equals("play")) || (!isSlideExpanded && context.equals("main"))) {
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


        }
*/

    }

    public void seekBarOperations() {
        seekBar.setOnSeekBarChangeListener(this);
        maxLengthTextView.setText(TimeFormat.formatTime(mediaPlayerDuration));
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setmFavouriteBitmap(R.mipmap.red_play);

        Log.d("mpdurations", mediaPlayerDuration + context);

        seekBarMax = seekBar.getMax();
        if (mediaPlayer.isPlaying())
            playPause.setImageResource(R.mipmap.pause);



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

                 /*   if(!mediaPlayer.isPlaying())
                        playPause.setImageResource(R.mipmap.play);
*/

                    if (context.equals("main") && isSlideExpanded) {
                        Thread.currentThread().interrupt();
                        return;

                    }
                    if (context.equals("play") && isSlideCollapsed) {
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
        mediaPlayer.seekTo(seekBar.getProgress());
        mediaPlayer.start();
        if (mediaPlayer.isPlaying())
            playPause.setImageResource(R.mipmap.pause);

    }
}
