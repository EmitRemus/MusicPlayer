package com.example.musicplayer.data.repository

import com.example.musicplayer.data.local.dao.SongDao
import com.example.musicplayer.data.local.entities.SongEntity
import com.example.musicplayer.data.scanner.FileScanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SongRepositoryImpl (
    private val songDao: SongDao,
    private val fileScanner: FileScanner
) {
    /** Flow for displaying all songs in UI */
    fun getAllSongs(): Flow<List<SongEntity>> = songDao.getAllSongs()

    /** For a details screen or to get a specific song by its path */
    suspend fun getSongByPath(path: String): SongEntity? = songDao.getSongByPath(path)

    suspend fun editSong(path: String, newTitle: String, newArtist: String) {
        songDao.editSong(path, newTitle, newArtist)
    }

    /** Full rescan: scans device, maps to SongEntity, wipes old table, inserts new songs */
    suspend fun refreshSongs() = withContext(Dispatchers.IO) {
        val scannedFiles = fileScanner.scanAndOrganize()
        val scannedPaths = scannedFiles.map { it.path }.toSet()

        val existingEntities = songDao.getAllSongsNonFlowSuspend()
        val existingPaths = existingEntities.map { it.path }.toSet()

        val pathsToRemove = existingPaths - scannedPaths
        val newPaths = scannedPaths - existingPaths

        val entitiesToAdd = scannedFiles
            .filter { it.path in newPaths }
            .map { scannedSongFile ->
                SongEntity(
                    path = scannedSongFile.path,
                    title = scannedSongFile.title,
                    artist = scannedSongFile.artist,
                    duration = scannedSongFile.duration,
                    album = scannedSongFile.album
                )
            }

        songDao.runInTransaction {
            if (entitiesToAdd.isNotEmpty()) {
                songDao.insertSongs(entitiesToAdd)
            }
            if (pathsToRemove.isNotEmpty()) {
                songDao.deleteSongsByPaths(pathsToRemove)
            }
        }
    }
}