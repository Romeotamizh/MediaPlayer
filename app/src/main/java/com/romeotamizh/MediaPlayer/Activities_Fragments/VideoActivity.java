package com.romeotamizh.MediaPlayer.Activities_Fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.romeotamizh.MediaPlayer.Helpers.Context;
import com.romeotamizh.MediaPlayer.Helpers.CustomSeekBar;
import com.romeotamizh.MediaPlayer.Helpers.FavouriteMoments;
import com.romeotamizh.MediaPlayer.MediaController.MediaController;
import com.romeotamizh.MediaPlayer.MediaController.PlayMedia;
import com.romeotamizh.MediaPlayer.R;

import static com.romeotamizh.MediaPlayer.Helpers.CustomSeekBar.mFavouritesPositionsList;
import static com.romeotamizh.MediaPlayer.MediaController.PlayMedia.mediaPlayer;
import static com.romeotamizh.MediaPlayer.MediaController.PlayMedia.mediaPlayerDuration;


public class VideoActivity extends AppCompatActivity implements View.OnClickListener, FavouriteMoments.OnFavouriteMomentsOperationsListener, View.OnLongClickListener {

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private static int id;
    private static Uri uri;
    private static CharSequence mTitle;
    private final Handler mHideHandler = new Handler();
    private FavouriteMoments favouriteMomentsVideo;
    private SurfaceView surfaceView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

            surfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private TextView titleTextViewVideo;
    private TextView currentPositionTextViewVideo;
    private TextView maxLengthTextViewVideo;
    private ImageView playPauseVideo;
    private ImageView nextFavButtonVideo;
    private ImageView previousButtonVideo;
    private ImageView favButtonVideo;

    private CustomSeekBar seekBarVideo;
    private MediaController mediaControllerVideo;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        surfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        delayedHide(100);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        populate();

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        surfaceView = findViewById(R.id.video_View);
        surfaceView.requestFocus();
        id = getIntent().getIntExtra("ID", 0);
        Log.d(String.valueOf(id), "pgi");
        Uri uri = Uri.parse(getIntent().getStringExtra("URI"));
        mTitle = getIntent().getCharSequenceExtra("TITLE");
        final SurfaceHolder holder = surfaceView.getHolder();
        holder.setKeepScreenOn(true);
        titleTextViewVideo = findViewById(R.id.title_video);
        PlayMedia.playMedia(uri, Context.MEDIATYPE.VIDEO);
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        titleTextViewVideo.setText(mTitle);


        playVideo();

        findViewById(R.id.seekBar_video).setOnTouchListener(mDelayHideTouchListener);
    }


    private void populate() {
        initializeViews();
        favouriteMomentsVideo = new FavouriteMoments(new int[100], 1, false, Context.MEDIATYPE.VIDEO);
        FavouriteMoments.setOnFavouriteMomentsOperationsListener(this);
        mediaControllerVideo = new MediaController(seekBarVideo, currentPositionTextViewVideo, maxLengthTextViewVideo, playPauseVideo, Context.CONTEXT.VIDEO_SCREEN);

        setSeekBarProperties();


    }

    private void playVideo() {

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                double videoRatio = (double) mediaPlayer.getVideoWidth() / (double) mediaPlayer.getVideoHeight();
                Point size = new Point();
                getWindowManager().getDefaultDisplay().getSize(size);
                int screenWidth = size.x;
                int screenHeight = size.y;
                double screenRatio = (double) screenWidth / (double) screenHeight;
                ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
                if (videoRatio > screenRatio) {
                    lp.width = screenWidth;
                    lp.height = (int) ((double) screenWidth / videoRatio);
                } else {
                    lp.width = (int) (videoRatio * (double) screenHeight);
                    lp.height = screenHeight;

                }
                surfaceView.setLayoutParams(lp);
                mediaPlayer.setDisplay(surfaceView.getHolder());
                mediaPlayer.start();
                mediaPlayerDuration = mediaPlayer.getDuration();
                mediaControllerVideo.seekBarOperations();


            }
        });


        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                double videoRatio = (double) mediaPlayer.getVideoWidth() / (double) mediaPlayer.getVideoHeight();
                Point size = new Point();
                getWindowManager().getDefaultDisplay().getSize(size);
                int screenWidth = size.x;
                int screenHeight = size.y;
                double screenRatio = (double) screenWidth / (double) screenHeight;
                ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
                if (videoRatio > screenRatio) {
                    lp.width = screenWidth;
                    lp.height = (int) ((double) screenWidth / videoRatio);
                } else {
                    lp.width = (int) (videoRatio * (double) screenHeight);
                    lp.height = screenHeight;

                }
                surfaceView.setLayoutParams(lp);
                mediaPlayer.setDisplay(surfaceView.getHolder());
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();

            }
        });


        //listen for seekBar and media initialization
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean f = true;

                while (true) {
                    if (mediaPlayer.isPlaying() && f) {
                        f = false;
                        favouriteMomentsVideo.databaseGetFavouritesOperation(id);
                        Log.d("sss", "sss");
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
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setSeekBarProperties();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            double videoRatio = (double) mediaPlayer.getVideoWidth() / (double) mediaPlayer.getVideoHeight();
            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;
            double screenRatio = (double) screenWidth / (double) screenHeight;
            ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
            if (videoRatio > screenRatio) {
                lp.width = screenWidth;
                lp.height = (int) ((double) screenWidth / videoRatio);
            } else {
                lp.width = (int) (videoRatio * (double) screenHeight);
                lp.height = screenHeight;

            }
            surfaceView.setLayoutParams(lp);
            mediaPlayer.setDisplay(surfaceView.getHolder());


        }


    }

    private void initializeViews() {
        seekBarVideo = findViewById(R.id.seekBar);
        currentPositionTextViewVideo = findViewById(R.id.current_position);
        maxLengthTextViewVideo = findViewById(R.id.max_length);
        nextFavButtonVideo = findViewById(R.id.next_fav);
        previousButtonVideo = findViewById(R.id.back);
        playPauseVideo = findViewById(R.id.play_or_pause);
        favButtonVideo = findViewById(R.id.fav);
        favButtonVideo.setOnLongClickListener(this);
        previousButtonVideo.setOnLongClickListener(this);
        nextFavButtonVideo.setOnLongClickListener(this);
        playPauseVideo.setOnLongClickListener(this);

        favButtonVideo.setOnClickListener(this);
        previousButtonVideo.setOnClickListener(this);
        nextFavButtonVideo.setOnClickListener(this);
        playPauseVideo.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.next_fav:
                favouriteMomentsVideo.nextFavouriteMomentOperation(id, mediaPlayer.getCurrentPosition());
                break;


            case R.id.back:
                favouriteMomentsVideo.previousFavouriteMomentOperation(id, mediaPlayer.getCurrentPosition(), false);
                break;


            case R.id.play_or_pause:
                playPausePress();
                break;

            case R.id.fav:
                favouriteMomentsVideo.addFavouriteMomentsOperation(id, mediaPlayer.getCurrentPosition());
                break;

            default:
                break;


        }


    }

    private void playPausePress() {
        if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {
                playPauseVideo.setImageResource(R.mipmap.final_play);
                mediaPlayer.pause();
            } else {
                playPauseVideo.setImageResource(R.mipmap.final_pause);
                mediaPlayer.start();
            }
        }

    }


    @Override
    public void onFavouriteMomentsOperationsCompleted(FavouriteMoments favouriteMoments) {

        if (favouriteMoments.getMediaType() == Context.MEDIATYPE.VIDEO) {
            Log.d("here", "there");
            favouriteMomentsVideo = favouriteMoments;
            mFavouritesPositionsList = favouriteMomentsVideo.getList();
        }


    }

    private void setSeekBarProperties() {


        MainActivity.seekBarMax = seekBarVideo.getMax();
        seekBarVideo.setFavouritesPositionsList(favouriteMomentsVideo.getList());
        seekBarVideo.setFavouriteBitmap(R.mipmap.final_just_heart);
        seekBarVideo.post(new Runnable() {
            @Override
            public void run() {
                MainActivity.seekBarWidth = seekBarVideo.getMeasuredWidth() - seekBarVideo.getPaddingStart() - seekBarVideo.getPaddingEnd();

            }
        });

    }

    @SuppressLint("NewApi")
    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {

            case R.id.main_next:

            case R.id.next_fav:
                //nextSong();
                break;

            case R.id.fav:
                favouriteMomentsVideo.databaseDeleteOperation(id);
                break;

            case R.id.back:

            case R.id.main_back:
                favouriteMomentsVideo.previousFavouriteMomentOperation(id, mediaPlayer.getCurrentPosition(), true);
                break;

            default:
                break;

        }

        return true;
    }
}