package com.example.musicplayer.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.local.entities.SongEntity
import com.example.musicplayer.player.exoplayer.NowPlayingMetadata
import com.example.musicplayer.player.service.MusicServiceHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(application: Application) : AndroidViewModel(application) {
    var position by mutableLongStateOf(0L)
    var duration by mutableLongStateOf(0L)
    var isPlaying by mutableStateOf(false)
    var metadata by mutableStateOf<NowPlayingMetadata?>(null)

    init {
        viewModelScope.launch {
            MusicServiceHolder.serviceFlow.collect { service ->
                if (service != null) {
                    launch {
                        while (isActive) {
                            position = service.currentPosition()
                            duration = service.duration()
                            isPlaying = service.isPlaying()
                            delay(500)
                        }
                    }

                    launch {
                        service.playerHolder.currentMetadata.collect { newMetadata ->
                            metadata = newMetadata
                        }
                    }
                }
            }
        }
    }

    fun play(song: SongEntity) {
        MusicServiceHolder.service?.playSong(song)
    }

    fun playPlaylist(songs: List<SongEntity>, index: Int = 0) {
        MusicServiceHolder.service?.playQueue(songs, index)
    }

    fun seekTo(position: Long) {
        MusicServiceHolder.service?.seekTo(position)
    }

    fun pause() {
        MusicServiceHolder.service?.pausePlayback()
    }

    fun resume() {
        MusicServiceHolder.service?.resumePlayback()
    }

    fun next() {
        MusicServiceHolder.service?.next()
    }

    fun previous() = {
        MusicServiceHolder.service?.previous()
    }

}



