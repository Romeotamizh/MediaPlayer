package com.romeotamizh.MusicPlayer.FavouriteMoments;

import android.util.Log;

import com.romeotamizh.MusicPlayer.Activities.MainActivity;
import com.romeotamizh.MusicPlayer.Helpers.SeekbarWithFavourites;
import com.romeotamizh.MusicPlayer.Music;
import com.romeotamizh.MusicPlayer.MusicRoomDatabase;

import java.util.Arrays;

import static com.romeotamizh.MusicPlayer.Helpers.MyApplication.getContext;
import static com.romeotamizh.MusicPlayer.Helpers.SeekbarWithFavourites.mFavouritesPositionsList;
import static com.romeotamizh.MusicPlayer.PlayMusic.mediaPlayer;


public class FavouriteMomentsRepository {


    static FavouriteMoments favouriteMoments;
    static MusicRoomDatabase musicRoomDatabase = MusicRoomDatabase.getInstance(getContext());


    public static void databaseDeleteOperation(final int mId, final String context) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                int temp = musicRoomDatabase.musicDao().delete(mId);

                favouriteMoments = new FavouriteMoments(new int[100], 1, false);

                Log.d("dbdel ", String.valueOf(temp));
                resetFavouritesOperation(context);


            }
        }).start();


    }

    public static void databaseGetFavouritesOperation(final int mId, final String context) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int[] temp;
                int[] mFavouriteMomentsList = new int[100];
                int mFavouriteMomentsCount = 1;
                boolean isFavouriteMomentsExist = false;

                temp = musicRoomDatabase.musicDao().getFavouriteMoments(mId);
                Log.d("getfavfn", Arrays.toString(temp));
                Log.d("getfavfn", String.valueOf(temp.length));


                if (temp.length <= 1) {
                    isFavouriteMomentsExist = false;
                    mFavouriteMomentsCount = 1;
                    mFavouriteMomentsList = new int[100];
                } else {
                    Arrays.sort(temp);
                    mFavouriteMomentsList = Arrays.copyOf(temp, 100);
                    mFavouriteMomentsCount = temp.length;
                    isFavouriteMomentsExist = true;
                }


                favouriteMoments = new FavouriteMoments(mFavouriteMomentsList, mFavouriteMomentsCount, isFavouriteMomentsExist);
                setFavouritesData(favouriteMoments, context);
                Thread.currentThread().interrupt();
                return;


            }
        });
        t1.start();


    }

    public static void setFavouritesData(FavouriteMoments favouriteMoments, final String context) {
        if (context.equals("music")) {
            MainActivity.mFavouriteMomentsCount = favouriteMoments.mFavouriteMomentsCount;
            MainActivity.mFavouriteMomentsList = favouriteMoments.mFavouriteMomentsList;
            MainActivity.isFavouriteMomentsExist = favouriteMoments.isFavouriteMomentsExist;
            mFavouritesPositionsList = favouriteMoments.mFavouriteMomentsList;
//            seekBar.setmFavouritesPositionsList(MainActivity.mFavouriteMomentsList);

            Log.d("getfavfno", Arrays.toString(mFavouritesPositionsList));
            Log.d("getfavfno", String.valueOf(MainActivity.mFavouriteMomentsCount));
        }
    }


    public static void databaseInsertOperation(final int mId, final int mFavouriteMomentsCount, final int[] mFavouriteMomentsList, final boolean isFavouriteMomentsExist, final String context) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                favouriteMoments = new FavouriteMoments(mFavouriteMomentsList, mFavouriteMomentsCount, isFavouriteMomentsExist);

                setFavouritesData(favouriteMoments, context);

                /*for (int i = 0; i < mFavouriteMomentsCount; i++) {
                    Music music = new Music(mId + "." + mFavouriteMomentsList[i], mId, mFavouriteMomentsList[i]);
                    musicRoomDatabase.musicDao().insertData(music);

                }*/


                for (int mFavouritesPosition : mFavouriteMomentsList) {
                    Music music = new Music(mId + "." + mFavouritesPosition, mId, mFavouritesPosition);
                    musicRoomDatabase.musicDao().insertData(music);
                }


                Log.d("dbinsfn", Arrays.toString(musicRoomDatabase.musicDao().getFavouriteMoments(mId)));
                Log.d("dbinsfn", Arrays.toString(musicRoomDatabase.musicDao().getId(mId)));


                Thread.currentThread().interrupt();
                return;


            }
        }).start();


    }

    public static void resetFavouritesOperation(final String context) {

        mFavouritesPositionsList = new int[100];

        favouriteMoments = new FavouriteMoments(new int[100], 1, false);
        setFavouritesData(favouriteMoments, context);

        Log.d("resetfav", "resetdone");

    }


    public static void addFavouriteMomentsOperation(int mId, int mFavouriteMomentsCount, int[] mFavouriteMomentsList, boolean isFavouriteMomentsExist, String context) {

        isFavouriteMomentsExist = true;
        int mFavouriteMomentLag = 0;
        mFavouriteMomentsList[mFavouriteMomentsCount++] = mediaPlayer.getCurrentPosition() - mFavouriteMomentLag;
        Arrays.sort(mFavouriteMomentsList, 0, mFavouriteMomentsCount);
        Log.d("favouritemomentadded", Arrays.toString(mFavouriteMomentsList));
        favouriteMoments = new FavouriteMoments(mFavouriteMomentsList, mFavouriteMomentsCount, isFavouriteMomentsExist);
        setFavouritesData(favouriteMoments, context);

        databaseInsertOperation(mId, mFavouriteMomentsCount, mFavouriteMomentsList, isFavouriteMomentsExist, context);


    }


    public static void nextFavouriteMomentOperation(final int mId, final int mFavouriteMomentsCount, final int[] mFavouriteMomentsList, final boolean isFavouriteMomentsExist, SeekbarWithFavourites seekBar) {
        int[] temp;
        int currentPosition;

        if (isFavouriteMomentsExist) {

            temp = Arrays.copyOfRange(mFavouriteMomentsList, 0, mFavouriteMomentsCount + 1);
            currentPosition = mediaPlayer.getCurrentPosition();
            temp[mFavouriteMomentsCount] = currentPosition;
            Arrays.sort(temp);
            int currentPositionIndex;
            currentPositionIndex = Arrays.binarySearch(temp, currentPosition);
            if (currentPositionIndex < temp.length - 1) {
                if (!mediaPlayer.isPlaying())
                    seekBar.setProgress(temp[currentPositionIndex + 1]);
                mediaPlayer.seekTo(temp[currentPositionIndex + 1]);
            }

            Log.d("nextfavouritemoment", String.valueOf(mediaPlayer.getCurrentPosition()));
            Log.d("nextfavouritemoment", Arrays.toString(temp));


        }

    }


    public static void previousFavouriteMomentOperation(final int mId, final int mFavouriteMomentsCount, final int[] mFavouriteMomentsList, final boolean isFavouriteMomentsExist, final boolean isPreviousButtonLongPressed, SeekbarWithFavourites seekBar) {
        int[] temp;
        if (isFavouriteMomentsExist) {
            temp = Arrays.copyOfRange(mFavouriteMomentsList, 0, mFavouriteMomentsCount + 1);
            int currentPosition = mediaPlayer.getCurrentPosition();
            temp[mFavouriteMomentsCount] = currentPosition;
            Arrays.sort(temp);
            int currentPositionIndex;
            currentPositionIndex = Arrays.binarySearch(temp, currentPosition);
            if (isPreviousButtonLongPressed) {
                if (currentPositionIndex >= 2) {
                    if (!mediaPlayer.isPlaying())
                        seekBar.setProgress(temp[currentPositionIndex - 2]);
                    mediaPlayer.seekTo(temp[currentPositionIndex - 2]);
                } else if (currentPositionIndex >= 1) {
                    if (!mediaPlayer.isPlaying())
                        seekBar.setProgress(temp[currentPositionIndex - 1]);
                    mediaPlayer.seekTo(temp[currentPositionIndex - 1]);
                }
            } else {
                if (!mediaPlayer.isPlaying())
                    seekBar.setProgress(temp[currentPositionIndex - 1]);
                mediaPlayer.seekTo(temp[currentPositionIndex - 1]);
            }
            Log.d("prevfavouritemoment", String.valueOf(mediaPlayer.getCurrentPosition()));
            Log.d("prevfavouritemoment", Arrays.toString(temp));

        } else {

            mediaPlayer.seekTo(0);


        }
    }


}
