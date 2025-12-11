package com.example.musicplayer.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.musicplayer.data.local.database.AppDatabase
import com.example.musicplayer.data.repository.PlaylistRepositoryImpl
import com.example.musicplayer.data.repository.SongRepositoryImpl
import com.example.musicplayer.data.scanner.FileScanner
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "music.db"
    ).build()

    private val playlistRepo = PlaylistRepositoryImpl(db.playlistDao())
    private val songRepo = SongRepositoryImpl(db.songDao(), FileScanner(application))

    val playlists = playlistRepo.getAllPlaylists().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun createPlaylist(name: String) {
        viewModelScope.launch { playlistRepo.createPlaylist(name) }
    }

    fun addSongToPlaylist(playlistId: Long, songPath: String) {
        viewModelScope.launch { playlistRepo.addSong(playlistId, songPath) }
    }

    fun getSongsInPlaylist(id: Long) =
        playlistRepo.getPlaylistWithSongs(id)
}
