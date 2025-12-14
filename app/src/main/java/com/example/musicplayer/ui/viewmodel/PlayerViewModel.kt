package com.example.musicplayer.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.example.musicplayer.data.local.entities.SongEntity
import com.example.musicplayer.player.exoplayer.NowPlayingMetadata
import com.example.musicplayer.player.exoplayer.PlayerHolder
import com.example.musicplayer.player.service.MusicService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerViewModel(application: Application) : AndroidViewModel(application) {
    private val player = PlayerHolder(application)

    val metadata: StateFlow<NowPlayingMetadata> = player.currentMetadata

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    fun play(song: SongEntity) {
        player.play(song)
        _isPlaying.value = true
    }

    fun playPlaylist(songs: List<SongEntity>) {
        player.playWholePlaylist(songs)
    }

    fun pause() {
        player.pause()
        _isPlaying.value = false
    }

    fun resume() {
        player.resume()
        _isPlaying.value = true
    }

    fun next() = player.nextInQueue()
    fun previous() = player.previousInQueue()

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}



