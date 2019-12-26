package com.romeotamizh.MusicPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Music")
public class Music {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    public String id;


    @ColumnInfo(name = "musicId")
    public int musicId;

    @Nullable
    @ColumnInfo(name = "favouriteMoments")
    public Integer favouriteMoments;


    public Music(String id, int musicId, Integer favouriteMoments) {
        this.id = id;
        this.musicId = musicId;
        this.favouriteMoments = favouriteMoments;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public Integer getFavouriteMoments() {
        return favouriteMoments;
    }

    public void setFavouriteMoments(int favouriteMoments) {
        this.favouriteMoments = favouriteMoments;
    }

}
