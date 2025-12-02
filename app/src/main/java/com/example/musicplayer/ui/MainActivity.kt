package com.example.musicplayer.ui

import android.os.Bundle
import android.os.Build
import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicplayer.ui.screens.dev.DevScreen
import com.example.musicplayer.ui.viewmodel.DevViewModel

class MainActivity : ComponentActivity() {

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        setContent {
            val vm: DevViewModel = viewModel()
            val log by vm.log.collectAsState()

            DevScreen(
                onScanClicked = { vm.scanMusic() },
                onShowSongsClicked = { vm.showAllSongs() },
                onTestPlayClicked = { vm.testPlaySong() },

                onCreatePlaylistClicked = { vm.createTestPlaylist() },
                onShowPlaylistsClicked = { vm.showAllPlaylists() },
                onAddFirstSongToPlaylistClicked = {vm.addFirstSongToPlaylist()},
                onPauseClicked = { vm.pauseSong() },
                onResumeClicked = { vm.resumeSong() },
                logOutput = log
            )
        }
    }
}