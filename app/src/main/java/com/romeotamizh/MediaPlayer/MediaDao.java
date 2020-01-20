package com.romeotamizh.MediaPlayer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface MediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(Media media);


    @Query("DELETE FROM Media")
    void deleteAll();

    @Query("DELETE FROM Media WHERE mediaId =:mId")
    int delete(Integer mId);

    @Update
    void update(Media media);


    @Query("SELECT favouriteMoments FROM Media WHERE mediaId = :mId ORDER BY favouriteMoments ASC")
    int[] getFavouriteMoments(Integer mId);

    @Query("SELECT id FROM Media WHERE mediaId = :mId ORDER BY favouriteMoments ASC")
    String[] getId(Integer mId);


}
