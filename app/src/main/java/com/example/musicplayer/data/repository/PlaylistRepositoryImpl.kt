package com.example.musicplayer.data.repository

import com.example.musicplayer.data.local.dao.PlaylistDao
import com.example.musicplayer.data.local.dao.PlaylistWithSongs
import com.example.musicplayer.data.local.entities.PlaylistEntity
import com.example.musicplayer.data.local.entities.PlaylistSongCrossRef
import kotlinx.coroutines.flow.Flow
class PlaylistRepositoryImpl (
    private val playlistDao: PlaylistDao
) {
    /** For playlist list screen */
    fun getAllPlaylists(): Flow<List<PlaylistEntity>> =
        playlistDao.getAllPlaylists()

    /** For playlist details screen (playlist header + songs) */
    fun getPlaylistWithSongs(id: Long): Flow<PlaylistWithSongs> =
        playlistDao.getPlaylistWithSongs(id)

    /** Create an empty playlist and return its id */
    suspend fun createPlaylist(name: String): Long {
        return playlistDao.insertPlaylist(PlaylistEntity(name = name))
    }

    /** Rename an existing playlist */
    suspend fun renamePlaylist(id: Long, newName: String) {
        playlistDao.renamePlaylist(id, newName)
    }

    /** Remove playlist and all its song links */
    suspend fun deletePlaylist(id: Long) {
        playlistDao.deleteAllSongsFromPlaylist(id)
        playlistDao.deletePlaylistById(id)
    }
    /** Add a song (by its song.path) to playlist */
    suspend fun addSong(playlistId: Long, path: String) {
        playlistDao.addSongToPlaylist(PlaylistSongCrossRef(playlistId, path))
    }

    /** Remove a song from playlist */
    suspend fun removeSong(playlistId: Long, path: String) {
        playlistDao.removeSongFromPlaylist(playlistId, path)
    }
}