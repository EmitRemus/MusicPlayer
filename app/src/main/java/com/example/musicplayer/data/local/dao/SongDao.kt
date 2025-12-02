package com.example.musicplayer.data.local.dao

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Insert
import androidx.room.Query
import com.example.musicplayer.data.local.entities.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<SongEntity>)

    @Query("SELECT * FROM songs ORDER BY TITLE ASC")
    fun getAllSongs(): Flow<List<SongEntity>>

    @Query("DELETE FROM songs")
    suspend fun deleteAll()
}