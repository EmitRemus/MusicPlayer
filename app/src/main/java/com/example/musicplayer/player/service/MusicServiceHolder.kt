package com.example.musicplayer.player.service

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object MusicServiceHolder {
    private val _service = MutableStateFlow<MusicService?>(null)
    val serviceFlow = _service.asStateFlow()

    var service: MusicService?
        get() = _service.value
        set(value) { _service.value = value }
}