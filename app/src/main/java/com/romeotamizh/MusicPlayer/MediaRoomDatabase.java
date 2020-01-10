package com.romeotamizh.MusicPlayer;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Media.class}, version = 1, exportSchema = false)
public abstract class MediaRoomDatabase extends RoomDatabase {
    public static MediaRoomDatabase instance;
    public static ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public static synchronized MediaRoomDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context, MediaRoomDatabase.class, "MusicRoomDatabase").fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

    public abstract MediaDao mediaDao();


}
