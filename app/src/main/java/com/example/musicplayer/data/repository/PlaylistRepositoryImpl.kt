package com.example.musicplayer.data.repository

import com.example.musicplayer.data.local.dao.PlaylistDao
import com.example.musicplayer.data.local.entities.PlaylistEntity
import com.example.musicplayer.data.local.entities.PlaylistSongCrossRef

class PlaylistRepositoryImpl (
    private val playlistDao: PlaylistDao
) {
    fun getAllPlaylists() = playlistDao.getAllPlaylists()

    fun getPlaylistWithSongs(id: Long) = playlistDao.getPlaylistWithSongs(id)

    suspend fun createPlaylist(name: String): Long {
        return playlistDao.insertPlaylist(PlaylistEntity(name = name))
    }

    suspend fun addSong(playlistId: Long, path: String) {
        playlistDao.addSongToPlaylist(PlaylistSongCrossRef(playlistId, path))
    }

    suspend fun removeSong(playlistId: Long, path: String) {
        playlistDao.removeSongFromPlaylist(PlaylistSongCrossRef(playlistId, path))
    }
}