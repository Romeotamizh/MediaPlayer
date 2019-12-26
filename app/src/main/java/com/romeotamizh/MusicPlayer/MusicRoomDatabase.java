package com.romeotamizh.MusicPlayer;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Music.class}, version = 2, exportSchema = false)
public abstract class MusicRoomDatabase extends RoomDatabase {
    public static MusicRoomDatabase instance;
    public static ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public static synchronized MusicRoomDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context, MusicRoomDatabase.class, "MusicRoomDatabase").fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

    public abstract MusicDao musicDao();


}
