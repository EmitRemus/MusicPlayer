package com.example.musicplayer.player.exoplayer

import android.content.Context
import android.net.Uri
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import com.example.musicplayer.data.local.entities.SongEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class NowPlayingMetadata(
    val title: String = "Unknown",
    val artist: String = "Unknown"
)

class PlayerHolder(context: Context){
    private val exoPlayer = ExoPlayer.Builder(context).build()

    private val _currentMetadata = MutableStateFlow(NowPlayingMetadata())
    val currentMetadata: StateFlow<NowPlayingMetadata> = _currentMetadata

    private val _queue = MutableStateFlow<List<SongEntity>>(emptyList())
    val queue = _queue.asStateFlow()

    private var currentIndex = 0


    fun play(path: String) {
        val mediaItem = MediaItem.fromUri(Uri.parse(path))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()

        updateMetadata(mediaItem)
    }

    fun pause() = exoPlayer.pause()
    fun resume() = exoPlayer.play()

    fun stop() = exoPlayer.stop()

    fun nextInQueue() {
        val songs = _queue.value
        if (songs.isEmpty()) return

        currentIndex = (currentIndex + 1) % songs.size
        play(songs[currentIndex].path)
    }

    fun previousInQueue() {
        val songs = _queue.value
        if (songs.isEmpty()) return

        currentIndex = if (currentIndex == 0) songs.lastIndex else currentIndex - 1
        play(songs[currentIndex].path)
    }

    fun seekTo(position: Long) = exoPlayer.seekTo(position)

    fun isPlaying(): Boolean = exoPlayer.isPlaying

    fun getCurrentPosition(): Long = exoPlayer.currentPosition

    private fun updateMetadata(mediaItem: MediaItem) {
        val title = mediaItem.mediaMetadata.title?.toString() ?: "Unknown title"
        val artist = mediaItem.mediaMetadata.artist?.toString() ?: "Unknown artist"

        _currentMetadata.value = NowPlayingMetadata(title, artist)
    }
    fun getDuration(): Long = exoPlayer.duration

    fun setQueue(songs: List<SongEntity>, startIndex: Int = 0) {
        _queue.value = songs
        currentIndex = startIndex
        if (songs.isNotEmpty()) {
            play(songs[startIndex].path)
        }
    }

    fun addToQueue(song: SongEntity) {
        val updated = _queue.value.toMutableList()
        updated.add(song)
        _queue.value = updated
    }

    fun removeFromQueue(index: Int) {
        val updated = _queue.value.toMutableList()
        if (index in updated.indices) {
            updated.removeAt(index)
            _queue.value = updated
        }
    }

    fun clearQueue() {
        _queue.value = emptyList()
        currentIndex = 0
    }

    fun moveQueueItem(from: Int, to: Int) {
        val updated = _queue.value.toMutableList()
        if (from in updated.indices && to in updated.indices) {
            val item = updated.removeAt(from)
            updated.add(to, item)
            _queue.value = updated
        }
    }
    fun skipTo(index: Int) {
        val songs = _queue.value
        if (index in songs.indices) {
            currentIndex = index
            play(songs[index].path)
        }
    }


    fun release() = exoPlayer.release()
}