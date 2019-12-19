package com.romeotamizh.MusicPlayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import static com.romeotamizh.MusicPlayer.MainActivity.mediaPlayer;

public class PlayScreenActivity extends AppCompatActivity {

    static ImageView playPause;
    ImageView imageView;
    TextView title;
    TextView currentPosition;
    TextView maxLength;
    String mTitle;
    SeekBar seekBar;

    public static void playMusic(final String mData, final String mTitle) {

        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setVolume(1.0f, 1.0f);
            mediaPlayer.setDataSource(mData);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    MainActivity.openPlayScreen(mData, mTitle);

                }
            });
            mediaPlayBackListener();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void mediaPlayBackListener() {
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
        title = findViewById(R.id.title_play_screen);
        imageView = findViewById(R.id.image_view);
        playPause = findViewById(R.id.play_pause);
        currentPosition = findViewById(R.id.current_position);
        maxLength = findViewById(R.id.max_length);
        seekBar = findViewById(R.id.seekbar);
        initialize();
        seekBarFunctions();


    }

    void initialize() {


        mTitle = getIntent().getStringExtra("title");
        title.setText(mTitle);
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
        maxLength = findViewById(R.id.max_length);
        Integer m = (mediaPlayer.getDuration() / 1000) / 60;
        Integer s = (mediaPlayer.getDuration() / 1000) % 60;


        if (m == 0 && s == 0)
            maxLength.setText("00:01");
        else {
            if (m < 10 && s < 10)
                maxLength.setText("0" + m + ":" + "0" + s);

            else if (m < 10 && s > 10)
                maxLength.setText("0" + m + ":" + s);
            else if (m >= 10 && s < 10)
                maxLength.setText(m + ":" + "0" + s);
            else
                maxLength.setText(m + ":" + s);
        }

        /*final ObjectAnimator objectAnimator = ObjectAnimator.ofInt(seekBar,"progress",0,mediaPlayer.getDuration());
        objectAnimator.setDuration(seekBar.getMax());
        objectAnimator.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return .01f;
            }
        });
        objectAnimator.start();*/


        seekBar.setMax(mediaPlayer.getDuration());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int currentPosition = 0;
                while (true) {
                    currentPosition = mediaPlayer.getCurrentPosition();
                    currentPosition++;

                    seekBar.setProgress(currentPosition);
                    if (currentPosition >= mediaPlayer.getDuration()) {
                        currentPosition = 0;
                        break;
                    }
                }

            }
        }).start();
        seekBar.clearAnimation();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // onProgressChanged(seekBar,mediaPlayer.getCurrentPosition(),false);

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

        if (mediaPlayer.isPlaying()) {

            playPause.setImageResource(R.mipmap.play);
            mediaPlayer.pause();
        } else {
            playPause.setImageResource(R.mipmap.pause);
            mediaPlayer.start();
            seekBarFunctions();
        }
        // PlaybackParams playbackParams =mediaPlayer.getPlaybackParams();


    }


}
