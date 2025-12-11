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
                // SONG DB
                onScanClicked = { vm.scanMusic() },
                onShowSongsClicked = { vm.showAllSongs() },

                // PLAYLISTS
                onCreatePlaylistClicked = { vm.createPlaylist("New Playlist") },
                onShowPlaylistsClicked = { vm.showPlaylists() },
                onDeletePlaylistClicked = { id -> vm.deletePlaylist(id) },

                // ADD SONG TO PLAYLIST
//                onAddSongToPlaylistClicked = { songId, playlistId ->
//                    vm.addSongToPlaylist(songId, playlistId)
//                },

                // PLAYBACK
                onTestPlayClicked = {
                    vm.playFirstSong()
                },
//                onPlaySongClicked = { vm.playSong(it) },
                onPauseClicked = { vm.pausePlayback() },
                onResumeClicked = { vm.resumePlayback() },
                onNextClicked = { vm.nextTrack() },
                onPreviousClicked = { vm.previousTrack() },
                onShowCurrentClicked = { vm.showCurrentSong() },

                // LOG OUTPUT
                logOutput = log
            )
        }
    }
}