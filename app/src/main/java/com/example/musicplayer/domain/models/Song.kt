package com.example.musicplayer.domain.models

data class Song(
    val path: String,
    val title: String,
    val artist: String,
    val duration: Long,
    val album: String?
)