package com.example.musicplayer.data.repository

import com.example.musicplayer.data.local.dao.SongDao
import com.example.musicplayer.data.local.entities.SongEntity
import com.example.musicplayer.data.scanner.FileScanner
import kotlinx.coroutines.flow.Flow

class SongRepositoryImpl (
    private val songDao: SongDao,
    private val fileScanner: FileScanner
) {
    fun getAllSongs(): Flow<List<SongEntity>> {
        return songDao.getAllSongs()
    }

    suspend fun refreshSongs() {
        val scanned = fileScanner.scanAndOrganize()

        val entities = scanned.map {
            SongEntity(
                path = it.path,
                title = it.title,
                artist = it.artist,
                duration = it.duration,
                album = it.album
            )
        }

        songDao.deleteAll()
        songDao.insertSongs(entities)
    }
}