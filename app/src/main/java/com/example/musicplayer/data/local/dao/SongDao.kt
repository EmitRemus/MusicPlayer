package com.example.musicplayer.data.local.dao

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.musicplayer.data.local.entities.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<SongEntity>)

    @Query("SELECT * FROM songs ORDER BY TITLE ASC")
    fun getAllSongs(): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE path = :path LIMIT 1")
    suspend fun getSongByPath(path: String): SongEntity?

    @Query("UPDATE songs SET title = :newTitle, artist = :newArtist, album = :newAlbum WHERE path = :path")
    suspend fun editSong(path: String, newTitle: String, newArtist: String, newAlbum: String)

    @Query("DELETE FROM songs")
    suspend fun deleteAll()

    @Query("SELECT * FROM songs")
    suspend fun getAllSongsNonFlowSuspend(): List<SongEntity>

    @Query("DELETE FROM songs WHERE path IN (:paths)")
    suspend fun deleteSongsByPaths(paths: Set<String>)

    @Transaction
    suspend fun runInTransaction(block: suspend () -> Unit) {
        block()
    }
}