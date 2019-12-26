package com.romeotamizh.MusicPlayer;

import android.app.Application;

import java.util.ArrayList;

public class MusicRepository {
    public MusicDao musicDao;
    public ArrayList<Integer> favouriteMoments;
    public int mId;

    MusicRepository(Application application) {
        //MusicRoomDatabase musicRoomDatabase = MusicRoomDatabase.getInstance(application);
        //  this.musicDao = musicRoomDatabase.musicDao();
        //this.favouriteMoments = musicDao.getFavouriteMoments(mId);


    }
}
