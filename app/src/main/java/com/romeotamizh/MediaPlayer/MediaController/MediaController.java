package com.romeotamizh.MediaPlayer.MediaController;

import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.romeotamizh.MediaPlayer.Helpers.CustomSeekBar;
import com.romeotamizh.MediaPlayer.Helpers.FormatData;
import com.romeotamizh.MediaPlayer.R;

import static com.romeotamizh.MediaPlayer.Activities_Fragments.MainActivity.isSlideCollapsed;
import static com.romeotamizh.MediaPlayer.Activities_Fragments.MainActivity.isSlideExpanded;
import static com.romeotamizh.MediaPlayer.Activities_Fragments.MainActivity.isSongChanged;
import static com.romeotamizh.MediaPlayer.Activities_Fragments.MainActivity.seekBarMax;
import static com.romeotamizh.MediaPlayer.Helpers.Context.CONTEXT;
import static com.romeotamizh.MediaPlayer.MediaController.PlayMedia.mediaPlayer;
import static com.romeotamizh.MediaPlayer.MediaController.PlayMedia.mediaPlayerDuration;


public class MediaController implements SeekBar.OnSeekBarChangeListener {


    private CustomSeekBar seekBar;
    private TextView currentPositionTextView;
    private TextView maxLengthTextView;
    private CONTEXT context;
    private ImageView playPause;
    private boolean f = true;


    public MediaController(final CustomSeekBar seekBar, final TextView currentPositionTextView, final TextView maxLengthTextView, final ImageView playPause, final CONTEXT context) {

        this.seekBar = seekBar;
        this.currentPositionTextView = currentPositionTextView;
        this.maxLengthTextView = maxLengthTextView;
        this.playPause = playPause;
        this.context = context;

    }

    public void seekBarOperations() {
        seekBar.setOnSeekBarChangeListener(this);
        maxLengthTextView.setText(FormatData.formatTime(mediaPlayerDuration));
        currentPositionTextView.setText(FormatData.formatTime(mediaPlayer.getCurrentPosition()));
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setFavouriteBitmap(R.mipmap.final_just_heart);

        Log.d("mpDurations", mediaPlayerDuration + context.name());
        seekBarMax = seekBar.getMax();
        if (mediaPlayer.isPlaying())
            playPause.setImageResource(R.mipmap.final_pause);
        else
            playPause.setImageResource(R.mipmap.final_pause);


        new Thread(new Runnable() {
            @Override
            public void run() {
                int currentPosition = 0;
                while (true) {

                    if (mediaPlayer.isPlaying()) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition() + 1);

                    }

                    if (currentPosition >= mediaPlayerDuration) {
                        currentPosition = 0;
                    }

                    if (context != CONTEXT.VIDEO_SCREEN) {
                        if (context == CONTEXT.MAIN && isSlideExpanded) {
                            Thread.currentThread().interrupt();
                            return;
                        }

                        if (context == CONTEXT.PLAY && isSlideCollapsed) {
                            Thread.currentThread().interrupt();
                            return;
                        }

                        if (isSongChanged) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }


            }
        }).start();


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress % 1000 > 990 && f) {
            currentPositionTextView.setText(FormatData.formatTime(progress));
            f = false;
        } else
            f = true;

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mediaPlayer.pause();

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        currentPositionTextView.setText(FormatData.formatTime(seekBar.getProgress()));
        mediaPlayer.seekTo(seekBar.getProgress());
        mediaPlayer.start();
        if (mediaPlayer.isPlaying())
            playPause.setImageResource(R.mipmap.final_pause);


    }


}
