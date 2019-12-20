package com.romeotamizh.MusicPlayer.Activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.romeotamizh.MusicPlayer.FormatTime;
import com.romeotamizh.MusicPlayer.PlayMusic;
import com.romeotamizh.MusicPlayer.R;

import static com.romeotamizh.MusicPlayer.Helpers.SetAlphabetImages.setAlphabetImages;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;
//import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;


public class PlayScreenActivity extends AppCompatActivity {

    static ImageView playPause;
    ImageView imageView;
    TextView titleTextView;
    TextView currentPositionTextView;
    TextView maxLengthTextView;
    String mTitle;
    String mData;
    SeekBar seekBar;
    Runnable runnable;
    // Activity activity = this;

    public static void mediaPlayBackListener() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playPause.setImageResource(R.mipmap.play);

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
        titleTextView = findViewById(R.id.title_play_screen);
        imageView = findViewById(R.id.image_view);
        playPause = findViewById(R.id.play_pause);
        currentPositionTextView = findViewById(R.id.current_position);
        maxLengthTextView = findViewById(R.id.max_length);
        seekBar = findViewById(R.id.seekbar);
        initialize();


    }

    @Override
    public void onBackPressed() {
        Thread.currentThread().interrupt();

        super.onBackPressed();
    }

    void initialize() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (mediaPlayer.isPlaying()) {
                        seekBarFunctions();
                        Thread.currentThread().interrupt();
                        return;
                    }
                }


            }
        }).start();

        mTitle = getIntent().getStringExtra("title");
        mData = getIntent().getStringExtra("data");
        titleTextView.setText(mTitle);
        imageView.setImageResource(setAlphabetImages(mTitle));
        PlayMusic.playMusic(mData, mTitle);
        seekBarFunctions();


    }

    public void seekBarFunctions() {
        maxLengthTextView.setText(FormatTime.formatTime(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());

        new Thread(new Runnable() {
            @Override
            public void run() {

                int currentPosition;
                while (mediaPlayer.isPlaying()) {
                    currentPosition = mediaPlayer.getCurrentPosition();
                    currentPosition++;
                    seekBar.setProgress(currentPosition);
                    if (currentPosition >= mediaPlayer.getDuration()) {
                        Thread.currentThread().interrupt();
                        return;

                    }
                }


            }
        }).start();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final Handler handler = new Handler();
                handler.postDelayed(runnable = new Runnable() {
                    @Override
                    public void run() {
                        currentPositionTextView.setText(FormatTime.formatTime(mediaPlayer.getCurrentPosition()));
                    }
                }, 100);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());


            }
        });

    }

    public void playPausePress(View view) {
        if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {

                playPause.setImageResource(R.mipmap.play);
                mediaPlayer.pause();
            } else {
                playPause.setImageResource(R.mipmap.pause);
                mediaPlayer.start();
                seekBarFunctions();
            }
        }


    }


}
