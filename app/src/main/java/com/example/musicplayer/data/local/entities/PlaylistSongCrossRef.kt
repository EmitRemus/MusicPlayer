package com.example.musicplayer.data.local.entities

import androidx.room.Entity

@Entity(
    tableName = "playlist_song_crossref",
    primaryKeys = ["playlistId", "songPath"]
)
data class PlaylistSongCrossRef(
    val playlistId: Long,
    val songPath: String
)