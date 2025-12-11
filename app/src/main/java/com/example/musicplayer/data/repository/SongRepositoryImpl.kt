package com.example.musicplayer.data.repository

import com.example.musicplayer.data.local.dao.SongDao
import com.example.musicplayer.data.local.entities.SongEntity
import com.example.musicplayer.data.scanner.FileScanner
import kotlinx.coroutines.flow.Flow

class SongRepositoryImpl (
    private val songDao: SongDao,
    private val fileScanner: FileScanner
) {
    /** Flow for displaying all songs in UI */
    fun getAllSongs(): Flow<List<SongEntity>> = songDao.getAllSongs()

    /** For a details screen or to get a specific song by its path */
    suspend fun getSongByPath(path: String): SongEntity? = songDao.getSongByPath(path)

    suspend fun renameSong(path: String, newTitle: String) {
        songDao.renameSong(path, newTitle)
    }

    /**
     * Full rescan: scans device, maps to SongEntity,
     * wipes old table, inserts new songs.
     */
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