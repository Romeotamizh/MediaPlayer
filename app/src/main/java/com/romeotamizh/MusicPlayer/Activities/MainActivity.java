package com.romeotamizh.MusicPlayer.Activities;

import android.Manifest;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.romeotamizh.MusicPlayer.Helpers.SeekbarWithFavourites;
import com.romeotamizh.MusicPlayer.PlayMusic;
import com.romeotamizh.MusicPlayer.R;
import com.romeotamizh.MusicPlayer.RecyclerViewAdapter;
import com.romeotamizh.MusicPlayer.SeekBarWithFavouritesHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.addFavouriteMomentsOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.databaseDeleteOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.databaseGetFavouritesOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.nextFavouriteMomentOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.previousFavouriteMomentOperation;
import static com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMomentsRepository.resetFavouritesOperation;
import static com.romeotamizh.MusicPlayer.Helpers.SetAlphabetImages.setAlphabetImages;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayerDuration;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, SlidingUpPanelLayout.PanelSlideListener, PlayMusicListener.PlayMusicListenerInterface, MediaPlayer.OnCompletionListener {

    private static final String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    public static boolean isFirstTime = true;
    public static boolean isFromMainActivity;
    public static String mData;
    public static String mTitle;
    public static int mId;
    public static OnReturnListener onReturnListener;
    public static PlayMusicListener playMusicListener;
    public static int[] mFavouriteMomentsList = new int[100];
    public static boolean isFavouriteMomentsExist = false;
    public static int mFavouriteMomentsCount = 1;
    public static int seekBarMax;
    public static int seekBarWidth;
    public static boolean isSlideCollapsed = true;
    public static boolean isSongChanged = false;
    public static boolean isPreviousButtonLongPressed = false;
    public static boolean isBackPressed = false;
    public static boolean isSlideExpanded = false;
    public static boolean isMediaPlayerCompleted = false;
    public ImageView playPauseMain;
    View childLayout;
    SeekbarWithFavourites seekBarMain;
    FrameLayout parentLayout;
    TextView titleTextViewMain;
    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView currentPositionTextViewMain;
    TextView maxLengthTextViewMain;
    SeekBarWithFavouritesHelper seekBarWithFavouritesHelperMain;
    SlidingUpPanelLayout slidingUpPanelLayout;
    SeekbarWithFavourites seekBarPlay;
    View childLayoutPlay;
    ImageView playPausePlay;
    ImageView imageView;
    TextView titleTextViewPlay;
    TextView currentPositionTextViewPlay;
    TextView maxLengthTextViewPlay;
    ImageView favButton;
    ImageView nextFavButton;
    ImageView previousButton;
    SeekBarWithFavouritesHelper seekBarWithFavouritesHelperPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check permissions
        checkPermissions();


        //initialize views
        initializeViews();

        //initialize seekBarWithFavouritesHelper
        seekBarWithFavouritesHelperMain = new SeekBarWithFavouritesHelper(seekBarMain, currentPositionTextViewMain, maxLengthTextViewMain, playPauseMain, "main");


        //initialize seekBarWithFavouritesHelper
        seekBarWithFavouritesHelperPlay = new SeekBarWithFavouritesHelper(seekBarPlay, currentPositionTextViewPlay, maxLengthTextViewPlay, playPausePlay, "play");

        currentPositionTextViewMain.setTextColor(getResources().getColor(R.color.White, getTheme()));
        maxLengthTextViewMain.setTextColor(getResources().getColor(R.color.White, getTheme()));

        //add music
        addMusic();

        //seekBar properties
        setSeekBarProperties();


        //playMusicListener
        playMusicListener = new PlayMusicListener();
        playMusicListener.setListener(this);


        // set buttons onClick and onLongClick
        setButtonsClickFunctions();


        if (!isFirstTime) {
            parentLayout.setVisibility(View.VISIBLE);

        }


        setSupportActionBar(toolbar);

        mediaPlayer.setOnCompletionListener(this);


    }

    void checkPermissions() {
        TedPermission tedPermission = new TedPermission(getBaseContext());
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        };
        tedPermission.setPermissionListener(permissionListener).setDeniedMessage("Must Accept Permissions").setPermissions(permissions).check();

    }

    private void initializeViews() {


        LayoutInflater inflaterPlay = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);


        FrameLayout parentLayout2 = findViewById(R.id.play_screen_frame);
        childLayoutPlay = inflaterPlay.inflate(R.layout.activity_play_screen, (ViewGroup) findViewById(R.id.parent_linear));
        parentLayout2.addView(childLayoutPlay);
        childLayout = inflater.inflate(R.layout.layout_linear_controls, (ViewGroup) childLayoutPlay.findViewById(R.id.parent_layout_linear_controls));
        parentLayout = childLayoutPlay.findViewById(R.id.activity_main_seekBar_frame);


        titleTextViewPlay = childLayoutPlay.findViewById(R.id.title_play_screen);
        imageView = childLayoutPlay.findViewById(R.id.image_view);
        currentPositionTextViewPlay = childLayoutPlay.findViewById(R.id.current_position);
        maxLengthTextViewPlay = childLayoutPlay.findViewById(R.id.max_length);
        seekBarPlay = childLayoutPlay.findViewById(R.id.seekBar);
        nextFavButton = childLayoutPlay.findViewById(R.id.next_fav);
        favButton = childLayoutPlay.findViewById(R.id.fav);
        previousButton = childLayoutPlay.findViewById(R.id.back);
        playPausePlay = childLayoutPlay.findViewById(R.id.play_or_pause);

        parentLayout.addView(childLayout);


        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        toolbar = findViewById(R.id.toolbar_main);
        recyclerView = findViewById(R.id.recyclerView);
        playPauseMain = childLayout.findViewById(R.id.main_play_or_pause);
        seekBarMain = childLayout.findViewById(R.id.seekBar);
        maxLengthTextViewMain = childLayout.findViewById(R.id.max_length);
        currentPositionTextViewMain = childLayout.findViewById(R.id.current_position);
        titleTextViewMain = childLayout.findViewById(R.id.title_linear_screen);


        slidingUpPanelLayout.addPanelSlideListener(this);


    }

    public void setSeekBarProperties() {

        seekBarMain.setmFavouriteBitmap(R.mipmap.red_play);
        seekBarMain.setmFavouritesPositionsList(mFavouriteMomentsList);
        seekBarMax = seekBarPlay.getMax();
        seekBarPlay.setmFavouritesPositionsList(mFavouriteMomentsList);
        seekBarPlay.setmFavouriteBitmap(R.mipmap.red_play);
        seekBarPlay.post(new Runnable() {
            @Override
            public void run() {
                seekBarWidth = seekBarPlay.getMeasuredWidth() - seekBarPlay.getPaddingStart() - seekBarPlay.getPaddingEnd();


            }
        });

    }

    private void setButtonsClickFunctions() {


        playPauseMain.setOnClickListener(this);
        nextFavButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        previousButton.setOnLongClickListener(this);
        favButton.setOnClickListener(this);
        favButton.setOnLongClickListener(this);
        playPausePlay.setOnClickListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addMusic() {
        ArrayList<String> mTitleList = new ArrayList<>();
        ArrayList<Integer> mDurationList = new ArrayList<>();
        ArrayList<Integer> mIdList = new ArrayList<>();
        ArrayList<String> mDataList = new ArrayList<>();
        Cursor cursor;
        Cursor cursorAlbum;
//        cursorAlbum = getApplicationContext().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,null,null,null,"upper("+MediaStore.Audio.Albums.ALBUM_ID);


        cursor = getApplicationContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, "upper(" + MediaStore.Audio.Media.DISPLAY_NAME + ")ASC");
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                mTitleList.add(cursor.getString(cursor.getColumnIndex("_display_name")));
                mDurationList.add(cursor.getInt(cursor.getColumnIndex("duration")));
                mDataList.add(cursor.getString(cursor.getColumnIndex("_data")));
                mIdList.add(cursor.getInt(cursor.getColumnIndex("_id")));
                //musicFilesUri.add(ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,cursor.getInt(cursor.getColumnIndex("_id"))));

            }
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.d("Column names", cursor.getColumnName(i));

            }
            //cursor.moveToPosition(2);

            // Log.d("data", cursor.getString(cursor.getColumnIndex("track")));


            cursor.close();
            initializeRecyclerView(mTitleList, mDurationList, mDataList, mIdList);
        }


    }

    void initializeRecyclerView(ArrayList<String> mTitleList, ArrayList<Integer> mDurationList, ArrayList<String> mDataList, ArrayList<Integer> mIdlist) {
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mTitleList, mDurationList, mDataList, mIdlist, getBaseContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void playMusic() {


        //play music
        PlayMusic.playMusic(mData, mTitle);


        //listen for seekBar and media changes
        listenerFunction();


        titleTextViewPlay.setText(mTitle);
        imageView.setImageResource(setAlphabetImages(mTitle));

        titleTextViewMain.setText(mTitle);
        if (!isFirstTime) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    parentLayout.setVisibility(View.VISIBLE);
                    Log.d("pp", "ppp");

                }
            });
        }


        Log.d("jjjlist", "list");

    }

    private void listenerFunction() {
        isSongChanged = false;

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    if (mediaPlayer.isPlaying() || isSongChanged) {

                        resetFavouritesOperation("music");
                        databaseGetFavouritesOperation(mId, "music");


                        seekBarWithFavouritesHelperMain.seekBarOperations();
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
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.main_play_or_pause:
                playPausePress();
                break;


            case R.id.fav:
                addFavouriteMomentsOperation(mId, mFavouriteMomentsCount, mFavouriteMomentsList, isFavouriteMomentsExist, "music");
                break;

            case R.id.back:
                isPreviousButtonLongPressed = false;
                previousFavouriteMomentOperation(mId, mFavouriteMomentsCount, mFavouriteMomentsList, isFavouriteMomentsExist, isPreviousButtonLongPressed, seekBarPlay);

                break;

            case R.id.next_fav:
                nextFavouriteMomentOperation(mId, mFavouriteMomentsCount, mFavouriteMomentsList, isFavouriteMomentsExist, seekBarPlay);
                break;

            case R.id.play_or_pause:
                playPausePress();
                break;


            default:
                break;


        }

    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {

            case R.id.fav:
                databaseDeleteOperation(mId, "music");
                break;

            case R.id.back:
                isPreviousButtonLongPressed = true;
                previousFavouriteMomentOperation(mId, mFavouriteMomentsCount, mFavouriteMomentsList, isFavouriteMomentsExist, isPreviousButtonLongPressed, seekBarPlay);

                break;


            default:
                break;

        }

        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        isMediaPlayerCompleted = true;

        playPauseMain.setImageResource(R.mipmap.play);
        seekBarMain.setProgress(mediaPlayerDuration);
        playPausePlay.setImageResource(R.mipmap.play);
        seekBarPlay.setProgress(mediaPlayerDuration);


    }

    public void playPausePress() {
        if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {
                playPausePlay.setImageResource(R.mipmap.play);
                playPauseMain.setImageResource(R.mipmap.play);
                mediaPlayer.pause();
            } else {
                playPausePlay.setImageResource(R.mipmap.pause);
                playPauseMain.setImageResource(R.mipmap.pause);
                mediaPlayer.start();
            }
        }


    }

    @Override
    public void onPanelSlide(View view, float v) {

    }

    @Override
    public void onPanelStateChanged(View view, SlidingUpPanelLayout.PanelState panelState, SlidingUpPanelLayout.PanelState panelState1) {


        if (panelState == SlidingUpPanelLayout.PanelState.COLLAPSED && panelState1 == SlidingUpPanelLayout.PanelState.DRAGGING) {

            childLayout.setVisibility(View.GONE);
            isSlideCollapsed = false;
            isSlideExpanded = true;


            seekBarWithFavouritesHelperPlay.seekBarOperations();
            if (mediaPlayer.isPlaying())
                playPausePlay.setImageResource(R.mipmap.pause);
            else
                playPausePlay.setImageResource(R.mipmap.play);

        }
        if (panelState1 == SlidingUpPanelLayout.PanelState.COLLAPSED && panelState == SlidingUpPanelLayout.PanelState.DRAGGING) {

            childLayout.setVisibility(View.VISIBLE);
            isSlideCollapsed = true;
            isSlideExpanded = false;

            seekBarWithFavouritesHelperMain.seekBarOperations();
            if (mediaPlayer.isPlaying())
                playPauseMain.setImageResource(R.mipmap.pause);
            else
                playPauseMain.setImageResource(R.mipmap.play);


        }

    }


}



