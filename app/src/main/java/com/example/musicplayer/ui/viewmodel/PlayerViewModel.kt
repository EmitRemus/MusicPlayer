package com.example.musicplayer.ui.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.example.musicplayer.player.service.MusicService
import com.example.musicplayer.player.service.MusicServiceHolder

class PlayerViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application

    fun play(path: String) {
        val intent = Intent(context, MusicService::class.java)
        intent.putExtra("path", path)
        context.startForegroundService(intent)
    }

    fun pause() {
        MusicServiceHolder.service?.pausePlayback()
    }

    fun resume() {
        MusicServiceHolder.service?.resumePlayback()
    }
}
