package com.example.musicplayer.player.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.compose.runtime.collectAsState
import androidx.core.app.NotificationCompat
import com.example.musicplayer.data.local.entities.SongEntity
import com.example.musicplayer.player.exoplayer.NowPlayingMetadata
import com.example.musicplayer.player.exoplayer.PlayerHolder
import com.example.musicplayer.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MusicService : Service() {

    private lateinit var playerHolder: PlayerHolder
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private val queue: MutableList<SongEntity> = mutableListOf()
    private var currentIndex: Int = -1

    private val _metadata = MutableStateFlow<NowPlayingMetadata?>(null)
    val metadata: StateFlow<NowPlayingMetadata?> get() = _metadata
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying

    override fun onCreate() {
        super.onCreate()
        playerHolder = PlayerHolder(this)

        serviceScope.launch {
            playerHolder.currentMetadata.collect { newMetadata ->
                _metadata.value = newMetadata
            }
        }

        MusicServiceHolder.service = this
    }


    override fun onDestroy() {
        MusicServiceHolder.service = null
        playerHolder.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Service will always run as a foreground service once started
        val notification = buildNotification()
        startForeground(NOTIFICATION_ID, notification)
        return START_STICKY
    }


    // THIS IS API FOR UI BASICALLY

    /** Play a single song and reset the queue */
    fun playSong(song: SongEntity) {
        queue.clear()
        queue.add(song)
        currentIndex = 0
        playerHolder.play(song)
        _isPlaying.value = true
    }

    /** Play a list of songs as a queue starting from [startIndex] */
    fun playQueue(songs: List<SongEntity>, startIndex: Int = 0) {
        if (songs.isEmpty()) return
        queue.clear()
        queue.addAll(songs)
        currentIndex = startIndex.coerceIn(0, queue.lastIndex)
        playerHolder.play(queue[currentIndex])
        _isPlaying.value = true
    }

    fun pausePlayback() {
        playerHolder.pause()
        _isPlaying.value = false
    }
    fun resumePlayback() {
        playerHolder.resume()
        _isPlaying.value = true
    }

    fun stopPlayback() {
        playerHolder.stop()
        // Detach foreground state, keep service alive
        stopForeground(STOP_FOREGROUND_DETACH)
        _isPlaying.value = false
    }

    fun next() {
        if (queue.isEmpty()) return
        if (currentIndex < queue.lastIndex) {
            currentIndex++
            playerHolder.play(queue[currentIndex])
        }
    }

    fun previous() {
        if (queue.isEmpty()) return
        if (currentIndex > 0) {
            currentIndex--
            playerHolder.play(queue[currentIndex])
        }
    }

    fun seekTo(positionMs: Long) {
        playerHolder.seekTo(positionMs)
    }

    fun isPlaying(): Boolean = playerHolder.isPlaying()

    fun currentPosition(): Long = playerHolder.getCurrentPosition()

    fun duration(): Long = playerHolder.getDuration()

    fun getQueue() = playerHolder.queue
    fun setQueue(list: List<SongEntity>, startIndex: Int = 0) = playerHolder.setQueue(list, startIndex)
    fun addToQueue(song: SongEntity) = playerHolder.addToQueue(song)
    fun clearQueue() = playerHolder.clearQueue()


    // NOTIFICATION STUFF SO THAT APP DOES NOT BLOW ITSELF UP

    private fun buildNotification(): Notification {
        val channelId = "music_playback"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val activityIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Music Player")
            .setContentText("Playing audio…")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
    }
}