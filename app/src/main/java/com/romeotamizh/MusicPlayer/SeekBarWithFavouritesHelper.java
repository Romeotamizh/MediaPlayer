package com.romeotamizh.MusicPlayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.romeotamizh.MusicPlayer.Helpers.SeekbarWithFavourites;
import com.romeotamizh.MusicPlayer.Helpers.TimeFormat;

import static com.romeotamizh.MusicPlayer.Activities.MainActivity.isFromMainActivity;
import static com.romeotamizh.MusicPlayer.Activities.MainActivity.onReturn;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.isBackPressed;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.isListenerFlagSet;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.isSeekBarFlagSet;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.mId;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.maxLengthTextView;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.playPause;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.seekBarMax;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.databaseGetFavouritesOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.resetFavouritesOperation;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayerDuration;

//import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.seekBar;


public class SeekBarWithFavouritesHelper {


    static Runnable runnable;


    public static void seekBarListener(final SeekbarWithFavourites seekBar, final TextView currentPositionTextView, final String context) {

        if ((!isBackPressed && context.equals("play")) || (isFromMainActivity && context.equals("main"))) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    final Handler handler = new Handler();
                    handler.postDelayed(runnable = new Runnable() {
                        @Override
                        public void run() {
                            currentPositionTextView.setText(TimeFormat.formatTime(mediaPlayer.getCurrentPosition()));
                        }
                    }, 1000);


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.pause();

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBarl) {

                    mediaPlayer.seekTo(seekBar.getProgress());
                    mediaPlayer.start();
                    seekBarOperations(seekBar, maxLengthTextView, context);
                    if (mediaPlayer.isPlaying())
                        playPause.setImageResource(R.mipmap.red_pause);


                }
            });


            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    databaseGetFavouritesOperation(mId, "music");
                    resetFavouritesOperation("music");
                    playPause.setImageResource(R.mipmap.red_play);

                }
            });
        }


    }

    public static void seekBarOperations(final SeekbarWithFavourites seekBar, final TextView maxLengthTextView, final String context) {
        maxLengthTextView.setText(TimeFormat.formatTime(mediaPlayerDuration));
        seekBar.setMax(mediaPlayer.getDuration());
        Log.d("mpdurations", String.valueOf(mediaPlayerDuration));

        seekBarMax = seekBar.getMax();
        if (mediaPlayerDuration < 1000)
            seekBar.setProgress(seekBar.getMax());
        else {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int currentPosition;
                    while (mediaPlayer.isPlaying()) {
                        if (isListenerFlagSet) {
                            databaseGetFavouritesOperation(mId, "music");
                            seekBar.setMax(mediaPlayer.getDuration());
                            seekBarMax = mediaPlayer.getDuration();
                            Log.d("mpduration", String.valueOf(mediaPlayerDuration));
                            isListenerFlagSet = false;
                        }

                        currentPosition = mediaPlayer.getCurrentPosition();
                        currentPosition++;
                        seekBar.setProgress(currentPosition);
                        if ((currentPosition >= mediaPlayerDuration || isSeekBarFlagSet) && context.equals("play") || (currentPosition >= mediaPlayerDuration || (!isFromMainActivity && context.equals("main")))) {
                            Thread.currentThread().interrupt();
                            return;

                        }
                        if (isBackPressed && context.equals("play")) {
                            onReturn();
                            Thread.currentThread().interrupt();
                            return;

                        }
                    }


                }
            }).start();
        }


    }

}
