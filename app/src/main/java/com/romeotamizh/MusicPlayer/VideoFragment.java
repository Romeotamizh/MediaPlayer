package com.romeotamizh.MusicPlayer;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.romeotamizh.MusicPlayer.Helpers.Context;
import com.romeotamizh.MusicPlayer.Helpers.Media;
import com.romeotamizh.MusicPlayer.Helpers.MyRecyclerView;

import java.util.ArrayList;


public class VideoFragment extends Fragment {

    Media video;
    private ArrayList<String> mDataList;
    private ArrayList<String> mTitleList;
    private ArrayList<Integer> mIdList;
    private ArrayList<Integer> mDurationList;
    private MyRecyclerView recyclerViewVideo;
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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewVideo = view.findViewById(R.id.recyclerView_video);
        Log.d(String.valueOf(recyclerViewVideo.getBaseline()), "kkkpo");
        rootView = view;
        addMedia(Context.MEDIATYPE.VIDEO);
        //  recyclerView.
        //  video = new Media(Context.MEDIATYPE.VIDEO);
        //video.geMedia();
        //  mDataList =video.getmDataList();
//      // mTitleList = video.getmTitleList();
        //  mDurationList = video.getmDurationList();
        //initializeRecyclerView(mTitleList, mDurationList, mDataList, mIdList,);

    }


    void initializeRecyclerView(ArrayList<String> mTitleList, ArrayList<Integer> mDurationList, ArrayList<String> mDataList, ArrayList<Integer> mIdlist, Context.MEDIATYPE mediaType) {
        Log.d("jjjjjgyfhfhg", "kkjikygfhjg");
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(mTitleList, mDurationList, mDataList, mIdlist, getActivity(), mediaType);
        recyclerViewVideo.setAdapter(recyclerViewAdapter);
        recyclerViewVideo.setLayoutManager(new LinearLayoutManager(this.getContext()));
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


}
