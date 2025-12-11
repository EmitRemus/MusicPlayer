package com.example.musicplayer.player.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.musicplayer.data.local.entities.SongEntity
import com.example.musicplayer.player.exoplayer.PlayerHolder
import com.example.musicplayer.ui.MainActivity

class MusicService : Service() {

    private lateinit var playerHolder: PlayerHolder

    private val queue: MutableList<String> = mutableListOf()
    private var currentIndex: Int = -1

//    override fun onCreate() {
//        super.onCreate()
//        createNotificationChannel()
//        MusicServiceHolder.service = this
//        playerHolder = PlayerHolder(this)
//
//    }
    override fun onCreate() {
        super.onCreate()
        playerHolder = PlayerHolder(this)
        MusicServiceHolder.service = this
    }

//    override fun onDestroy() {
//        MusicServiceHolder.service = null
//        super.onDestroy()
//    }
    override fun onDestroy() {
        MusicServiceHolder.service = null
        playerHolder.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
////        // Required for foreground service
//        startForeground(1, createNotification())
//        val path = intent?.getStringExtra("path")
//        if (path != null) {
//            playerHolder.play(path)
//        }
//        return START_STICKY
//    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Service will always run as a foreground service once started
        val notification = buildNotification()
        startForeground(NOTIFICATION_ID, notification)
        return START_STICKY
    }

//    override fun onBind(intent: Intent?): IBinder? = null

//    private fun createEmptyNotification(): Notification {
//        val intent = Intent(this, MainActivity::class.java)
//        val pIntent = PendingIntent.getActivity(
//            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
//        )
//
//        return Notification.Builder(this, "music_channel")
//            .setContentTitle("Music Player")
//            .setContentText("Playing audio…")
//            .setSmallIcon(android.R.drawable.ic_media_play)
//            .setContentIntent(pIntent)
//            .build()
//    }

    // THIS IS API FOR UI BASICALLY

    /** Play a single song and reset the queue */
    fun playSong(path: String) {
        queue.clear()
        queue.add(path)
        currentIndex = 0
        playerHolder.play(path)
    }

    /** Play a list of songs as a queue starting from [startIndex] */
    fun playQueue(paths: List<String>, startIndex: Int = 0) {
        if (paths.isEmpty()) return
        queue.clear()
        queue.addAll(paths)
        currentIndex = startIndex.coerceIn(0, queue.lastIndex)
        playerHolder.play(queue[currentIndex])
    }

//    fun play(path: String) = playerHolder.play(path)

    fun pausePlayback() {
        playerHolder.pause()
    }
    fun resumePlayback() {
        playerHolder.resume()
    }

    fun stopPlayback() {
        playerHolder.stop()
        // Detach foreground state, keep service alive
        stopForeground(STOP_FOREGROUND_DETACH)
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

//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "music_channel",
//                "Music Playback",
//                NotificationManager.IMPORTANCE_LOW
//            )
//
//            val manager = getSystemService(NotificationManager::class.java)
//            manager.createNotificationChannel(channel)
//        }
//    }
//    private fun createNotification(): Notification {
//        val channelId = "music_channel"
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "Music Playback",
//                NotificationManager.IMPORTANCE_LOW
//            )
//            val manager = getSystemService(NotificationManager::class.java)
//            manager.createNotificationChannel(channel)
//        }
//
//        return NotificationCompat.Builder(this, channelId)
//            .setContentTitle("Music Player")
//            .setContentText("Playing audio…")
//            .setSmallIcon(android.R.drawable.ic_media_play)
//            .build()
//    }
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