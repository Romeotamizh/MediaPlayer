package com.romeotamizh.MusicPlayer;

import android.util.Log;

import java.util.Arrays;

import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.isFavouriteMomentsExist;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.mFavouriteMomentsCount;
import static com.romeotamizh.MusicPlayer.Activities.PlayScreenActivity.mFavouriteMomentsList;
import static com.romeotamizh.MusicPlayer.MyApplication.getContext;


public class FavouriteMomentsRepository {


    public static boolean isFinished = false;
    static DatabaseOperationObject databaseOperationObject;
    static MusicRoomDatabase musicRoomDatabase = MusicRoomDatabase.getInstance(getContext());


    public static void databaseDeleteOperation(final int mId, final String s) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                isFinished = false;
                int temp = musicRoomDatabase.musicDao().delete(mId);
                int[] toemp = new int[100];
              /*  mFavouriteMomentsList = new int[100];
                mFavouriteMomentsCount = 1;
                isFavouriteMomentsExist = false;*/
                databaseOperationObject = new DatabaseOperationObject(toemp, 1, false);

                Log.d("dbdel ", String.valueOf(temp));
                isFinished = true;


            }
        }).start();


    }

    public static void databaseGetFavouritesOperation(final int mId, final String s) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                isFinished = false;
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


                databaseOperationObject = new DatabaseOperationObject(mFavouriteMomentsList, mFavouriteMomentsCount, isFavouriteMomentsExist);
                isFinished = true;
                setFavouritesDataMusic(databaseOperationObject, s);
                Thread.currentThread().interrupt();
                return;


            }
        });
        t1.start();


    }

    private static void setFavouritesDataMusic(DatabaseOperationObject databaseOperationObject, final String s) {
        mFavouriteMomentsCount = databaseOperationObject.mFavouriteMomentsCount;
        mFavouriteMomentsList = databaseOperationObject.mFavouriteMomentsList;
        isFavouriteMomentsExist = databaseOperationObject.isFavouriteMomentsExist;
        Log.d("getfavfno", Arrays.toString(mFavouriteMomentsList));
        Log.d("getfavfno", String.valueOf(mFavouriteMomentsCount));
    }


    public static void databaseInsertOperation(final int mId, final int mFavouriteMomentsCount, final int[] mFavouriteMomentsList, final String s) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                isFinished = false;

                boolean isFavouriteMomentsExist = false;
                for (int i = 0; i < mFavouriteMomentsCount; i++) {
                    Music music = new Music(String.valueOf(mId) + "." + String.valueOf(mFavouriteMomentsList[i]), mId, mFavouriteMomentsList[i]);
                    musicRoomDatabase.musicDao().insertData(music);

                }
                Log.d("dbinsfn", Arrays.toString(musicRoomDatabase.musicDao().getFavouriteMoments(mId)));
                Log.d("dbinsfn", Arrays.toString(musicRoomDatabase.musicDao().getId(mId)));

                isFinished = true;
                Thread.currentThread().interrupt();
                return;


            }
        }).start();


    }

    public static void resetFavouritesOperation(final String s) {
        mFavouriteMomentsCount = 1;
        mFavouriteMomentsList = new int[100];
        isFavouriteMomentsExist = false;
        Log.d("resetfav", "resetdone");

    }


}
