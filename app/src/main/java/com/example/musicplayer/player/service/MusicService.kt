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
import com.example.musicplayer.player.exoplayer.PlayerHolder
import com.example.musicplayer.ui.MainActivity

class MusicService : Service() {

    private lateinit var playerHolder: PlayerHolder

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        MusicServiceHolder.service = this
        playerHolder = PlayerHolder(this)

    }

    override fun onDestroy() {
        MusicServiceHolder.service = null
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        // Required for foreground service
        startForeground(1, createNotification())
        val path = intent?.getStringExtra("path")
        if (path != null) {
            playerHolder.play(path)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createEmptyNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        return Notification.Builder(this, "music_channel")
            .setContentTitle("Music Player")
            .setContentText("Playing audio…")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pIntent)
            .build()
    }

    fun play(path: String) = playerHolder.play(path)
    fun pausePlayback() = playerHolder.pause()
    fun resumePlayback() = playerHolder.resume()

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "music_channel",
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
    private fun createNotification(): Notification {
        val channelId = "music_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Music Player")
            .setContentText("Playing audio…")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .build()
    }

}