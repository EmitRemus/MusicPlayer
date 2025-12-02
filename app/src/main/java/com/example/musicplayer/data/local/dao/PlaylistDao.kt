package com.example.musicplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.example.musicplayer.data.local.entities.PlaylistEntity
import com.example.musicplayer.data.local.entities.PlaylistSongCrossRef
import com.example.musicplayer.data.local.entities.SongEntity
import kotlinx.coroutines.flow.Flow

data class PlaylistWithSongs(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "path",
        associateBy = Junction(
            PlaylistSongCrossRef::class,
            parentColumn = "playlistId",
            entityColumn = "songPath")
    )
    val songs: List<SongEntity>
)

@Dao
interface PlaylistDao {

    @Insert
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSongToPlaylist(crossRef: PlaylistSongCrossRef)

    @Delete
    suspend fun removeSongFromPlaylist(crossRef: PlaylistSongCrossRef)

    @Transaction
    @Query("SELECT * FROM playlists WHERE playlistId = :id")
    fun getPlaylistWithSongs(id: Long): Flow<PlaylistWithSongs>

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
}