package com.example.musicplayer.ui.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.musicplayer.data.local.database.AppDatabase
import com.example.musicplayer.data.repository.PlaylistRepositoryImpl
import com.example.musicplayer.data.repository.SongRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.musicplayer.data.scanner.FileScanner
import com.example.musicplayer.player.service.MusicService
import com.example.musicplayer.player.service.MusicServiceHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.compose
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class DevViewModel(application: Application) : AndroidViewModel(application) {
    private val _log = MutableStateFlow("No logs yet.")
    val log = _log.asStateFlow()

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "music.db"
    )
        .fallbackToDestructiveMigration()
        .build()

    private val scanner = FileScanner(application)
    private val playlistRepo = PlaylistRepositoryImpl(db.playlistDao())
    private val repository = SongRepositoryImpl(db.songDao(), scanner)

//    private val vmScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private fun appendLog(text: String) {
        _log.value = _log.value + "\n" + text
    }


    fun scanMusic() {
        viewModelScope.launch {
            appendLog("Scan started")
                try {
                    repository.refreshSongs()
                    appendLog("Done! Songs inserted into database.")
                } catch (e: Exception){
                    appendLog("Error: ${e.message}")
                }

//            val songs = scanner.scanAndOrganize()
//            appendLog("Found ${songs.size} songs:")
//            songs.take(10).forEach {
//                appendLog("${it.title} — ${it.artist}")
//            }
//
//            if (songs.size > 10) appendLog("and more")
        }
    }

    fun showAllSongs() {
        viewModelScope.launch {
            repository.getAllSongs().collect { list ->
                appendLog("Songs in DB: ${list.size}")
                list.forEach {
                    appendLog("- ${it.title} | ${it.artist}")
                }
            }
        }
    }
    fun testPlaySong() {
        viewModelScope.launch {
            val songs = repository.getAllSongs().first()
            if (songs.isEmpty()) {
                appendLog("No songs to play")
                return@launch
            }

            val firstSongPath = songs.first().path

            val intent = Intent(getApplication(), MusicService::class.java)
            intent.putExtra("path", firstSongPath)

            getApplication<Application>().startForegroundService(intent)

            appendLog("Started playing: ${songs.first().title}")
        }
    }

    fun pauseSong() {
        try {
            MusicServiceHolder.service?.pausePlayback()
            appendLog("Paused song")
        } catch (e: Exception) {
            appendLog("Error while pausing: ${e.message}")
        }
    }

    fun resumeSong() {
        try {
            MusicServiceHolder.service?.resumePlayback()
            appendLog("resumed song")
        } catch (e: Exception) {
            appendLog("Error while resuming: ${e.message}")
        }
    }

    fun createTestPlaylist() {
        viewModelScope.launch {
            try {
                val id = playlistRepo.createPlaylist("Test playlist")
                appendLog("playlist created")
            }catch (e: Exception){
                appendLog("Playlist creation failed: ${e.message}")
            }
        }
    }

    fun showAllPlaylists() {
        viewModelScope.launch {
            playlistRepo.getAllPlaylists().collect { list ->
                appendLog("Playlists in DB: ${list.size}")
                list.forEach {
                    appendLog("- ${it.playlistId}: ${it.name}")
                }
            }
        }
    }

    fun addFirstSongToPlaylist() {
        viewModelScope.launch {
            try {
                val playlists = playlistRepo.getAllPlaylists().first()
                if (playlists.isEmpty()) {
                    appendLog("No playlists found.")
                    return@launch
                }

                val playlist = playlists.first()
                val playlistId = playlist.playlistId

                try {
                    val allSongs = repository.getAllSongs().first()

                    if (allSongs.isEmpty()) {
                        appendLog("No songs in DB, scan first!")
                        return@launch
                    }

                    val firstSong = allSongs.first()
                    playlistRepo.addSong(playlistId, firstSong.path)

                    appendLog("Added: ${firstSong.title} to playlist $playlistId")
                } catch (e: Exception) {
                    appendLog("Error adding song: ${e.message}")
                }
            }catch (e: Exception) {
                appendLog("Error adding song to playlist: ${e.message}")

            }

        }
    }

}
