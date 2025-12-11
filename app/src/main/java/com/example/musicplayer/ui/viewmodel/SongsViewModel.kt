package com.example.musicplayer.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.musicplayer.data.local.database.AppDatabase
import com.example.musicplayer.data.local.entities.SongEntity
import com.example.musicplayer.data.repository.SongRepositoryImpl
import com.example.musicplayer.data.scanner.FileScanner
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "music.db"
    ).build()
    private val scanner = FileScanner(application)
    private val repo = SongRepositoryImpl(db.songDao(), scanner)

    val songs: StateFlow<List<SongEntity>> =
        repo.getAllSongs()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    init {
        viewModelScope.launch { repo.refreshSongs() }
    }

    fun editSong(song: SongEntity) {
        viewModelScope.launch {
            repo.editSong(song.path, song.title, song.artist)
        }
    }
}

