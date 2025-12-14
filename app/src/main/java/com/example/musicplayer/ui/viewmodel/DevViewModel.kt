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
import com.example.musicplayer.player.exoplayer.PlayerHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

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
    private val songRepo = SongRepositoryImpl(db.songDao(), scanner)
    private val playlistRepo = PlaylistRepositoryImpl(db.playlistDao())

    private val player = PlayerHolder(application)

    private fun append(msg: String) {
        _log.value += "\n$msg"
    }

    // ================================================================
    // SONG SCANNING
    // ================================================================
    fun scanMusic() = viewModelScope.launch {
        append("Scan started")
        try {
            songRepo.refreshSongs()
            append("Scan finished. Songs inserted into DB.")
        } catch (e: Exception) {
            append("Scan error: ${e.message}")
        }
    }

    fun showAllSongs() = viewModelScope.launch {
        songRepo.getAllSongs().collect { list ->
            append("Songs in DB: ${list.size}")
            list.forEach {
                append("- ${it.path}: ${it.title} | ${it.artist}")
            }
        }
    }

    // ================================================================
    // PLAYLIST MANAGEMENT
    // ================================================================
    fun createPlaylist(name: String) = viewModelScope.launch {
        try {
            playlistRepo.createPlaylist(name)
            append("Playlist created: $name")
        } catch (e: Exception) {
            append("Error creating playlist: ${e.message}")
        }
    }

    fun showPlaylists() = viewModelScope.launch {
        playlistRepo.getAllPlaylists().collect { lists ->

            append("Playlists: ${lists.size}")

            lists.forEach {
                append("- ${it.playlistId}: ${it.name}")
            }
        }
    }


    fun deletePlaylist(id: Long) = viewModelScope.launch {
        try {
            playlistRepo.deletePlaylist(id)
            append("Playlist $id deleted")
        } catch (e: Exception) {
            append("Delete error: ${e.message}")
        }
    }

    fun renamePlaylist(id: Long, newName: String) = viewModelScope.launch {
        try {
            playlistRepo.renamePlaylist(id, newName)
            append("Playlist $id renamed to $newName")
        } catch (e: Exception) {
            append("Rename error: ${e.message}")
        }
    }

    fun addSongToPlaylist(songPath: String, playlistId: Long) = viewModelScope.launch {
        try {
            playlistRepo.addSong(playlistId, songPath)
            append("Added $songPath → playlist $playlistId")
        } catch (e: Exception) {
            append("Add error: ${e.message}")
        }
    }

    fun showSongsFromPlaylist(playlistId: Long) = viewModelScope.launch {
        try {
            playlistRepo.getPlaylistWithSongs(playlistId).collect { pl ->

                append("Playlist ${pl.playlist.name}: ${pl.songs.size} songs")

                pl.songs.forEach { s ->
                    append("- ${s.title} (${s.artist})")
                }
            }
        } catch (e: Exception) {
            append("Error: ${e.message}")
        }
    }

    // ================================================================
    // PLAYER CONTROLS
    // ================================================================

    fun playFirstSong() = viewModelScope.launch {
        val songs = songRepo.getAllSongs().firstOrNull()

        if (songs == null || songs.isEmpty()) {
            append("No songs in DB. Scan first!")
            return@launch
        }

        val first = songs.first()
        append("Playing first song: ${first.title}")
        player.play(first)
    }
    fun playSong(song: SongEntity) {
        append("Playing: ${song.path}")
        player.play(song)
    }

    fun pausePlayback() {
        player.pause()
        append("Paused playback.")
    }

    fun resumePlayback() {
        player.resume()
        append("Resumed playback.")
    }

    fun stopPlayback() {
        player.stop()
        append("Stopped playback.")
    }

    fun nextTrack() {
        player.nextInQueue()
        append("Next track")
    }

    fun previousTrack() {
        player.previousInQueue()
        append("Previous track")
    }

    fun showCurrentSong() {
        val meta = player.currentMetadata.value
        if (meta != null) {
            append("Now playing: ${meta.title} by ${meta.artist}")
        } else {
            append("No song playing.")
        }
    }
}
