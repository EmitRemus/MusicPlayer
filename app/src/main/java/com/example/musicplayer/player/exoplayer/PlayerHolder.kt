package com.example.musicplayer.player.exoplayer

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem


class PlayerHolder(context: Context){
    private val exoPlayer = ExoPlayer.Builder(context).build()

    fun play(path:String) {
        val mediaItem = MediaItem.fromUri(path)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun pause() = exoPlayer.pause()
    fun resume() = exoPlayer.play()
    fun stop() = exoPlayer.stop()

    fun seekTo(position: Long) = exoPlayer.seekTo(position)

    fun isPlaying(): Boolean = exoPlayer.isPlaying

    fun release() = exoPlayer.release()
}