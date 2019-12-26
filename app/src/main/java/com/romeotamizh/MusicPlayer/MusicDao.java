package com.romeotamizh.MusicPlayer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MusicDao {
    /*@Query("IF NOT EXISTS (SELECT * FROM Music WHERE Music.musicId = :musicId and Music.favouriteMoments = :favouriteMoments) INSERT INTO Music (musicId, favouriteMoments) VALUES(:musicId, :favouriteMoments)" )
    void insertData(int musicId,int favouriteMoments);*/
   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData_(int musicId,int favouriteMoments);
*/
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

    @Query(("SELECT musicId, favouriteMoments, COUNT(*) FROM Music WHERE musicId = :mId GROUP BY musicId,favouriteMoments HAVING COUNT(*) >1"))
    int[] getFavouriteMoments_(Integer mId);


}
