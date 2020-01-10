package com.romeotamizh.MusicPlayer.Helpers;

import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class Media {


    private ArrayList<String> mDataList;
    private ArrayList<String> mTitleList;
    private ArrayList<Integer> mIdList;
    private Context.MEDIATYPE mediaType;
    private ArrayList<Integer> mDurationList;

    public Media(/*ArrayList<String> mDataList, ArrayList<String> mTitleList, ArrayList<Integer> mIdList,*/ Context.MEDIATYPE mediaType) {
       /* this.mDataList = mDataList;
        this.mTitleList = mTitleList;
        this.mIdList = mIdList;*/
        this.mediaType = mediaType;
        addMedia(this.mediaType);
    }

    public ArrayList<String> getmDataList() {
        return mDataList;
    }

    public ArrayList<String> getmTitleList() {
        return mTitleList;
    }

    public ArrayList<Integer> getmIdList() {
        return mIdList;
    }

    public Context.MEDIATYPE getMediaType() {
        return mediaType;
    }

    public ArrayList<Integer> getmDurationList() {
        return mDurationList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addMedia(com.romeotamizh.MusicPlayer.Helpers.Context.MEDIATYPE mediaType) {
        this.mTitleList = new ArrayList<>();
        this.mDurationList = new ArrayList<>();
        this.mIdList = new ArrayList<>();
        this.mDataList = new ArrayList<>();
        Cursor cursor;
        //   Cursor cursorAlbum;
//        cursorAlbum = getApplicationContext().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,null,null,null,"upper("+MediaStore.Audio.Albums.ALBUM_ID);

        if (mediaType == Context.MEDIATYPE.AUDIO)
            cursor = MyApplication.getContext().getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, "upper(" + MediaStore.Audio.Media.DISPLAY_NAME + ")ASC");
        else
            cursor = MyApplication.getContext().getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, "upper(" + MediaStore.Video.Media.DISPLAY_NAME + ")ASC");

        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                this.mTitleList.add(cursor.getString(cursor.getColumnIndex("_display_name")));
                mDurationList.add(cursor.getInt(cursor.getColumnIndex("duration")));
                this.mDataList.add(cursor.getString(cursor.getColumnIndex("_data")));
                this.mIdList.add(cursor.getInt(cursor.getColumnIndex("_id")));

            }
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.d("Column names", cursor.getColumnName(i));

            }
            //cursor.moveToPosition(2);

            // Log.d("data", cursor.getString(cursor.getColumnIndex("track")));


            cursor.close();


            // initializeRecyclerView(mTitleList, mDurationList, mDataList, mIdList);
        }


    }
}
