package com.romeotamizh.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Media")
public class Media {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    private String id;


    @ColumnInfo(name = "mediaId")
    private int mediaId;

    @Nullable
    @ColumnInfo(name = "favouriteMoments")
    private Integer favouriteMoments;


    public Media(String id, int mediaId, Integer favouriteMoments) {
        this.id = id;
        this.mediaId = mediaId;
        this.favouriteMoments = favouriteMoments;
    }

    public int getMediaId() {
        return mediaId;
    }



    public Integer getFavouriteMoments() {
        return favouriteMoments;
    }


    public String getId() {
        return id;
    }

}
