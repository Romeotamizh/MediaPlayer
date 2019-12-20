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
import com.romeotamizh.MusicPlayer.R;

import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;


public class PlayScreenActivity extends AppCompatActivity {

    static ImageView playPause;
    ImageView imageView;
    TextView titleTextView;
    TextView currentPositionTextView;
    TextView maxLengthTextView;
    String mTitle;
    SeekBar seekBar;
    Runnable runnable;


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
        seekBarFunctions();


    }

    @Override
    public void onBackPressed() {
        Thread.currentThread().interrupt();

        super.onBackPressed();
    }


    void initialize() {


        mTitle = getIntent().getStringExtra("title");
        titleTextView.setText(mTitle);
        setAlphabetImage(mTitle);


    }


    void setAlphabetImage(String mTitle) {


        switch (mTitle.toLowerCase().charAt(0)) {

            case 'a':
                imageView.setImageResource(R.mipmap.a);
                break;
            case 'b':
                imageView.setImageResource(R.mipmap.b);
                break;

            case 'c':
                imageView.setImageResource(R.mipmap.c);
                break;
            case 'd':
                imageView.setImageResource(R.mipmap.d);
                break;
            case 'e':
                imageView.setImageResource(R.mipmap.e);
                break;
            case 'f':
                imageView.setImageResource(R.mipmap.f);
                break;

            case 'g':
                imageView.setImageResource(R.mipmap.g);
                break;
            case 'h':
                imageView.setImageResource(R.mipmap.h);
                break;

            case 'i':
                imageView.setImageResource(R.mipmap.i);
                break;


            case 'j':
                imageView.setImageResource(R.mipmap.j);
                break;
            case 'k':
                imageView.setImageResource(R.mipmap.k);
                break;
            case 'l':
                imageView.setImageResource(R.mipmap.l);
                break;
            case 'm':
                imageView.setImageResource(R.mipmap.m);
                break;
            case 'n':
                imageView.setImageResource(R.mipmap.n);
                break;
            case 'o':
                imageView.setImageResource(R.mipmap.o);
                break;
            case 'p':
                imageView.setImageResource(R.mipmap.p);
                break;
            case 'q':
                imageView.setImageResource(R.mipmap.q);
                break;

            case 'r':
                imageView.setImageResource(R.mipmap.r);
                break;

            case 's':
                imageView.setImageResource(R.mipmap.s);
                break;

            case 't':
                imageView.setImageResource(R.mipmap.t);
                break;

            case 'u':
                imageView.setImageResource(R.mipmap.u);
                break;

            case 'v':
                imageView.setImageResource(R.mipmap.v);
                break;
            case 'w':
                imageView.setImageResource(R.mipmap.w);
                break;
            case 'x':
                imageView.setImageResource(R.mipmap.x);
                break;

            case 'y':
                imageView.setImageResource(R.mipmap.y);
                break;
            case 'z':
                imageView.setImageResource(R.mipmap.z);
                break;


            default:
                break;
        }

    }

    void seekBarFunctions() {
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
                // onProgressChanged(seekBar,mediaPlayer.getCurrentPosition(),false);
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
