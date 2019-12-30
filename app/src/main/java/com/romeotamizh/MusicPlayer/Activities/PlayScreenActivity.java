package com.romeotamizh.MusicPlayer.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.romeotamizh.MusicPlayer.Helpers.SeekbarWithFavourites;
import com.romeotamizh.MusicPlayer.PlayMusic;
import com.romeotamizh.MusicPlayer.R;

import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.addFavouriteMomentsOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.databaseDeleteOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.databaseGetFavouritesOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.nextFavouriteMomentOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.previousFavouriteMomentOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.resetFavouritesOperation;
import static com.romeotamizh.MusicPlayer.Helpers.SetAlphabetImages.setAlphabetImages;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;
import static com.romeotamizh.MusicPlayer.SeekBarWithFavouritesHelper.playPausePress;
import static com.romeotamizh.MusicPlayer.SeekBarWithFavouritesHelper.seekBarListener;
import static com.romeotamizh.MusicPlayer.SeekBarWithFavouritesHelper.seekBarOperations;


public class PlayScreenActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    public static int[] mFavouriteMomentsList = new int[100];
    public static boolean isFavouriteMomentsExist = false;
    public static int mFavouriteMomentsCount = 1;


    public static int seekBarMax;

    public static int seekBarWidth;
    public static int mId;
    public static ImageView playPause;
    ImageView imageView;
    TextView titleTextView;
    public static TextView currentPositionTextView;
    public static TextView maxLengthTextView;
    String mTitle;
    String mData;
    public static Boolean isSongChanged = false;
    ImageView favButton;
    ImageView nextFavButton;
    ImageView previousButton;
    public static boolean isPreviousButtonLongPressed;
    public static boolean isListenerFlagSet = true;
    ConstraintLayout imageViewBackground;
    public static SeekbarWithFavourites seekBar;
    public static boolean isBackPressed = false;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
        context = this;

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

    public void setSeekBarProperties() {
        seekBarMax = seekBar.getMax();
        seekBar.setmFavouritesPositionsList(mFavouriteMomentsList);
        seekBar.setmFavouriteBitmap(R.mipmap.red_play);
        seekBar.post(new Runnable() {
            @Override
            public void run() {
                seekBarWidth = seekBar.getMeasuredWidth() - seekBar.getPaddingStart() - seekBar.getPaddingEnd();


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
        nextFavButton = findViewById(R.id.next_fav);
        favButton = findViewById(R.id.fav);
        previousButton = findViewById(R.id.back);
        playPause = findViewById(R.id.play_or_pause);
        imageViewBackground = findViewById(R.id.pic_layout);


    }

    private void setButtonsClickFunctions() {

        nextFavButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        previousButton.setOnLongClickListener(this);
        favButton.setOnClickListener(this);
        favButton.setOnLongClickListener(this);
        playPause.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {


        isBackPressed = true;
        super.onBackPressed();

    }

    private void listenerFunction() {
        isListenerFlagSet = true;
        isSongChanged = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (mediaPlayer.isPlaying() || isSongChanged) {
                        resetFavouritesOperation("music");
                        databaseGetFavouritesOperation(mId, "music");
                        // databaseGetFavouritesOperation(mId, "music");
                        seekBarListener(seekBar, currentPositionTextView, "play");
                        seekBarOperations(seekBar, maxLengthTextView, "play");
                        databaseGetFavouritesOperation(mId, "music");

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



    }



    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {

            case R.id.fav:
                databaseDeleteOperation(mId, "music");
                break;

            case R.id.back:
                isPreviousButtonLongPressed = true;
                previousFavouriteMomentOperation(mId, mFavouriteMomentsCount, mFavouriteMomentsList, isFavouriteMomentsExist, isPreviousButtonLongPressed, seekBar);

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
                addFavouriteMomentsOperation(mId, mFavouriteMomentsCount, mFavouriteMomentsList, isFavouriteMomentsExist, "music");
                break;

            case R.id.back:
                isPreviousButtonLongPressed = false;
                previousFavouriteMomentOperation(mId, mFavouriteMomentsCount, mFavouriteMomentsList, isFavouriteMomentsExist, isPreviousButtonLongPressed, seekBar);

                break;

            case R.id.next_fav:
                nextFavouriteMomentOperation(mId, mFavouriteMomentsCount, mFavouriteMomentsList, isFavouriteMomentsExist, seekBar);
                break;

            case R.id.play_or_pause:
                playPausePress("play");
                break;


            default:
                break;


        }

    }









}
