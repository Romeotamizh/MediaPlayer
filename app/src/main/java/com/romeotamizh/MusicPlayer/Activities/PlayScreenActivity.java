package com.romeotamizh.MusicPlayer.Activities;

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

import com.romeotamizh.MusicPlayer.Helpers.SeekbarWithFavourites;
import com.romeotamizh.MusicPlayer.Helpers.TimeFormat;
import com.romeotamizh.MusicPlayer.PlayMusic;
import com.romeotamizh.MusicPlayer.R;

import java.util.Arrays;

import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.databaseDeleteOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.databaseGetFavouritesOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.databaseInsertOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.resetFavouritesOperation;
import static com.romeotamizh.MusicPlayer.Helpers.SetAlphabetImages.setAlphabetImages;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayerDuration;


public class PlayScreenActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    public static int[] mFavouriteMomentsList = new int[100];
    public static boolean isFavouriteMomentsExist = false;
    public static int seekBarMax;
    public static int mFavouriteMomentsCount = 1;
    public static int seekBarWidth;
    static int mId;
    ImageView playPause;
    ImageView imageView;
    TextView titleTextView;
    TextView currentPositionTextView;
    TextView maxLengthTextView;
    String mTitle;
    String mData;
    Runnable runnable;
    Boolean isSongChanged = false;
    TextView favTextView;
    ImageView favButton;
    ImageView nextFavButton;
    ImageView previousButton;
    boolean isPreviousButtonLongPressed;
    int mFavouriteMomentsListPosition = 0;
    boolean isSeekBarFlagSet = false;
    boolean isListenerFlagSet = true;
    ConstraintLayout imageViewBackground;
    SeekbarWithFavourites seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);

        //initialize views
        initializeViews();

        //get intent
        getIntentFunction();


        //seekBar properties
        setSeekBarProperties();


        //play music
        PlayMusic.playMusic(mData, mTitle);


        titleTextView.setText(mTitle);
        imageView.setImageResource(setAlphabetImages(mTitle));

        // set buttons onClick and onLongCick
        setButtonsClickFunctions();

        //listen for seekBar and media changes
        listenerFunction();


    }

    private void setSeekBarProperties() {
        seekBarMax = seekBar.getMax();
        seekBar.setmFavouriteBitmap(R.mipmap.red_play);
        seekBar.post(new Runnable() {
            @Override
            public void run() {
                seekBarWidth = seekBar.getMeasuredWidth();
                //    seekBarMax = seekBar.getMax();

            }
        });
    }

    private void getIntentFunction() {
        mTitle = getIntent().getStringExtra("title");
        mData = getIntent().getStringExtra("data");
        mId = getIntent().getIntExtra("id", 0);
        Log.d("id", String.valueOf(mId));


    }

    private void initializeViews() {

        titleTextView = findViewById(R.id.title_play_screen);
        imageView = findViewById(R.id.image_view);
        currentPositionTextView = findViewById(R.id.current_position);
        maxLengthTextView = findViewById(R.id.max_length);
        seekBar = findViewById(R.id.seekBar);
        favTextView = findViewById(R.id.next_fav_textView);
        nextFavButton = findViewById(R.id.next_fav);
        favButton = findViewById(R.id.fav);
        previousButton = findViewById(R.id.back);
        playPause = findViewById(R.id.play_or_pause);
        imageViewBackground = findViewById(R.id.pic_layout);


    }

    private void setButtonsClickFunctions() {

        favTextView.setOnClickListener(this);
        nextFavButton.setOnClickListener(this);

        previousButton.setOnClickListener(this);
        previousButton.setOnLongClickListener(this);
        favButton.setOnClickListener(this);
        favButton.setOnLongClickListener(this);
        playPause.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {


        databaseInsertFunction();
        resetFavouriteMoments();
        isSongChanged = true;
        super.onBackPressed();

    }

    void listenerFunction() {
        isListenerFlagSet = true;
        isSongChanged = false;


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
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());
                mediaPlayer.start();
                seekBarFunctions();
                if (mediaPlayer.isPlaying())
                    playPause.setImageResource(R.mipmap.red_pause);


            }
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
        maxLengthTextView.setText(TimeFormat.formatTime(mediaPlayerDuration));
        seekBar.setMax(mediaPlayer.getDuration());
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
                            databaseGetFavouriteMomentsFunction();
                            seekBar.setMax(mediaPlayer.getDuration());
                            seekBarMax = mediaPlayer.getDuration();
                            Log.d("mpduration", String.valueOf(mediaPlayerDuration));
                            isListenerFlagSet = false;
                        }
                        currentPosition = mediaPlayer.getCurrentPosition();
                        currentPosition++;
                        seekBar.setProgress(currentPosition);
                        if (currentPosition >= mediaPlayerDuration || isSeekBarFlagSet) {
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
        Arrays.sort(mFavouriteMomentsList, 0, mFavouriteMomentsCount);
        Log.d("favouritemomentadded", Arrays.toString(mFavouriteMomentsList));

        databaseInsertFunction();


    }

    public void nextFavouriteMoment() {
        int[] temp;
        int currentPosition;

        if (isFavouriteMomentsExist) {
            isSeekBarFlagSet = true;

            temp = Arrays.copyOfRange(mFavouriteMomentsList, 0, mFavouriteMomentsCount + 1);
            currentPosition = mediaPlayer.getCurrentPosition();
            // Log.d("nextfavouritemoment", String.valueOf(currentPosition));
            temp[mFavouriteMomentsCount] = currentPosition;
            Arrays.sort(temp);
            int currentPositionIndex;
            currentPositionIndex = Arrays.binarySearch(temp, currentPosition);
            if (currentPositionIndex < temp.length - 1) {
                if (!mediaPlayer.isPlaying())
                    seekBar.setProgress(temp[currentPositionIndex + 1]);
                mediaPlayer.seekTo(temp[currentPositionIndex + 1]);
            }

            Log.d("nextfavouritemoment", String.valueOf(mediaPlayer.getCurrentPosition()));
            Log.d("nextfavouritemoment", Arrays.toString(temp));
            isSeekBarFlagSet = false;
            seekBarFunctions();


        }

    }

    public void previousFavouriteMoment() {
        int[] temp;
        if (isFavouriteMomentsExist) {
            isSeekBarFlagSet = true;
            temp = Arrays.copyOfRange(mFavouriteMomentsList, 0, mFavouriteMomentsCount + 1);
            int currentPosition = mediaPlayer.getCurrentPosition();
            temp[mFavouriteMomentsCount] = currentPosition;
            Arrays.sort(temp);
            int currentPositionIndex;
            currentPositionIndex = Arrays.binarySearch(temp, currentPosition);
            if (isPreviousButtonLongPressed) {
                if (currentPositionIndex >= 2) {
                    if (!mediaPlayer.isPlaying())
                        seekBar.setProgress(temp[currentPositionIndex - 2]);
                    mediaPlayer.seekTo(temp[currentPositionIndex - 2]);
                } else if (currentPositionIndex >= 1) {
                    if (!mediaPlayer.isPlaying())
                        seekBar.setProgress(temp[currentPositionIndex - 1]);
                    mediaPlayer.seekTo(temp[currentPositionIndex - 1]);
                }
            } else {
                if (!mediaPlayer.isPlaying())
                    seekBar.setProgress(temp[currentPositionIndex - 1]);
                mediaPlayer.seekTo(temp[currentPositionIndex - 1]);
            }
            Log.d("prevfavouritemoment", String.valueOf(mediaPlayer.getCurrentPosition()));
            Log.d("prevfavouritemoment", Arrays.toString(temp));
            isSeekBarFlagSet = false;

        } else {
            isSeekBarFlagSet = true;
            mediaPlayer.seekTo(0);
            isSeekBarFlagSet = false;


        }
        seekBarFunctions();
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {

            case R.id.fav:
                databaseDeleteFunction();
                break;

            case R.id.back:
                isPreviousButtonLongPressed = true;
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
                isPreviousButtonLongPressed = false;
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

        databaseInsertOperation(mId, mFavouriteMomentsCount, mFavouriteMomentsList, "music");

    }

    public void resetFavouriteMoments() {
        resetFavouritesOperation("music");

    }


}
