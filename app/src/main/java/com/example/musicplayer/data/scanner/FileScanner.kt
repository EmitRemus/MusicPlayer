package com.example.musicplayer.data.scanner

import android.content.Context
import android.media.MediaMetadataRetriever
import android.os.Environment
import java.io.File
import java.io.IOException

class FileScanner(private val context: Context) {

    private val allowedExtensions = listOf("mp3", "wav", "flac", "ogg", "m4a")

    fun scanAndOrganize(): List<ScannedSong> {
        moveAudioFilesFromDownloads()
        return scanMusicFolder()
    }

    private fun moveAudioFilesFromDownloads() {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val musicDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)

        if (!downloadsDir.exists()) return

        downloadsDir.listFiles()?.forEach { file ->
            if (file.isFile && isAudioFile(file)) {
                val targetFile = File(musicDir, file.name)
                try {
                    file.copyTo(targetFile, overwrite = true)
                    file.delete() // Remove original from Downloads
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun scanMusicFolder(): List<ScannedSong> {
        val musicDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)

        val songs = mutableListOf<ScannedSong>()

        if (!musicDir.exists()) return songs

        musicDir.listFiles()?.forEach { file ->
            if (file.isFile && isAudioFile(file)) {
                val metadata = extractMetadata(file)
                if (metadata != null) songs.add(metadata)
            }
        }

        return songs
    }

    private fun isAudioFile(file: File): Boolean {
        val ext = file.extension.lowercase()
        return allowedExtensions.contains(ext)
    }

    private fun extractMetadata(file: File): ScannedSong? {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(file.absolutePath)

            val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                ?: file.nameWithoutExtension

            val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                ?: "Unknown Artist"

            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?.toLongOrNull() ?: 0L

            val album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                ?: "Unknown Album"

            retriever.release()

            ScannedSong(
                path = file.absolutePath,
                title = title,
                artist = artist,
                duration = duration,
                album = album
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

data class ScannedSong(
    val path: String,
    val title: String,
    val artist: String,
    val duration: Long,
    val album: String?
)
