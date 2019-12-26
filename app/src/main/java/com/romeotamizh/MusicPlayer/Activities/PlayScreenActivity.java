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

import com.romeotamizh.MusicPlayer.Helpers.FormatTime;
import com.romeotamizh.MusicPlayer.Music;
import com.romeotamizh.MusicPlayer.MusicRoomDatabase;
import com.romeotamizh.MusicPlayer.PlayMusic;
import com.romeotamizh.MusicPlayer.R;

import java.util.Arrays;

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
    String mTitle;
    String mData;
    static int[] mFavouriteMomentsList = new int[100];
    SeekBar seekBar;
    Runnable runnable;
    Boolean isSongChanged = false;
    public boolean isFavouriteMomentsExist = false;
    int mId;
    int mFavouriteMomentsCount = 1;
    MusicRoomDatabase musicRoomDatabase;
    ImageView favButton;
    ImageView previousButton;
    boolean y;
    int mFavouriteMomentsListPosition = 0;
    boolean x = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
        titleTextView = findViewById(R.id.title_play_screen);
        imageView = findViewById(R.id.image_view);
        playPause = findViewById(R.id.play_or_pause);
        currentPositionTextView = findViewById(R.id.current_position);
        maxLengthTextView = findViewById(R.id.max_length);
        seekBar = findViewById(R.id.seekBar);
        mTitle = getIntent().getStringExtra("title");
        mData = getIntent().getStringExtra("data");
        mId = getIntent().getIntExtra("id", 0);
        Log.d("id", String.valueOf(mId));

        PlayMusic.playMusic(mData, mTitle);
        titleTextView.setText(mTitle);
        imageView.setImageResource(setAlphabetImages(mTitle));
        musicRoomDatabase = MusicRoomDatabase.getInstance(getApplicationContext());


        favButton = findViewById(R.id.fav);
        previousButton = findViewById(R.id.back);
        previousButton.setOnClickListener(this);
        previousButton.setOnLongClickListener(this);
        favButton.setOnClickListener(this);
        favButton.setOnLongClickListener(this);

        initialize();


    }

    @Override
    public void onBackPressed() {


        //Thread.currentThread().interrupt();
        databaseInsertFunction();


        isSongChanged = true;

        super.onBackPressed();

    }

    void initialize() {
        isSongChanged = false;
        mFavouriteMomentsList[0] = 0;
        //  databaseGetFavouriteMomentsFunction();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (mediaPlayer.isPlaying() || isSongChanged) {
                        seekBarFunctions();
                        Thread.currentThread().interrupt();
                        return;
                    }
                }


            }
        }).start();

        databaseGetFavouriteMomentsFunction();


        seekBarFunctions();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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


            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                databaseInsertFunction();
                mFavouriteMomentsListPosition = 0;
                playPause.setImageResource(R.mipmap.red_play);

            }
        });


        // Blurry.with(getBaseContext()).radius(4).sampling(4).color(255).async().onto(viewGroup);

        previousButton = findViewById(R.id.back);
        previousButton.setOnLongClickListener(this);
        /*previousButton.setOnLongClickListener(new View.OnLongClickListener()  {
            @Override
            public boolean onLongClick(View v) {
                y = true;
                return false;
            }
        });*/

        /*favButton.setOnLongClickListener(new View.OnLongClickListener()  {
            @Override
            public boolean onLongClick(View v) {
                databaseDeleteFunction();
                mFavouriteMomentsList = new int[100];
                mFavouriteMomentsCount = 1;
                isFavouriteMomentsExist =false;
                Log.d("db","dbdel");
                return true;
            }
        });*/


    }

    public void databaseInsertFunction() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // MusicRoomDatabase musicRoomDatabase = MusicRoomDatabase.getInstance(getApplicationContext());


                for (int i = 0; i < mFavouriteMomentsCount; i++) {
                    Music music = new Music(String.valueOf(mId) + "." + String.valueOf(mFavouriteMomentsList[i]), mId, mFavouriteMomentsList[i]);
                    musicRoomDatabase.musicDao().insertData(music);
                    //musicRoomDatabase.musicDao().insertData(mId,mFavouriteMomentsList[i]);

                }
                Log.d("dbinsfn", Arrays.toString(musicRoomDatabase.musicDao().getFavouriteMoments(mId)));
                Log.d("dbinsfn", Arrays.toString(musicRoomDatabase.musicDao().getId(mId)));
                mFavouriteMomentsList = new int[100];
                mFavouriteMomentsList[0] = 0;
                databaseGetFavouriteMomentsFunction();


                // Thread.currentThread().interrupt();
                return;


            }
        }).start();


    }

    public void databaseGetFavouriteMomentsFunction() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // MusicRoomDatabase musicRoomDatabase = MusicRoomDatabase.getInstance(getApplicationContext());
                int[] temp = new int[100];

                temp = musicRoomDatabase.musicDao().getFavouriteMoments(mId);
                Log.d("getfavfn", Arrays.toString(musicRoomDatabase.musicDao().getFavouriteMoments(mId)));
                // Log.d("getfavfn", String.valueOf(mId));
                Log.d("getfavfn", String.valueOf(temp.length));


                if (temp.length < 1) {
                    isFavouriteMomentsExist = false;
                } else if (temp.length == 1) {
                    isFavouriteMomentsExist = false;


                } else {
                    Arrays.sort(temp);
                    mFavouriteMomentsList = Arrays.copyOf(temp, 100);

                    /*for (int x = 1; x < temp.length; x++) {
                        mFavouriteMomentsList[x] = temp[x];

                    }*/
                    //mFavouriteMomentsCount = mFavouriteMomentsList.length;
                    mFavouriteMomentsCount = temp.length;

                    isFavouriteMomentsExist = true;
                }
                Log.d("getfavfn", Arrays.toString(mFavouriteMomentsList));
                Log.d("getfavfn", String.valueOf(mFavouriteMomentsCount));


                //   Thread.currentThread().interrupt();
                return;


            }
        }).start();


    }

    public void databaseDeleteFunction() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                int temp = musicRoomDatabase.musicDao().delete(mId);
                mFavouriteMomentsList = new int[100];
                mFavouriteMomentsCount = 1;
                isFavouriteMomentsExist = false;
                Log.d("dbdel ", String.valueOf(temp));


            }
        }).start();

    }


    public void seekBarFunctions() {
        maxLengthTextView.setText(FormatTime.formatTime(mediaPlayerDuration));
        seekBar.setMax(mediaPlayerDuration);
        if (mediaPlayerDuration < 1000)
            seekBar.setProgress(seekBar.getMax());
        else {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int currentPosition;


                    while (mediaPlayer.isPlaying()) {
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

    public void playPausePress(View view) {
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
        //Log.d("favouritemomentadded", String.valueOf(mFavouriteMomentsList[mFavouriteMomentsListPosition]));
        Arrays.sort(mFavouriteMomentsList, 0, mFavouriteMomentsCount);
        Log.d("favouritemomentadded", Arrays.toString(mFavouriteMomentsList));


    }

    public void nextFavouriteMoment(View view) {
        int[] temp;
        int currentPosition;

        if (isFavouriteMomentsExist) {
            // if (mFavouriteMomentsListPosition < mFavouriteMomentsCount - 1) {
            x = true;

            temp = Arrays.copyOfRange(mFavouriteMomentsList, 0, mFavouriteMomentsCount + 1);
            currentPosition = mediaPlayer.getCurrentPosition();
            Log.d("favouritemoment", String.valueOf(currentPosition));
            temp[mFavouriteMomentsCount] = currentPosition;
            Arrays.sort(temp);
            int currentPositionIndex;
            currentPositionIndex = Arrays.binarySearch(temp, currentPosition);
            if (currentPositionIndex < temp.length - 1)
                mediaPlayer.seekTo(temp[currentPositionIndex + 1]);

            //mediaPlayer.seekTo(mFavouriteMomentsList[++mFavouriteMomentsListPosition]);
            // Log.d("favouritemoment", String.valueOf(temp[currentPositionIndex]));
            Log.d("favouritemoment", String.valueOf(mediaPlayer.getCurrentPosition()));
            Log.d("favouritemoment", Arrays.toString(temp));
            x = false;
            seekBarFunctions();
            temp = Arrays.copyOfRange(mFavouriteMomentsList, 0, mFavouriteMomentsCount + 1);
            //  }
        }
    }

    public void previousFavouriteMoment() {
        int[] temp;
        if (isFavouriteMomentsExist) {
            //  if (mFavouriteMomentsListPosition > 0) {
            x = true;

            temp = Arrays.copyOfRange(mFavouriteMomentsList, 0, mFavouriteMomentsCount + 1);

            int currentPosition = mediaPlayer.getCurrentPosition();
            //    Log.d("favouritemoment", String.valueOf(currentPosition));
            temp[mFavouriteMomentsCount] = currentPosition;
            Arrays.sort(temp);
            int currentPositionIndex;
            currentPositionIndex = Arrays.binarySearch(temp, currentPosition);
            //if(currentPositionIndex>2)
            if (y) {
                if (currentPositionIndex >= 2)
                    mediaPlayer.seekTo(temp[currentPositionIndex - 2]);
                else if (currentPositionIndex >= 1)
                    mediaPlayer.seekTo(temp[currentPositionIndex - 1]);
            } else
                mediaPlayer.seekTo(temp[currentPositionIndex - 1]);
            //mediaPlayer.seekTo(mFavouriteMomentsList[--mFavouriteMomentsListPosition]);
            Log.d("favouritemoment", String.valueOf(mediaPlayer.getCurrentPosition()));
            Log.d("favouritemoment", Arrays.toString(temp));
            x = false;
            seekBarFunctions();
            //   }
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

            default:
                break;


        }

    }
}
