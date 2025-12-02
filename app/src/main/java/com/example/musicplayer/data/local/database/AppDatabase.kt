package com.example.musicplayer.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musicplayer.data.local.dao.PlaylistDao
import com.example.musicplayer.data.local.dao.SongDao
import com.example.musicplayer.data.local.entities.PlaylistEntity
import com.example.musicplayer.data.local.entities.PlaylistSongCrossRef
import com.example.musicplayer.data.local.entities.SongEntity

@Database(
    entities = [
        SongEntity::class,
        PlaylistEntity::class,
        PlaylistSongCrossRef::class],
    version = 2,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
}