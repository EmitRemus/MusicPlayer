package com.example.musicplayer.ui.screens.dev

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
@Composable
fun DevScreen(
    // SONGS
    onScanClicked: () -> Unit,
    onShowSongsClicked: () -> Unit,

    // PLAYLISTS
    onCreatePlaylistClicked: () -> Unit,
    onShowPlaylistsClicked: () -> Unit,
    onDeletePlaylistClicked: (Long) -> Unit,

    // PLAYBACK
//    onPlaySongClicked: () -> Unit,
    onPauseClicked: () -> Unit,
    onResumeClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onShowCurrentClicked: () -> Unit,
    onTestPlayClicked: () -> Unit,
    // LOG
    logOutput: String
){
    var playlistIdInput by remember { mutableStateOf("") }
    var songIdInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
        Text("Developer Test Screen", style = MaterialTheme.typography.headlineSmall)

        // ================================
        // SONG DATABASE
        // ================================
        Spacer(Modifier.height(14.dp))
        Button(onClick = onScanClicked) { Text("Scan Music Folder") }
        Button(onClick = onShowSongsClicked) { Text("Show Songs") }

        // ================================
        // PLAYLISTS
        // ================================
        Spacer(Modifier.height(20.dp))
        Button(onClick = onCreatePlaylistClicked) { Text("Create Playlist") }
        Button(onClick = onShowPlaylistsClicked) { Text("Show Playlists") }

        OutlinedTextField(
            value = playlistIdInput,
            onValueChange = { playlistIdInput = it },
            label = { Text("Playlist ID") }
        )
        Button(onClick = {
            playlistIdInput.toLongOrNull()?.let { onDeletePlaylistClicked(it) }
        }) { Text("Delete Playlist") }


        // ================================
        // PLAYBACK
        // ================================
        Spacer(Modifier.height(20.dp))


        Button(onClick = onTestPlayClicked) { Text("Start first song") }
        Button(onClick = onPauseClicked) { Text("Pause") }
        Button(onClick = onResumeClicked) { Text("Resume") }
        Button(onClick = onNextClicked) { Text("Next Track") }
        Button(onClick = onPreviousClicked) { Text("Previous Track") }
        Button(onClick = onShowCurrentClicked) { Text("Show Current Song") }

        // ================================
        // LOG OUTPUT
        // ================================
        Spacer(Modifier.height(20.dp))
        Text("Logs:")
        Text(logOutput)

    }
}