package com.romeotamizh.MediaPlayer.Activities_Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.romeotamizh.MediaPlayer.Adapters.RecyclerViewAdapter;
import com.romeotamizh.MediaPlayer.Helpers.Context;
import com.romeotamizh.MediaPlayer.Helpers.CustomRecyclerView;
import com.romeotamizh.MediaPlayer.Helpers.MediaInfoDatabase;
import com.romeotamizh.MediaPlayer.Helpers.MyApplication;
import com.romeotamizh.MediaPlayer.MediaController.PlayMedia;
import com.romeotamizh.MediaPlayer.R;

import java.util.ArrayList;

import static com.romeotamizh.MediaPlayer.Activities_Fragments.MainActivity.groupByVideo;
import static com.romeotamizh.MediaPlayer.Activities_Fragments.MainActivity.isExitVideo;
import static com.romeotamizh.MediaPlayer.Helpers.Thumbnail.setThumbnailImage;


public class VideoFragment extends Fragment implements PlayMedia.OnPlayMediaListener, MyApplication.SetFragmentOnBackPressedListener, MyApplication.SetFragmentOnOptionsMenuClickedListener {

    MediaInfoDatabase videoInfo;
    private ArrayList<String> titleList;
    private ArrayList<Integer> idList;
    private ArrayList<Integer> durationList;
    private CustomRecyclerView recyclerViewVideo;
    private View rootView;


    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
        populate();
    }


    public static boolean isInFinalList = false;
    public static ArrayList<Bitmap> t = new ArrayList<>();
    RecyclerViewAdapter recyclerViewAdapter = null;
    Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void populate() {

        intent = new Intent(getContext(), VideoActivity.class);


        recyclerViewVideo = rootView.findViewById(R.id.recyclerView_video);

        //add video
        videoInfo = new MediaInfoDatabase(Context.MEDIATYPE.VIDEO);

        //initialize recyclerView
        initializeRecyclerView(videoInfo, 0);

        PlayMedia.setPlayMusicListener(this);

        MyApplication.setOnBackPressed(this);

        MyApplication.setOnOptionsSelected(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    void initializeRecyclerView(MediaInfoDatabase mediaInfoDatabase, int id) {

        if (recyclerViewAdapter == null)
            recyclerViewAdapter = new RecyclerViewAdapter(videoInfo, null, Context.CONTEXT.MAIN, getActivity());


        recyclerViewVideo.setAdapter(recyclerViewAdapter);

        if (groupByVideo == Context.GROUPBY.NOTHING) {
            recyclerViewVideo.setLayoutManager(new LinearLayoutManager(this.getContext()));
            recyclerViewAdapter.setTitleList(mediaInfoDatabase.getTitleList());
            recyclerViewAdapter.setIdList(mediaInfoDatabase.getIdList());
            recyclerViewAdapter.setDurationList(mediaInfoDatabase.getDurationList());
            recyclerViewAdapter.setExtensionList(mediaInfoDatabase.getExtensionList());
            recyclerViewAdapter.setListSize(mediaInfoDatabase.getMediaCount());
            recyclerViewAdapter.setThumbs(t);
            recyclerViewAdapter.notifyDataSetChanged();
            isInFinalList = true;


        }
        if (groupByVideo == Context.GROUPBY.ALBUM) {
            if (id == 0) {
                isInFinalList = false;
                Size size = new Size(200, 200);
                ArrayList<CharSequence> albumTitleList = mediaInfoDatabase.getAlbumTitleList();
                recyclerViewVideo.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
                recyclerViewAdapter.setTitleList(albumTitleList);
                recyclerViewAdapter.setExtensionList(albumTitleList);
                recyclerViewAdapter.setDurationList(albumTitleList);
                recyclerViewAdapter.setListSize(albumTitleList.size());
                ArrayList<Bitmap> bitmaps = new ArrayList<>();
                for (CharSequence album : albumTitleList)
                    bitmaps.add(setThumbnailImage(mediaInfoDatabase.getUriById(mediaInfoDatabase.getIdsInAlbum(album).get(0)), size, album));
                recyclerViewAdapter.setThumbs(bitmaps);
                recyclerViewAdapter.notifyDataSetChanged();


            } else {
                isInFinalList = true;
                Size size = new Size(80, 80);
                recyclerViewVideo.setLayoutManager(new LinearLayoutManager(this.getContext()));
                ArrayList<CharSequence> temp = new ArrayList<>();
                ArrayList<Bitmap> t = new ArrayList<>();
                ArrayList<CharSequence> e = new ArrayList<>();
                ArrayList<CharSequence> d = new ArrayList<>();
                recyclerViewAdapter.setIdList(videoInfo.getIdsInAlbum(id));
                for (int x : videoInfo.getIdsInAlbum(id)) {
                    CharSequence title = mediaInfoDatabase.getTitleById(x);
                    temp.add(title);
                    t.add(setThumbnailImage(mediaInfoDatabase.getUriById(x), size, title));
                    d.add(mediaInfoDatabase.getDurationById(x));
                    e.add(mediaInfoDatabase.getExtensionById(x));
                }
                recyclerViewAdapter.setExtensionList(e);
                recyclerViewAdapter.setTitleList(temp);
                recyclerViewAdapter.setThumbs(t);
                recyclerViewAdapter.setDurationList(d);
                recyclerViewAdapter.setListSize(t.size());
                recyclerViewAdapter.notifyDataSetChanged();


            }
        }

    }

    @Override
    public void playMedia(int id, Context.MEDIATYPE mediaType) {
        if (mediaType == Context.MEDIATYPE.VIDEO) {
            if (id != 0) {
                if (isInFinalList) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("URI", videoInfo.getUriById(id).toString());
                    intent.putExtra("TITLE", videoInfo.getTitleById(id));
                    intent.putExtra("ID", id);
                    Log.d(videoInfo.getUriById(id).toString(), "dummy");
                    this.startActivity(intent);
                } else {
                    initializeRecyclerView(videoInfo, id);

                }
            }
        }
    }


    @Override
    public void onBackPressed() {

        if (isInFinalList) {
            isExitVideo = false;
            initializeRecyclerView(videoInfo, 0);
        } else isExitVideo = true;

    }


    @Override
    public void onOptionSelected(MenuItem menuItem) {
        Log.d("io.", "lol.");

        switch (menuItem.getItemId()) {

            case R.id.group_by:
                Log.d("io.", "lol");
                if (groupByVideo == Context.GROUPBY.ALBUM) {
                    groupByVideo = Context.GROUPBY.NOTHING;
                    initializeRecyclerView(videoInfo, 0);
                } else if (groupByVideo == Context.GROUPBY.NOTHING) {
                    groupByVideo = Context.GROUPBY.ALBUM;
                    initializeRecyclerView(videoInfo, 0);
                }
                break;

            default:
                break;
        }


    }
}
