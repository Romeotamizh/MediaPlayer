package com.romeotamizh.MediaPlayer.Activities_Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.romeotamizh.MediaPlayer.Adapters.RecyclerViewAdapter;
import com.romeotamizh.MediaPlayer.Helpers.Context;
import com.romeotamizh.MediaPlayer.Helpers.CustomRecyclerView;
import com.romeotamizh.MediaPlayer.Helpers.MediaInfoDatabase;
import com.romeotamizh.MediaPlayer.MediaController.PlayMedia;
import com.romeotamizh.MediaPlayer.R;

import java.util.ArrayList;


public class VideoFragment extends Fragment implements PlayMedia.OnPlayMediaListener {

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void populate() {

        recyclerViewVideo = rootView.findViewById(R.id.recyclerView_video);

        //add video
        videoInfo = new MediaInfoDatabase(Context.MEDIATYPE.VIDEO);

        //initialize recyclerView
        initializeRecyclerView(videoInfo);

        PlayMedia.setPlayMusicListener(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    void initializeRecyclerView(MediaInfoDatabase mediaInfoDatabase) {
        ArrayList<CharSequence> titleList = mediaInfoDatabase.getTitleList();
        ArrayList<CharSequence> durationList = mediaInfoDatabase.getDurationList();
        ArrayList<CharSequence> extensionList = mediaInfoDatabase.getExtensionList();
        ArrayList<Uri> uriList = mediaInfoDatabase.getUriList();
        ArrayList<Integer> idList = mediaInfoDatabase.getIdList();
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(idList, uriList, titleList, extensionList, durationList, Context.MEDIATYPE.VIDEO, Context.CONTEXT.MAIN, getActivity());
        recyclerViewVideo.setAdapter(recyclerViewAdapter);
        recyclerViewVideo.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }


    @Override
    public void playMedia(int id, Context.MEDIATYPE mediaType) {
        if (mediaType == Context.MEDIATYPE.VIDEO) {
            if (id != 0) {
                Intent intent = new Intent(this.getContext(), VideoActivity.class);
                intent.putExtra("URI", videoInfo.getUriById(id).toString());
                intent.putExtra("TITLE", videoInfo.getTitleById(id));
                intent.putExtra("ID", id);
                Log.d(videoInfo.getUriById(id).toString(), "dummy");
                this.getContext().startActivity(intent);
            }
        }
    }
}
