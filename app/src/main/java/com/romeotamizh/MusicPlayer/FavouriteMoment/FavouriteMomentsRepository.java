package com.romeotamizh.MusicPlayer.FavouriteMoment;

import android.util.Log;

import com.romeotamizh.MusicPlayer.Music;
import com.romeotamizh.MusicPlayer.MusicRoomDatabase;

import java.util.Arrays;

import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.isFavouriteMomentsExist;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.mFavouriteMomentsCount;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.mFavouriteMomentsList;
import static com.romeotamizh.MusicPlayer.Helpers.MyApplication.getContext;
import static com.romeotamizh.MusicPlayer.Helpers.SeekbarWithFavourites.mFavouritesPositionsList;


public class FavouriteMomentsRepository {


    static FavouriteMoments favouriteMoments;
    static MusicRoomDatabase musicRoomDatabase = MusicRoomDatabase.getInstance(getContext());


    public static void databaseDeleteOperation(final int mId, final String s) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                int temp = musicRoomDatabase.musicDao().delete(mId);

                favouriteMoments = new FavouriteMoments(new int[100], 1, false);

                Log.d("dbdel ", String.valueOf(temp));
                resetFavouritesOperation("music");


            }
        }).start();


    }

    public static void databaseGetFavouritesOperation(final int mId, final String s) {
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
                setFavouritesDataMusic(favouriteMoments, s);
                Thread.currentThread().interrupt();
                return;


            }
        });
        t1.start();


    }

    private static void setFavouritesDataMusic(FavouriteMoments favouriteMoments, final String s) {
        mFavouriteMomentsCount = favouriteMoments.mFavouriteMomentsCount;
        mFavouriteMomentsList = favouriteMoments.mFavouriteMomentsList;
        isFavouriteMomentsExist = favouriteMoments.isFavouriteMomentsExist;
        mFavouritesPositionsList = mFavouriteMomentsList;
        Log.d("getfavfno", Arrays.toString(mFavouriteMomentsList));
        Log.d("getfavfno", String.valueOf(mFavouriteMomentsCount));
    }


    public static void databaseInsertOperation(final int mId, final int mFavouriteMomentsCount, final int[] mFavouriteMomentsList, final String s) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                favouriteMoments = new FavouriteMoments(mFavouriteMomentsList, mFavouriteMomentsCount, isFavouriteMomentsExist);

                setFavouritesDataMusic(favouriteMoments, s);

                for (int i = 0; i < mFavouriteMomentsCount; i++) {
                    Music music = new Music(mId + "." + mFavouriteMomentsList[i], mId, mFavouriteMomentsList[i]);
                    musicRoomDatabase.musicDao().insertData(music);

                }
                Log.d("dbinsfn", Arrays.toString(musicRoomDatabase.musicDao().getFavouriteMoments(mId)));
                Log.d("dbinsfn", Arrays.toString(musicRoomDatabase.musicDao().getId(mId)));


                Thread.currentThread().interrupt();
                return;


            }
        }).start();


    }

    public static void resetFavouritesOperation(final String s) {
        mFavouriteMomentsCount = 1;
        mFavouritesPositionsList = new int[100];
        mFavouriteMomentsList = new int[100];
        isFavouriteMomentsExist = false;
        Log.d("resetfav", "resetdone");

    }


}
