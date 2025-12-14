package com.example.musicplayer.ui

import android.os.Bundle
import android.os.Build
import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import com.example.musicplayer.player.service.MusicService
import com.example.musicplayer.player.service.MusicServiceHolder
import com.example.musicplayer.theme.DarkScheme
import com.example.musicplayer.theme.Typography
import com.example.musicplayer.ui.screens.app.MusicAppNavHost

@Composable
fun MusicPlayerAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkScheme,
        typography = Typography,
        content = content
    )
}

class MainActivity : ComponentActivity() {
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val musicBinder = binder as MusicService.MusicBinder
            MusicServiceHolder.service = musicBinder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            MusicServiceHolder.service = null
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, MusicService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MusicService::class.java)

        startForegroundService(intent)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
            100
        )

        setContent {
            MusicPlayerAppTheme {
                MusicAppNavHost()
            }
        }
    }
}