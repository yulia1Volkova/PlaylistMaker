package com.practicum.playlistmaker.db.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.db.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM favorite_tracks_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT COUNT(*) as count FROM favorite_tracks_table WHERE trackId = :trackId")
    suspend fun isFavorite(trackId:Int): Int
}