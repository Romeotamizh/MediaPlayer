package com.romeotamizh.MediaPlayer.Helpers;


import android.util.Log;

import com.romeotamizh.MediaPlayer.Media;
import com.romeotamizh.MediaPlayer.MediaRoomDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import static com.romeotamizh.MediaPlayer.Helpers.CustomSeekBar.mFavouritesPositionsList;
import static com.romeotamizh.MediaPlayer.Helpers.MyApplication.getContext;

public class FavouriteMoments {
    private static ArrayList<OnFavouriteMomentsOperationsListener> listeners = new ArrayList<>();
    private static FavouriteMoments favouriteMoments;
    private static MediaRoomDatabase mediaRoomDatabase = MediaRoomDatabase.getInstance(getContext());
    private int[] list;
    private int count;
    private boolean isExist;
    private Context.MEDIATYPE mediaType;

    public FavouriteMoments(int[] list, int count, boolean isExist, Context.MEDIATYPE mediaType) {
        this.list = list;
        this.count = count;
        this.isExist = isExist;
        this.mediaType = mediaType;
    }

    public static void setOnFavouriteMomentsOperationsListener(OnFavouriteMomentsOperationsListener listener) {
        listeners.add(listener);
    }

    public static void callback(FavouriteMoments favouriteMoments) {
        for (OnFavouriteMomentsOperationsListener listener : listeners)
            listener.onFavouriteMomentsOperationsCompleted(favouriteMoments);

    }

    public int[] getList() {
        return list;
    }

    public int getCount() {
        return count;
    }

    public boolean isExist() {
        return isExist;
    }

    public Context.MEDIATYPE getMediaType() {
        return mediaType;
    }

    public void databaseDeleteOperation(final int mId) {
        final Context.MEDIATYPE mediaType = this.mediaType;
        if (mId != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mediaRoomDatabase.mediaDao().delete(mId);
                    mFavouritesPositionsList = new int[100];
                    favouriteMoments = new FavouriteMoments(new int[100], 1, false, mediaType);
                    FavouriteMoments.callback(favouriteMoments);

                }
            }).start();
        }


    }

    public void databaseGetFavouritesOperation(final int mId) {

        final Context.MEDIATYPE mediatype = this.mediaType;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int[] temp;
                int[] list = new int[100];
                int count = 1;
                boolean isExist = false;

                temp = mediaRoomDatabase.mediaDao().getFavouriteMoments(mId);


                if (temp.length > 1) {
                    Arrays.sort(temp);
                    list = Arrays.copyOf(temp, 100);
                    count = temp.length;
                    isExist = true;
                }

                favouriteMoments = new FavouriteMoments(list, count, isExist, mediaType);
                Log.d("mediatype", mediaType.toString());
                FavouriteMoments.callback(favouriteMoments);


            }
        }).start();


    }

    private void databaseInsertOperation(final int mId) {
        favouriteMoments = this;

        new Thread(new Runnable() {
            @Override
            public void run() {


                for (int mFavouritesPosition : favouriteMoments.list) {
                    Media music = new Media(mId + "." + mFavouritesPosition, mId, mFavouritesPosition);
                    mediaRoomDatabase.mediaDao().insertData(music);
                }


            }
        }).start();


    }

    public void addFavouriteMomentsOperation(int mId, int currentPosition) {
        if (mId != 0) {
            favouriteMoments = this;
            favouriteMoments.list[favouriteMoments.count++] = currentPosition;
            Arrays.sort(favouriteMoments.list, 0, favouriteMoments.count);
            favouriteMoments.isExist = true;
            FavouriteMoments.callback(favouriteMoments);
            databaseInsertOperation(mId);
            Log.d("j1j", Arrays.toString(list));

        }

    }

    public void nextFavouriteMomentOperation(final int mId, int currentPosition) {
        favouriteMoments = this;
        if (mId != 0)
            if (favouriteMoments.isExist) {
                int[] temp = Arrays.copyOfRange(favouriteMoments.list, 0, favouriteMoments.count + 1);
                temp[favouriteMoments.count] = currentPosition;
                Arrays.sort(temp);
                int currentPositionIndex = Arrays.binarySearch(temp, currentPosition);
                if (currentPositionIndex < temp.length - 1)
                    CustomSeekBar.callBack(temp[currentPositionIndex + 1]);


            }

    }

    public void previousFavouriteMomentOperation(final int mId, int currentPosition, final boolean isPreviousButtonLongPressed) {
        favouriteMoments = this;
        if (mId != 0)
            if (favouriteMoments.isExist) {
                int[] temp = Arrays.copyOfRange(favouriteMoments.list, 0, favouriteMoments.count + 1);
                temp[favouriteMoments.count] = currentPosition;
                Arrays.sort(temp);
                Log.d(Arrays.toString(temp), String.valueOf(currentPosition));

                int currentPositionIndex;
                currentPositionIndex = Arrays.binarySearch(temp, currentPosition);

                if (isPreviousButtonLongPressed)
                    if (currentPositionIndex >= 2)
                        CustomSeekBar.callBack(temp[currentPositionIndex - 2]);
                    else
                        CustomSeekBar.callBack(temp[currentPositionIndex - 1]);
                else
                    CustomSeekBar.callBack(temp[currentPositionIndex - 1]);

            }

    }


    public interface OnFavouriteMomentsOperationsListener {

        void onFavouriteMomentsOperationsCompleted(FavouriteMoments favouriteMoments);
    }
}
