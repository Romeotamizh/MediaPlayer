package com.romeotamizh.MusicPlayer.Activities;

import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.romeotamizh.MusicPlayer.DatabaseOperationObject;
import com.romeotamizh.MusicPlayer.Helpers.FormatTime;
import com.romeotamizh.MusicPlayer.MusicRoomDatabase;
import com.romeotamizh.MusicPlayer.PlayMusic;
import com.romeotamizh.MusicPlayer.R;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.Arrays;

import static com.romeotamizh.MusicPlayer.FavouriteMomentsRepository.databaseDeleteOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMomentsRepository.databaseGetFavouritesOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMomentsRepository.databaseInsertOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMomentsRepository.resetFavouritesOperation;
import static com.romeotamizh.MusicPlayer.Helpers.SetAlphabetImages.setAlphabetImages;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayerDuration;

//import jp.wasabeef.blurry.Blurry;
//import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;


public class PlayScreenActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    ImageView playPause;
    ImageView imageView;
    TextView titleTextView;
    TextView currentPositionTextView;
    TextView maxLengthTextView;
    public static int[] mFavouriteMomentsList = new int[100];
    String mTitle;
    String mData;
    public static boolean isFavouriteMomentsExist = false;
    IndicatorSeekBar seekBar;
    Runnable runnable;
    Boolean isSongChanged = false;
    public static int mFavouriteMomentsCount = 1;
    static int mId;
    TextView favTextView;
    MusicRoomDatabase musicRoomDatabase;
    ImageView playPauseCircle;
    ImageView favButton;
    ImageView nextFavButton;
    ImageView previousButton;
    boolean y;
    int mFavouriteMomentsListPosition = 0;
    boolean x = false;
    boolean z = true;
    final Handler handler = new Handler();

    ConstraintLayout imageViewBackground;
    DatabaseOperationObject databaseOperationObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
        titleTextView = findViewById(R.id.title_play_screen);
        imageView = findViewById(R.id.image_view);
        currentPositionTextView = findViewById(R.id.current_position);
        maxLengthTextView = findViewById(R.id.max_length);
        seekBar = findViewById(R.id.seekBar);
        /*seekBar.setDots(new int[] {25, 50, 75});
        seekBar.setDotsDrawable(R.drawable.ic_launcher_background);
        seekBar.setDotsDrawable(R.mipmap.red_play);*/
        mTitle = getIntent().getStringExtra("title");
        mData = getIntent().getStringExtra("data");
        mId = getIntent().getIntExtra("id", 0);
        Log.d("id", String.valueOf(mId));

        PlayMusic.playMusic(mData, mTitle);
        titleTextView.setText(mTitle);
        imageView.setImageResource(setAlphabetImages(mTitle));
        musicRoomDatabase = MusicRoomDatabase.getInstance(getApplicationContext());

        initializeButtons();

        imageViewBackground = findViewById(R.id.pic_layout);
        initialize();
        seekBar.setThumbAdjustAuto(false);





    }



    private void initializeButtons() {
        favTextView = findViewById(R.id.next_fav_textView);
        favTextView.setOnClickListener(this);
        nextFavButton = findViewById(R.id.next_fav);
        nextFavButton.setOnClickListener(this);
        favButton = findViewById(R.id.fav);
        previousButton = findViewById(R.id.back);
        previousButton.setOnClickListener(this);
        previousButton.setOnLongClickListener(this);
        favButton.setOnClickListener(this);
        favButton.setOnLongClickListener(this);
        playPause = findViewById(R.id.play_or_pause);
        playPause.setOnClickListener(this);
        //playPauseCircle = findViewById(R.id.play_pause);
        // playPauseCircle.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {


        databaseInsertFunction();
        resetFavouriteMoments();
        isSongChanged = true;
        super.onBackPressed();

    }

    void initialize() {
        z = true;
        isSongChanged = false;
        mFavouriteMomentsList[0] = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (mediaPlayer.isPlaying() || isSongChanged) {
                        databaseGetFavouriteMomentsFunction();
                        resetFavouriteMoments();
                        seekBarFunctions();
                        Thread.currentThread().interrupt();
                        return;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }).start();


        seekBarFunctions();
/*
        seekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {


                *//*final Handler handler = new Handler();
                handler.postDelayed(runnable = *//*
                new Runnable() {
                    @Override
                    public void run() {
                        currentPositionTextView.setText(FormatTime.formatTime(mediaPlayer.getCurrentPosition()));
                    }

                }.run();

            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

                mediaPlayer.seekTo(seekBar.getProgressLeft());
                mediaPlayer.start();
                seekBarFunctions();
                if (mediaPlayer.isPlaying())
                    playPause.setImageResource(R.mipmap.red_pause);


            }
        });*/
        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                handler.postDelayed(runnable = new Runnable() {
                    @Override
                    public void run() {
                        currentPositionTextView.setText(FormatTime.formatTime(mediaPlayer.getCurrentPosition()));
                    }
                }, 1000);

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
                mediaPlayer.pause();

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                mediaPlayer.start();
                seekBarFunctions();
                if (mediaPlayer.isPlaying())
                    playPause.setImageResource(R.mipmap.red_pause);

            }
/*
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                final Handler handler = new Handler();
                handler.postDelayed(runnable = new Runnable() {
                    @Override
                    public void run() {
                        currentPositionTextView.setText(FormatTime.formatTime(mediaPlayer.getCurrentPosition()));
                    }
                }, 1000);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());
                mediaPlayer.start();
                seekBarFunctions();
                if (mediaPlayer.isPlaying())
                    playPause.setImageResource(R.mipmap.red_pause);


            }*/
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                databaseGetFavouriteMomentsFunction();
                resetFavouriteMoments();
                mFavouriteMomentsListPosition = 0;
                playPause.setImageResource(R.mipmap.red_play);

            }
        });


    }


    public void seekBarFunctions() {
        maxLengthTextView.setText(FormatTime.formatTime(mediaPlayerDuration));
        seekBar.setTickCount(mFavouriteMomentsCount);

        // seekBar.setRange(0f,(float)mediaPlayerDuration);
        if (mediaPlayerDuration < 1000)
            seekBar.setProgress(seekBar.getMax());
        else {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int currentPosition;
                    while (mediaPlayer.isPlaying()) {
                        if (z) {
                            Log.d("mpduration", String.valueOf(mediaPlayerDuration));
                            seekBar.setMax(mediaPlayer.getDuration());
                            z = false;
                        }
                        currentPosition = mediaPlayer.getCurrentPosition();
                        currentPosition++;
                        seekBar.setProgress(currentPosition);
                        if (currentPosition >= mediaPlayerDuration || x) {
                            Thread.currentThread().interrupt();
                            return;

                        }
                    }



                }
            }).start();
        }




    }

    public void playPausePress() {
        if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {

                playPause.setImageResource(R.mipmap.red_play);
                mediaPlayer.pause();
            } else {
                playPause.setImageResource(R.mipmap.red_pause);
                mediaPlayer.start();
                seekBarFunctions();
            }
        }


    }

    public void addFavouriteMoments() {
        isFavouriteMomentsExist = true;


        mFavouriteMomentsList[mFavouriteMomentsCount++] = mediaPlayer.getCurrentPosition();
        //mFavouriteMomentsCount++;
        Arrays.sort(mFavouriteMomentsList, 0, mFavouriteMomentsCount);
        Log.d("favouritemomentadded", Arrays.toString(mFavouriteMomentsList));

        databaseInsertFunction();


    }

    public void nextFavouriteMoment() {
        int[] temp;
        int currentPosition;

        if (isFavouriteMomentsExist) {
            x = true;

            temp = Arrays.copyOfRange(mFavouriteMomentsList, 0, mFavouriteMomentsCount + 1);
            currentPosition = mediaPlayer.getCurrentPosition();
            // Log.d("nextfavouritemoment", String.valueOf(currentPosition));
            temp[mFavouriteMomentsCount] = currentPosition;
            Arrays.sort(temp);
            int currentPositionIndex;
            currentPositionIndex = Arrays.binarySearch(temp, currentPosition);
            if (currentPositionIndex < temp.length - 1)
                mediaPlayer.seekTo(temp[currentPositionIndex + 1]);

            Log.d("nextfavouritemoment", String.valueOf(mediaPlayer.getCurrentPosition()));
            Log.d("nextfavouritemoment", Arrays.toString(temp));
            x = false;
            seekBarFunctions();

        }
    }

    public void previousFavouriteMoment() {
        int[] temp;
        if (isFavouriteMomentsExist) {
            x = true;
            temp = Arrays.copyOfRange(mFavouriteMomentsList, 0, mFavouriteMomentsCount + 1);
            int currentPosition = mediaPlayer.getCurrentPosition();
            temp[mFavouriteMomentsCount] = currentPosition;
            Arrays.sort(temp);
            int currentPositionIndex;
            currentPositionIndex = Arrays.binarySearch(temp, currentPosition);
            if (y) {
                if (currentPositionIndex >= 2)
                    mediaPlayer.seekTo(temp[currentPositionIndex - 2]);
                else if (currentPositionIndex >= 1)
                    mediaPlayer.seekTo(temp[currentPositionIndex - 1]);
            } else
                mediaPlayer.seekTo(temp[currentPositionIndex - 1]);
            Log.d("prevfavouritemoment", String.valueOf(mediaPlayer.getCurrentPosition()));
            Log.d("prevfavouritemoment", Arrays.toString(temp));
            x = false;
            seekBarFunctions();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {

            case R.id.fav:
                databaseDeleteFunction();
                break;

            case R.id.back:
                y = true;
                previousFavouriteMoment();
                break;


            default:
                break;

        }

        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fav:
                addFavouriteMoments();
                break;

            case R.id.back:
                y = false;
                previousFavouriteMoment();
                break;

            case R.id.next_fav:
                nextFavouriteMoment();
                break;

            case R.id.play_or_pause:
                playPausePress();
                break;


            default:
                break;


        }

    }

    public void databaseDeleteFunction() {
        databaseDeleteOperation(mId, "music");

    }

    public void databaseGetFavouriteMomentsFunction() {


        databaseGetFavouritesOperation(mId, "music");
    }

    public void databaseInsertFunction() {

        databaseInsertOperation(mId, this.mFavouriteMomentsCount, this.mFavouriteMomentsList, "music");

    }

    public void resetFavouriteMoments() {
        resetFavouritesOperation("music");

    }



}
