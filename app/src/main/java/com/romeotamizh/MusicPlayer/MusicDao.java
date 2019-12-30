package com.romeotamizh.MusicPlayer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(Music music);


    @Query("DELETE FROM Music")
    void deleteAll();

    @Query("DELETE FROM Music WHERE musicId =:mId")
    int delete(Integer mId);

    @Update
    void update(Music music);


    @Query("SELECT favouriteMoments FROM Music WHERE musicId = :mId ORDER BY favouriteMoments ASC")
    int[] getFavouriteMoments(Integer mId);

    @Query("SELECT id FROM Music WHERE musicId = :mId ORDER BY favouriteMoments ASC")
    String[] getId(Integer mId);


}
