package com.romeotamizh.MusicPlayer;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavouriteMoments {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "favoriteMomentsId")
    public int favoriteMomentsId;


}
