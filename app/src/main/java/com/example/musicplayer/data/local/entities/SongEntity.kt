package com.example.musicplayer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey val path: String,
    val title: String,
    val artist: String,
    val duration: Long,
    val album: String?
)