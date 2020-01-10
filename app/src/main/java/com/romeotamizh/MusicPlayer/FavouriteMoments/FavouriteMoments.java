package com.romeotamizh.MusicPlayer.FavouriteMoments;


import android.util.Log;

import com.romeotamizh.MusicPlayer.Helpers.Context;
import com.romeotamizh.MusicPlayer.Helpers.SeekBarWithFavourites;
import com.romeotamizh.MusicPlayer.Media;
import com.romeotamizh.MusicPlayer.MediaRoomDatabase;

import java.util.Arrays;

import static com.romeotamizh.MusicPlayer.Helpers.MyApplication.getContext;
import static com.romeotamizh.MusicPlayer.Helpers.SeekBarWithFavourites.mFavouritesPositionsList;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;

public class FavouriteMoments {
    private static OnFavouriteMomentsOperationsListener listener;
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
        FavouriteMoments.listener = listener;
    }

    public static void callback(FavouriteMoments favouriteMoments) {
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
                Log.d("kk", mediaType.toString());
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

    public void addFavouriteMomentsOperation(int mId) {
        favouriteMoments = this;
        favouriteMoments.list[favouriteMoments.count++] = mediaPlayer.getCurrentPosition();
        Arrays.sort(favouriteMoments.list, 0, favouriteMoments.count);
        favouriteMoments.isExist = true;
        FavouriteMoments.callback(favouriteMoments);
        databaseInsertOperation(mId);


    }

    public void nextFavouriteMomentOperation(final int mId) {
        favouriteMoments = this;
        if (mId != 0)
            if (favouriteMoments.isExist) {

                int[] temp = Arrays.copyOfRange(favouriteMoments.list, 0, favouriteMoments.count + 1);
                int currentPosition = mediaPlayer.getCurrentPosition();
                temp[favouriteMoments.count] = currentPosition;
                Arrays.sort(temp);
                int currentPositionIndex = Arrays.binarySearch(temp, currentPosition);
                if (currentPositionIndex < temp.length - 1) {
                    if (!mediaPlayer.isPlaying())
                        SeekBarWithFavourites.callBack(temp[currentPositionIndex + 1]);
                    mediaPlayer.seekTo(temp[currentPositionIndex + 1]);
                }

            }

    }

    public void previousFavouriteMomentOperation(final int mId, final boolean isPreviousButtonLongPressed) {
        favouriteMoments = this;
        if (mId != 0)
            if (favouriteMoments.isExist) {
                int[] temp = Arrays.copyOfRange(favouriteMoments.list, 0, favouriteMoments.count + 1);
                int currentPosition = mediaPlayer.getCurrentPosition();
                temp[favouriteMoments.count] = currentPosition;
                Arrays.sort(temp);
                Log.d(Arrays.toString(temp), String.valueOf(currentPosition));

                int currentPositionIndex;
                currentPositionIndex = Arrays.binarySearch(temp, currentPosition);
                Log.d("cpi", String.valueOf(currentPositionIndex));

                if (isPreviousButtonLongPressed) {
                    if (currentPositionIndex >= 2) {
                        if (!mediaPlayer.isPlaying())
                            SeekBarWithFavourites.callBack(temp[currentPositionIndex - 2]);
                        mediaPlayer.seekTo(temp[currentPositionIndex - 2]);
                    } else if (currentPositionIndex >= 1) {
                        if (!mediaPlayer.isPlaying())
                            SeekBarWithFavourites.callBack(temp[currentPositionIndex - 1]);

                        mediaPlayer.seekTo(temp[currentPositionIndex - 1]);
                    }
                } else {
                    if (!mediaPlayer.isPlaying())
                        SeekBarWithFavourites.callBack(temp[currentPositionIndex - 1]);
                    mediaPlayer.seekTo(temp[currentPositionIndex - 1]);
                }


            } else {
                if (!mediaPlayer.isPlaying())
                    SeekBarWithFavourites.callBack(0);
                mediaPlayer.seekTo(0);


            }
    }


    public interface OnFavouriteMomentsOperationsListener {

        void onFavouriteMomentsOperationsCompleted(FavouriteMoments favouriteMoments);
    }
}
