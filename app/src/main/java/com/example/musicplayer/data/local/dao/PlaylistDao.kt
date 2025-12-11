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


/**
 * Room "relation" to load a playlist and all its songs in one shot.
 */
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSongToPlaylist(crossRef: PlaylistSongCrossRef)

    @Query("DELETE FROM playlist_song_crossref WHERE playlistId = :id AND songPath = :path")
    suspend fun removeSongFromPlaylist(id: Long, path: String)

    @Transaction
    @Query("SELECT * FROM playlists WHERE playlistId = :id")
    fun getPlaylistWithSongs(id: Long): Flow<PlaylistWithSongs>

    @Query("SELECT * FROM playlists ORDER BY name ASC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("UPDATE playlists SET name = :newName WHERE playlistId = :id")
    suspend fun renamePlaylist(id: Long, newName: String)

    @Query("DELETE FROM playlists WHERE playlistId = :id")
    suspend fun deletePlaylistById(id: Long)

    @Query("DELETE FROM playlist_song_crossref WHERE playlistId = :id")
    suspend fun deleteAllSongsFromPlaylist(id: Long)
}