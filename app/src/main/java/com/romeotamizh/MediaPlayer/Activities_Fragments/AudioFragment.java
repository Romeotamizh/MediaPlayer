package com.romeotamizh.MediaPlayer.Activities_Fragments;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.romeotamizh.MediaPlayer.Adapters.RecyclerViewAdapter;
import com.romeotamizh.MediaPlayer.Helpers.Context;
import com.romeotamizh.MediaPlayer.Helpers.CustomRecyclerView;
import com.romeotamizh.MediaPlayer.Helpers.CustomSeekBar;
import com.romeotamizh.MediaPlayer.Helpers.FavouriteMoments;
import com.romeotamizh.MediaPlayer.Helpers.MediaInfoDatabase;
import com.romeotamizh.MediaPlayer.MediaController.MediaController;
import com.romeotamizh.MediaPlayer.MediaController.PlayMedia;
import com.romeotamizh.MediaPlayer.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.romeotamizh.MediaPlayer.Activities_Fragments.MainActivity.groupByAudio;
import static com.romeotamizh.MediaPlayer.Activities_Fragments.MainActivity.isExitAudio;
import static com.romeotamizh.MediaPlayer.Activities_Fragments.MainActivity.isFirstTime;
import static com.romeotamizh.MediaPlayer.Activities_Fragments.MainActivity.isSongChanged;
import static com.romeotamizh.MediaPlayer.Helpers.CustomSeekBar.mFavouritesPositionsList;
import static com.romeotamizh.MediaPlayer.Helpers.Thumbnail.setThumbnailImage;
import static com.romeotamizh.MediaPlayer.MediaController.PlayMedia.mediaPlayer;
import static com.romeotamizh.MediaPlayer.MediaController.PlayMedia.mediaPlayerDuration;

public class AudioFragment extends Fragment implements Context.SetFragmentOnBackPressedListener, View.OnClickListener, Context.SetFragmentOnOptionsMenuClickedListener, View.OnLongClickListener, FavouriteMoments.OnFavouriteMomentsOperationsListener, SlidingUpPanelLayout.PanelSlideListener, PlayMedia.OnPlayMediaListener, MediaPlayer.OnCompletionListener {


    private MediaInfoDatabase audioInfoDatabase;
    private View childLayout;
    private int id;
    private FavouriteMoments favouriteMomentsAudio;
    private View rootView;
    private MediaInfoDatabase.TrackInfo trackInfo;
    private int height = 260, width = 400;
    private SlidingUpPanelLayout slidingUpPanelLayout;


    //mainViews
    private CustomRecyclerView recyclerViewAudioMain;
    private TextView titleTextViewMain;
    private TextView currentPositionTextViewMain;
    private TextView maxLengthTextViewMain;
    private ImageView playPauseMain;
    private ImageView nextFavButtonMain;
    private ImageView previousButtonMain;
    private ImageView backGroundMain;
    private CustomSeekBar seekBarMain;
    private MediaController mediaControllerMain;

    //playViews
    private RecyclerView recyclerViewAudioPlay;
    private TextView titleTextViewPlay;
    private TextView currentPositionTextViewPlay;
    private TextView maxLengthTextViewPlay;
    private ImageView playPausePlay;
    private ImageView nextFavButtonPlay;
    private ImageView previousButtonPlay;
    private ImageView favButtonPlay;
    private ImageView imageViewPlay;
    private ImageView backGroundPlay;
    private CustomSeekBar seekBarPlay;
    private MediaController mediaControllerPlay;
    public static boolean isReturnFromVideo = false;
    public static int leftPosition;


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

    private int prevId;

    public static ArrayList<Bitmap> tAudio;
    public static int ls;
    public static Uri previousAudioPlayed;
    RecyclerViewAdapter recyclerViewAdapter = null;

    private void setButtonsClickFunctions() {

        {
            nextFavButtonMain.setOnClickListener(this);
            nextFavButtonMain.setOnLongClickListener(this);
            previousButtonMain.setOnClickListener(this);
            playPauseMain.setOnClickListener(this);
            previousButtonMain.setOnLongClickListener(this);
        }


        {
            nextFavButtonPlay.setOnClickListener(this);
            nextFavButtonPlay.setOnLongClickListener(this);
            previousButtonPlay.setOnClickListener(this);
            favButtonPlay.setOnClickListener(this);
            playPausePlay.setOnClickListener(this);
            previousButtonPlay.setOnLongClickListener(this);
            favButtonPlay.setOnLongClickListener(this);
        }


    }

    private void playPausePress() {
        if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {
                playPausePlay.setImageResource(R.mipmap.final_play);
                playPauseMain.setImageResource(R.mipmap.final_play);
                mediaPlayer.pause();
            } else {
                playPausePlay.setImageResource(R.mipmap.final_pause);
                playPauseMain.setImageResource(R.mipmap.final_pause);
                mediaPlayer.start();
            }
        }


    }

    private void setSeekBarProperties() {

        seekBarMain.setFavouriteBitmap(R.mipmap.final_just_heart);
        seekBarMain.setFavouritesPositionsList(favouriteMomentsAudio.getList());
        MainActivity.seekBarMax = seekBarPlay.getMax();
        seekBarPlay.setFavouritesPositionsList(favouriteMomentsAudio.getList());
        seekBarPlay.setFavouriteBitmap(R.mipmap.final_just_heart);
        seekBarPlay.post(new Runnable() {
            @Override
            public void run() {
                MainActivity.seekBarWidth = seekBarPlay.getMeasuredWidth() - seekBarPlay.getPaddingStart() - seekBarPlay.getPaddingEnd();

            }
        });

    }

    ArrayList<CharSequence> albumTitleList = null;

    @Override
    public void onBackPressed() {
        Log.d("mom", "mom");
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED || isFirstTime)
            isExitAudio = true;
        else {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            isExitAudio = false;
        }

    }

    ArrayList<Integer> i = new ArrayList<>();
    ArrayList<Bitmap> b = new ArrayList<>();
    Size size = new Size(200, 200);
    private ImageView backGroundAccentColor;
    private RecyclerViewAdapter recyclerViewAdapterPlay = null;


    @Override
    public void playMedia(final int id, Context.MEDIATYPE mediaType) {


        if (mediaType == Context.MEDIATYPE.AUDIO) {
            trackInfo = audioInfoDatabase.getTrackInfo(id, mediaType);
            if (id != 0) {
                play(id);
            }
        }
    }

    @Override
    public void onResume() {

        super.onResume();
        if (mediaPlayer.isPlaying())
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);


    }

    private void initializeViews() {


        //initialize common Views
        slidingUpPanelLayout = rootView.findViewById(R.id.sliding_layout);
        recyclerViewAudioMain = rootView.findViewById(R.id.recyclerView);


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
            imageViewPlay = childLayoutPlay.findViewById(R.id.circle_image_view_play);
            backGroundPlay = childLayoutPlay.findViewById(R.id.bg_image);
            recyclerViewAudioPlay = childLayoutPlay.findViewById(R.id.recyclerView_play);
            currentPositionTextViewPlay = childLayoutPlay.findViewById(R.id.current_position);
            maxLengthTextViewPlay = childLayoutPlay.findViewById(R.id.max_length);
            seekBarPlay = childLayoutPlay.findViewById(R.id.seekBar);
            nextFavButtonPlay = childLayoutPlay.findViewById(R.id.next_fav);
            favButtonPlay = childLayoutPlay.findViewById(R.id.fav_play);
            previousButtonPlay = childLayoutPlay.findViewById(R.id.back);
            playPausePlay = childLayoutPlay.findViewById(R.id.play_or_pause);
            backGroundAccentColor = childLayoutPlay.findViewById(R.id.theme_bg);
        }

        //initialize add seekBar to playScreen
        parentLayout.addView(childLayout);

        //initialize mainScreen Views
        {
            nextFavButtonMain = childLayout.findViewById(R.id.main_next);
            previousButtonMain = childLayout.findViewById(R.id.main_back);
            playPauseMain = childLayout.findViewById(R.id.main_play_or_pause);
            seekBarMain = childLayout.findViewById(R.id.seekBar);
            backGroundMain = childLayout.findViewById(R.id.bg_image);
            maxLengthTextViewMain = childLayout.findViewById(R.id.max_length);
            currentPositionTextViewMain = childLayout.findViewById(R.id.current_position);
            titleTextViewMain = childLayout.findViewById(R.id.title_linear_screen);
        }


    }

    private void populate() {
        //initialize views
        initializeViews();

        favouriteMomentsAudio = new FavouriteMoments(new int[100], 1, false, Context.MEDIATYPE.AUDIO);
        FavouriteMoments.setOnFavouriteMomentsOperationsListener(this);

        //initialize seekBarWithFavouritesHelper
        mediaControllerMain = new MediaController(seekBarMain, currentPositionTextViewMain, maxLengthTextViewMain, playPauseMain, Context.CONTEXT.MAIN);

        //initialize seekBarWithFavouritesHelper
        mediaControllerPlay = new MediaController(seekBarPlay, currentPositionTextViewPlay, maxLengthTextViewPlay, playPausePlay, Context.CONTEXT.PLAY);

        //alter playScreen Views
        // currentPositionTextViewPlay.setTextColor(getResources().getColor(R.color.Black, getActivity().getTheme()));
        //maxLengthTextViewPlay.setTextColor(getResources().getColor(R.color.Black, getActivity().getTheme()));

        //add music
        audioInfoDatabase = new MediaInfoDatabase(Context.MEDIATYPE.AUDIO);

        //initialize recyclerViewAudioMain
        initializeRecyclerView(audioInfoDatabase);

        //recyclerViewAdapter.

        //playMusicListener
        PlayMedia.setPlayMediaListener(this);

        // set buttons onClick and onLongClick
        setButtonsClickFunctions();

        //set mediaPlayerCompletion listener
        mediaPlayer.setOnCompletionListener(this);

        //set slideUpPanel listener
        slidingUpPanelLayout.addPanelSlideListener(this);

        //set SeekBar properties
        setSeekBarProperties();

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        isFirstTime = true;

        slidingUpPanelLayout.setDragView(backGroundMain);

        Context.setOnBackPressed(this);
        Context.setOnOptionsSelected(this);
    }

    private void initializeRecyclerView(MediaInfoDatabase mediaInfoDatabase) {

        if (recyclerViewAdapterPlay == null) {

            recyclerViewAdapterPlay = new RecyclerViewAdapter(audioInfoDatabase, Context.CONTEXT.PLAY, this.getContext());
            recyclerViewAudioPlay.setAdapter(recyclerViewAdapterPlay);
            recyclerViewAudioPlay.setLayoutManager(new LinearLayoutManager(this.getContext()));

        }




        if (recyclerViewAdapter == null) {
            recyclerViewAdapter = new RecyclerViewAdapter(audioInfoDatabase, Context.CONTEXT.MAIN, getActivity());
            albumTitleList = mediaInfoDatabase.getAlbumTitleList();
            recyclerViewAudioMain.setAdapter(recyclerViewAdapter);
            for (CharSequence album : albumTitleList) {
                int firstId = mediaInfoDatabase.getIdsInAlbum(album).get(0);
                i.add(firstId);
                b.add(setThumbnailImage(mediaInfoDatabase.getUriById(firstId), size, album));
            }

        }

        if (groupByAudio == Context.GROUPBY.ALBUM) {
            recyclerViewAudioMain.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            recyclerViewAdapter.setTitleList(albumTitleList);
            recyclerViewAdapter.setListSize(albumTitleList.size());
            recyclerViewAdapter.setIdList(i);
            recyclerViewAdapter.setThumbs(b);
            recyclerViewAudioMain.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();


        }
        if (groupByAudio == Context.GROUPBY.NOTHING) {
            recyclerViewAudioMain.setLayoutManager(new LinearLayoutManager(this.getContext()));
            recyclerViewAdapter.setTitleList(mediaInfoDatabase.getTitleList());
            recyclerViewAdapter.setIdList(mediaInfoDatabase.getIdList());
            recyclerViewAdapter.setDurationList(mediaInfoDatabase.getDurationList());
            recyclerViewAdapter.setExtensionList(mediaInfoDatabase.getExtensionList());
            recyclerViewAdapter.setListSize(mediaInfoDatabase.getMediaCount());
            recyclerViewAdapter.setThumbs(tAudio);
            recyclerViewAudioMain.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();


        }


    }

    private void play(final int id) {
        this.id = id;
        setSeekBarProperties();
        Uri uri = audioInfoDatabase.getUriById(id);
        CharSequence mTitle = audioInfoDatabase.getTitleById(id);
        PlayMedia.callBack(id, uri, mTitle, Context.MEDIATYPE.AUDIO);


        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN)
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);


        PlayMedia.playMedia(uri, Context.MEDIATYPE.AUDIO);

        //listen for seekBar and media initialization
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean f = true;

                while (true) {
                    if (mediaPlayer.isPlaying() && f) {
                        f = false;
                        isSongChanged = false;
                        favouriteMomentsAudio.databaseGetFavouritesOperation(id);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED)
                                    mediaControllerMain.seekBarOperations();
                                else
                                    mediaControllerPlay.seekBarOperations();
                            }
                        });


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

        {   //set title text
            titleTextViewPlay.setText(mTitle);
            titleTextViewMain.setText(mTitle);

            backGroundPlay.post(new Runnable() {
                @Override
                public void run() {
                    height = backGroundPlay.getMeasuredHeight();
                    width = backGroundPlay.getMeasuredHeight();

                }
            });

            //set title images
            Bitmap bitmapPlay = setThumbnailImage(uri, new Size(width, height), null);
            Bitmap bitmapMain = setThumbnailImage(uri, new Size(width, 100), null);
            int whiteColor = getResources().getColor(R.color.White, null);
            int accentColor = 0;

            if (bitmapPlay != null)
                accentColor = Palette.from(bitmapPlay).generate().getLightVibrantColor(whiteColor);

            //set Background images and tint
            if (bitmapPlay == null) {
                backGroundPlay.setImageResource(R.mipmap.wallpaper);
                backGroundAccentColor.setBackgroundColor(getResources().getColor(R.color.Black, null));
                nextFavButtonPlay.setColorFilter(whiteColor);
                titleTextViewPlay.setTextColor(whiteColor);
                previousButtonPlay.setColorFilter(whiteColor);
                currentPositionTextViewPlay.setTextColor(whiteColor);
                maxLengthTextViewPlay.setTextColor(whiteColor);
                seekBarPlay.setProgressTintList(ColorStateList.valueOf(whiteColor));

            } else {


                backGroundAccentColor.setBackgroundColor(Palette.from(bitmapPlay).generate().getDominantColor(getResources().getColor(R.color.Black, null)));
                nextFavButtonPlay.setColorFilter(accentColor);
                previousButtonPlay.setColorFilter(accentColor);
                titleTextViewPlay.setTextColor(accentColor);
                maxLengthTextViewPlay.setTextColor(accentColor);
                currentPositionTextViewPlay.setTextColor(accentColor);
                seekBarPlay.setProgressTintList(ColorStateList.valueOf(accentColor));
                backGroundPlay.setImageBitmap(bitmapPlay);
            }
            imageViewPlay.setVisibility(View.GONE);


            if (bitmapMain == null) {
                titleTextViewMain.setTextColor(whiteColor);
                nextFavButtonMain.setColorFilter(whiteColor);
                previousButtonMain.setColorFilter(whiteColor);
                currentPositionTextViewMain.setTextColor(whiteColor);
                maxLengthTextViewMain.setTextColor(whiteColor);
                seekBarMain.setProgressTintList(ColorStateList.valueOf(whiteColor));
                backGroundMain.setImageResource(R.mipmap.wallpaper);

            } else {
                titleTextViewMain.setTextColor(accentColor);
                currentPositionTextViewMain.setTextColor(accentColor);
                maxLengthTextViewMain.setTextColor(accentColor);
                nextFavButtonMain.setColorFilter(accentColor);
                previousButtonMain.setColorFilter(accentColor);
                seekBarMain.setProgressTintList(ColorStateList.valueOf(accentColor));
                backGroundMain.setImageBitmap(bitmapMain);

            }

        }

        isFirstTime = false;

        previousAudioPlayed = uri;

        if (!trackInfo.getIdsInAlbum().contains(prevId)) {

            ArrayList<Bitmap> th = new ArrayList<>();
            ArrayList<Integer> id_ = trackInfo.getIdsInAlbum();
            ArrayList<CharSequence> d = new ArrayList<>();
            ArrayList<CharSequence> t = new ArrayList<>();
            ArrayList<CharSequence> e = new ArrayList<>();
            for (int i : id_) {
                th.add(tAudio.get(audioInfoDatabase.getIdList().indexOf(i)));
                d.add(audioInfoDatabase.getDurationById(i));
                t.add(audioInfoDatabase.getTitleById(i));
                e.add(audioInfoDatabase.getExtensionById(i));

            }

            recyclerViewAdapterPlay.setThumbs(th);
            recyclerViewAdapterPlay.setIdList(id_);
            recyclerViewAdapterPlay.setTitleList(t);
            recyclerViewAdapterPlay.setDurationList(d);
            recyclerViewAdapterPlay.setExtensionList(e);
            recyclerViewAdapterPlay.setListSize(id_.size());
            recyclerViewAdapterPlay.setContextScreen(Context.CONTEXT.PLAY);
            recyclerViewAdapterPlay.notifyDataSetChanged();
        }

        prevId = id;
        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN)
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.main_next:

            case R.id.next_fav:
                favouriteMomentsAudio.nextFavouriteMomentOperation(id, mediaPlayer.getCurrentPosition());
                break;

            case R.id.main_back:

            case R.id.back:
                favouriteMomentsAudio.previousFavouriteMomentOperation(id, mediaPlayer.getCurrentPosition(), false);
                break;

            case R.id.main_play_or_pause:

            case R.id.play_or_pause:
                playPausePress();
                break;

            case R.id.fav_play:
                favouriteMomentsAudio.addFavouriteMomentsOperation(id, mediaPlayer.getCurrentPosition());
                Log.d("ss", "jjs");
                break;

            default:
                break;


        }

    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {

            case R.id.main_next:

            case R.id.next_fav:
                nextSong();
                break;


            case R.id.fav_play:
                favouriteMomentsAudio.databaseDeleteOperation(id);
                Log.d("liu", "liii");
                break;

            case R.id.back:

            case R.id.main_back:
                favouriteMomentsAudio.previousFavouriteMomentOperation(id, mediaPlayer.getCurrentPosition(), true);
                break;

            default:
                break;

        }

        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playPauseMain.setImageResource(R.mipmap.final_play);
        seekBarMain.setProgress(mediaPlayerDuration);
        playPausePlay.setImageResource(R.mipmap.final_play);
        seekBarPlay.setProgress(mediaPlayerDuration);
        nextSong();

    }

    private void nextSong() {

        isSongChanged = true;
        if (trackInfo != null) {
            int albumIdIndex = trackInfo.getIdsInAlbum().indexOf(id);
            albumIdIndex = (albumIdIndex == trackInfo.getIdsInAlbum().size() - 1) ? 0 : albumIdIndex + 1;
            play(trackInfo.getIdsInAlbum().get(albumIdIndex));
            Log.d(String.valueOf(trackInfo.getIdsInAlbum().get(albumIdIndex)), trackInfo.getIdsInAlbum().toString());
        }

    }

    @Override
    public void onPanelSlide(View view, float v) {


    }

    @Override
    public void onPanelStateChanged(View view, SlidingUpPanelLayout.PanelState panelState, SlidingUpPanelLayout.PanelState panelState1) {


        if (panelState == SlidingUpPanelLayout.PanelState.COLLAPSED && panelState1 == SlidingUpPanelLayout.PanelState.DRAGGING) {

            setSeekBarProperties();
            playPausePlay.setVisibility(View.VISIBLE);
            titleTextViewPlay.setVisibility(View.VISIBLE);
            nextFavButtonPlay.setVisibility(View.VISIBLE);
            previousButtonPlay.setVisibility(View.VISIBLE);
            childLayout.setVisibility(View.GONE);
            MainActivity.isSlideCollapsed = false;
            MainActivity.isSlideExpanded = true;
            slidingUpPanelLayout.setDragView(backGroundPlay);
            mediaControllerPlay.seekBarOperations();
            if (mediaPlayer.isPlaying())
                playPausePlay.setImageResource(R.mipmap.final_pause);
            else
                playPausePlay.setImageResource(R.mipmap.final_play);

        }


        if (panelState == SlidingUpPanelLayout.PanelState.DRAGGING && panelState1 == SlidingUpPanelLayout.PanelState.COLLAPSED) {

            setSeekBarProperties();
            playPausePlay.setVisibility(View.GONE);
            titleTextViewPlay.setVisibility(View.GONE);
            nextFavButtonPlay.setVisibility(View.GONE);
            previousButtonPlay.setVisibility(View.GONE);
            slidingUpPanelLayout.setDragView(backGroundMain);
            childLayout.setVisibility(View.VISIBLE);
            MainActivity.isSlideCollapsed = true;
            MainActivity.isSlideExpanded = false;

            mediaControllerMain.seekBarOperations();
            if (mediaPlayer.isPlaying())
                playPauseMain.setImageResource(R.mipmap.final_pause);
            else
                playPauseMain.setImageResource(R.mipmap.final_play);

        }


    }


    @Override
    public void onFavouriteMomentsOperationsCompleted(FavouriteMoments favouriteMoments) {

        if (favouriteMoments.getMediaType() == Context.MEDIATYPE.AUDIO) {
            favouriteMomentsAudio = favouriteMoments;
            mFavouritesPositionsList = favouriteMomentsAudio.getList();
        }


    }

    @Override
    public void onOptionSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.group_by:
                switch (groupByAudio) {
                    case ALBUM:
                        groupByAudio = Context.GROUPBY.NOTHING;
                        initializeRecyclerView(audioInfoDatabase);
                        break;

                    case NOTHING:
                        groupByAudio = Context.GROUPBY.ALBUM;
                        initializeRecyclerView(audioInfoDatabase);
                        break;

                    default:
                        break;

                }
                break;
            default:
                break;
        }

    }
}


