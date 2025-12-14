package com.example.musicplayer.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.musicplayer.data.local.database.AppDatabase
import com.example.musicplayer.data.local.entities.SongEntity
import com.example.musicplayer.data.repository.PlaylistRepositoryImpl
import com.example.musicplayer.data.repository.SongRepositoryImpl
import com.example.musicplayer.data.scanner.FileScanner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "music.db"
    ).build()

    private val playlistRepo = PlaylistRepositoryImpl(db.playlistDao())
    private val _playlistSongs = MutableStateFlow<List<SongEntity>>(emptyList())
    val playlistSongs: StateFlow<List<SongEntity>> = _playlistSongs.asStateFlow()

    val playlists = playlistRepo.getAllPlaylists().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun createPlaylist(name: String) {
        viewModelScope.launch { playlistRepo.createPlaylist(name) }
    }

    fun addSongToPlaylists(songPath: String, playlistIds: List<Long>) {
        playlistIds.forEach { it -> viewModelScope.launch { playlistRepo.addSong(it, songPath) }}
    }

    fun renamePlaylist(id: Long, name: String) = viewModelScope.launch {
        viewModelScope.launch { playlistRepo.renamePlaylist(id, name) }
    }

    fun deletePlaylist(id: Long) = viewModelScope.launch {
        viewModelScope.launch { playlistRepo.deletePlaylist(id) }
    }

    fun getSongsInPlaylist(id: Long) =
        viewModelScope.launch {
            _playlistSongs.value = playlistRepo.getSongsFromPlaylist(id)
        }}
