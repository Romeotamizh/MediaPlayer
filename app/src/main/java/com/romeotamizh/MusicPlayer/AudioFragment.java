package com.romeotamizh.MusicPlayer;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.romeotamizh.MusicPlayer.Activities.MainActivity;
import com.romeotamizh.MusicPlayer.FavouriteMoments.FavouriteMoments;
import com.romeotamizh.MusicPlayer.Helpers.Context;
import com.romeotamizh.MusicPlayer.Helpers.MyRecyclerView;
import com.romeotamizh.MusicPlayer.Helpers.SeekBarWithFavourites;
import com.romeotamizh.MusicPlayer.Helpers.SeekBarWithFavouritesHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.romeotamizh.MusicPlayer.Helpers.SeekBarWithFavourites.mFavouritesPositionsList;
import static com.romeotamizh.MusicPlayer.Helpers.SetAlphabetImages.setAlphabetImages;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayerDuration;

public class AudioFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener, SeekBarWithFavourites.OnSeekBarProgressListener, FavouriteMoments.OnFavouriteMomentsOperationsListener, MenuItem.OnMenuItemClickListener, SlidingUpPanelLayout.PanelSlideListener, PlayMusic.PlayMusicListener, MediaPlayer.OnCompletionListener {


    public static String mData;
    public static String mTitle;
    public static int mId;
    RelativeLayout audioFragmentLayout;
    private ImageView playPauseMain;
    private View childLayout;
    private SeekBarWithFavourites seekBarMain;
    private TextView titleTextViewMain;
    private MyRecyclerView recyclerView;
    private TextView currentPositionTextViewMain;
    private TextView maxLengthTextViewMain;
    private ImageView nextFavButtonMain;
    private ImageView previousButtonMain;
    private SeekBarWithFavouritesHelper seekBarWithFavouritesHelperMain;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private SeekBarWithFavourites seekBarPlay;
    private ImageView playPausePlay;
    private ImageView imageView;
    private TextView titleTextViewPlay;
    private FavouriteMoments favouriteMomentsAudio;
    private TextView currentPositionTextViewPlay;
    private TextView maxLengthTextViewPlay;
    private ImageView favButtonPlay;
    private ImageView nextFavButtonPlay;
    private ImageView previousButtonPlay;
    private SeekBarWithFavouritesHelper seekBarWithFavouritesHelperPlay;
    private MenuItem videoMenuItem;
    private FavouriteMoments favouriteMomentsVideo;
    private ArrayList<String> mDataList;
    private ArrayList<String> mTitleList;
    private ArrayList<Integer> mIdList;
    private View rootView;


    public AudioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio, container, false);
    }


        /*@Override
        public void  onBackPressed() {
            if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            else
                super.getActivity().onBackPressed();
        }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
        populate();
    }

    private void populate() {
        //initialize views
        initializeViews();


        favouriteMomentsAudio = new FavouriteMoments(new int[100], 1, false, Context.MEDIATYPE.AUDIO);
        favouriteMomentsVideo = new FavouriteMoments(new int[100], 1, false, Context.MEDIATYPE.AUDIO);
        FavouriteMoments.setOnFavouriteMomentsOperationsListener(this);


        //initialize seekBarWithFavouritesHelper
        seekBarWithFavouritesHelperMain = new SeekBarWithFavouritesHelper(seekBarMain, currentPositionTextViewMain, maxLengthTextViewMain, playPauseMain, Context.CONTEXT.MAIN);

        //initialize seekBarWithFavouritesHelper
        seekBarWithFavouritesHelperPlay = new SeekBarWithFavouritesHelper(seekBarPlay, currentPositionTextViewPlay, maxLengthTextViewPlay, playPausePlay, Context.CONTEXT.PLAY);

        //alter mainScreen Views
        currentPositionTextViewMain.setTextColor(getResources().getColor(R.color.White, getActivity().getTheme()));
        maxLengthTextViewMain.setTextColor(getResources().getColor(R.color.White, getActivity().getTheme()));

        //add music
        addMedia(Context.MEDIATYPE.AUDIO);

        //playMusicListener
        PlayMusic.setPlayMusicListener(this);

        // set buttons onClick and onLongClick
        setButtonsClickFunctions();

        //set mediaPlayerCompletion listener
        mediaPlayer.setOnCompletionListener(this);

        //set slideUpPanel listener
        slidingUpPanelLayout.addPanelSlideListener(this);

        // videoMenuItem = findViewById(R.id.video);
        //  videoMenuItem.setOnMenuItemClickListener(this);

        //set SeekBar properties
        setSeekBarProperties();


        SeekBarWithFavourites.setSeekBarProgress(this);

            /*fragmentTransaction.detach(this);
            fragmentTransaction.attach(this);
            fragmentTransaction.commit();*/


    }

    @Override
    public void playMusic(int position) {
        mData = mDataList.get(position);
        mTitle = mTitleList.get(position);
        mId = mIdList.get(position);


        //play music
        PlayMusic.playMusic(mData, mTitle);


        //listen for seekBar and media changes
        listenerFunction();

        //set title text
        titleTextViewPlay.setText(mTitle);
        titleTextViewMain.setText(mTitle);

        //set title images
        imageView.setImageResource(setAlphabetImages(mTitle));


    }

    private void listenerFunction() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    if (mediaPlayer.isPlaying()) {
                        //databaseDeleteOperation(mId, favouriteMomentsAudio);
                        favouriteMomentsAudio.databaseGetFavouritesOperation(mId);
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

            case R.id.main_next:
                favouriteMomentsAudio.nextFavouriteMomentOperation(mId);
                break;

            case R.id.main_back:
                favouriteMomentsAudio.previousFavouriteMomentOperation(mId, false);
                break;

            case R.id.main_play_or_pause:
                playPausePress();
                break;

            case R.id.fav:
                favouriteMomentsAudio.addFavouriteMomentsOperation(mId);
                break;

            case R.id.back:
                favouriteMomentsAudio.previousFavouriteMomentOperation(mId, false);
                break;

            case R.id.next_fav:
                favouriteMomentsAudio.nextFavouriteMomentOperation(mId);
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
                favouriteMomentsAudio.databaseDeleteOperation(mId);
                break;

            case R.id.back:
                favouriteMomentsAudio.previousFavouriteMomentOperation(mId, true);
                break;

            case R.id.main_back:
                favouriteMomentsAudio.previousFavouriteMomentOperation(mId, true);
                break;

            default:
                break;

        }

        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
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
            MainActivity.isSlideCollapsed = false;
            MainActivity.isSlideExpanded = true;

            seekBarWithFavouritesHelperPlay.seekBarOperations();
            if (mediaPlayer.isPlaying())
                playPausePlay.setImageResource(R.mipmap.pause);
            else
                playPausePlay.setImageResource(R.mipmap.play);

        }


        if (panelState == SlidingUpPanelLayout.PanelState.DRAGGING && panelState1 == SlidingUpPanelLayout.PanelState.COLLAPSED) {

            childLayout.setVisibility(View.VISIBLE);
            MainActivity.isSlideCollapsed = true;
            MainActivity.isSlideExpanded = false;

            seekBarWithFavouritesHelperMain.seekBarOperations();
            if (mediaPlayer.isPlaying())
                playPauseMain.setImageResource(R.mipmap.pause);
            else
                playPauseMain.setImageResource(R.mipmap.play);

        }


    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return true;
    }

    @Override
    public void onFavouriteMomentsOperationsCompleted(FavouriteMoments favouriteMoments) {

        if (favouriteMoments.getMediaType() == Context.MEDIATYPE.AUDIO) {
            favouriteMomentsAudio = favouriteMoments;
            Log.d("jjj", Arrays.toString(favouriteMomentsAudio.getList()));
            Log.d("jjj", Arrays.toString(favouriteMomentsAudio.getList()));
            mFavouritesPositionsList = favouriteMomentsAudio.getList();
        }

        if (favouriteMoments.getMediaType() == Context.MEDIATYPE.VIDEO) {

            Log.d("video", "vifeo");
        }


    }

    @Override
    public void onSeekBarProgressChange(int progress) {
        seekBarMain.setProgress(progress);
        seekBarPlay.setProgress(progress);
    }

    private void initializeViews() {


        //initialize common Views
        slidingUpPanelLayout = rootView.findViewById(R.id.sliding_layout);
        recyclerView = rootView.findViewById(R.id.recyclerView);


        //inflate playScreen
        LayoutInflater inflaterPlay = (LayoutInflater) this.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        FrameLayout parentLayout2 = rootView.findViewById(R.id.play_screen_frame);
        View childLayoutPlay = inflaterPlay.inflate(R.layout.activity_play_screen, (ViewGroup) rootView.findViewById(R.id.parent_linear));

        //add playScreen to mainScreen
        parentLayout2.addView(childLayoutPlay);

        //inflate seekBar
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        childLayout = inflater.inflate(R.layout.layout_linear_controls, (ViewGroup) childLayoutPlay.findViewById(R.id.parent_layout_linear_controls));
        FrameLayout parentLayout = childLayoutPlay.findViewById(R.id.activity_main_seekBar_frame);

        //initialize playScreen Views
        {
            titleTextViewPlay = childLayoutPlay.findViewById(R.id.title_play_screen);
            imageView = childLayoutPlay.findViewById(R.id.image_view);
            currentPositionTextViewPlay = childLayoutPlay.findViewById(R.id.current_position);
            maxLengthTextViewPlay = childLayoutPlay.findViewById(R.id.max_length);
            seekBarPlay = childLayoutPlay.findViewById(R.id.seekBar);
            nextFavButtonPlay = childLayoutPlay.findViewById(R.id.next_fav);
            favButtonPlay = childLayoutPlay.findViewById(R.id.fav);
            previousButtonPlay = childLayoutPlay.findViewById(R.id.back);
            playPausePlay = childLayoutPlay.findViewById(R.id.play_or_pause);
        }

        //initialize add seekBar to playScreen
        parentLayout.addView(childLayout);

        //initialize mainScreen Views
        {
            nextFavButtonMain = childLayout.findViewById(R.id.main_next);
            previousButtonMain = childLayout.findViewById(R.id.main_back);
            playPauseMain = childLayout.findViewById(R.id.main_play_or_pause);
            seekBarMain = childLayout.findViewById(R.id.seekBar);
            maxLengthTextViewMain = childLayout.findViewById(R.id.max_length);
            currentPositionTextViewMain = childLayout.findViewById(R.id.current_position);
            titleTextViewMain = childLayout.findViewById(R.id.title_linear_screen);
        }


    }

    public void setSeekBarProperties() {

        seekBarMain.setFavouriteBitmap(R.mipmap.red_play);
        seekBarMain.setFavouritesPositionsList(favouriteMomentsAudio.getList());
        MainActivity.seekBarMax = seekBarPlay.getMax();
        seekBarPlay.setFavouritesPositionsList(favouriteMomentsAudio.getList());
        seekBarPlay.setFavouriteBitmap(R.mipmap.red_play);
        seekBarPlay.post(new Runnable() {
            @Override
            public void run() {
                MainActivity.seekBarWidth = seekBarPlay.getMeasuredWidth() - seekBarPlay.getPaddingStart() - seekBarPlay.getPaddingEnd();


            }
        });

    }

    private void setButtonsClickFunctions() {

        {
            nextFavButtonMain.setOnClickListener(this);
            previousButtonMain.setOnClickListener(this);
            playPauseMain.setOnClickListener(this);
            previousButtonMain.setOnLongClickListener(this);
        }


        {
            nextFavButtonPlay.setOnClickListener(this);
            previousButtonPlay.setOnClickListener(this);
            favButtonPlay.setOnClickListener(this);
            playPausePlay.setOnClickListener(this);
            previousButtonPlay.setOnLongClickListener(this);
            favButtonPlay.setOnLongClickListener(this);
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addMedia(Context.MEDIATYPE mediaType) {
        mTitleList = new ArrayList<>();
        ArrayList<Integer> mDurationList = new ArrayList<>();
        mIdList = new ArrayList<>();
        mDataList = new ArrayList<>();
        Cursor cursor;
        //   Cursor cursorAlbum;
//        cursorAlbum = getApplicationContext().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,null,null,null,"upper("+MediaStore.Audio.Albums.ALBUM_ID);

        if (mediaType == Context.MEDIATYPE.AUDIO)
            cursor = getActivity().getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, "upper(" + MediaStore.Audio.Media.DISPLAY_NAME + ")ASC");
        else
            cursor = getActivity().getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, "upper(" + MediaStore.Video.Media.DISPLAY_NAME + ")ASC");

        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                mTitleList.add(cursor.getString(cursor.getColumnIndex("_display_name")));
                mDurationList.add(cursor.getInt(cursor.getColumnIndex("duration")));
                mDataList.add(cursor.getString(cursor.getColumnIndex("_data")));
                mIdList.add(cursor.getInt(cursor.getColumnIndex("_id")));

            }
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.d("Column names", cursor.getColumnName(i));

            }
            //cursor.moveToPosition(2);

            // Log.d("data", cursor.getString(cursor.getColumnIndex("track")));


            cursor.close();


            initializeRecyclerView(mTitleList, mDurationList, mDataList, mIdList, mediaType);
        }


    }

    void initializeRecyclerView(ArrayList<String> mTitleList, ArrayList<Integer> mDurationList, ArrayList<String> mDataList, ArrayList<Integer> mIdlist, Context.MEDIATYPE mediaType) {
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mTitleList, mDurationList, mDataList, mIdlist, this.getContext(), mediaType);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

}


