package com.example.musicplayer.ui.screens.dev

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DevScreen(
    onScanClicked: () -> Unit,
    onShowSongsClicked: () -> Unit,
    onTestPlayClicked: () -> Unit,
    onCreatePlaylistClicked: (String) -> Unit,
    onShowPlaylistsClicked: () -> Unit,
    onAddFirstSongToPlaylistClicked: () -> Unit,
    onPauseClicked: () -> Unit,
    onResumeClicked: () -> Unit,
    logOutput: String
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Text("Developer Test Screen", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        Button(onClick = onScanClicked, modifier = Modifier.fillMaxWidth()) {
            Text("Scan Music Folder")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = onShowSongsClicked, modifier = Modifier.fillMaxWidth()) {
            Text("Show Songs from DB")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = onTestPlayClicked, modifier = Modifier.fillMaxWidth()) {
            Text("Test Play First Song")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onPauseClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pause")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onResumeClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Resume")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = { onCreatePlaylistClicked("MyPlaylist") }, modifier = Modifier.fillMaxWidth()) {
            Text("Create Playlist: MyPlaylist")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = onShowPlaylistsClicked, modifier = Modifier.fillMaxWidth()) {
            Text("Show All Playlists")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onAddFirstSongToPlaylistClicked, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add FIRST song to Playlist 1")
        }



        Spacer(Modifier.height(16.dp))

        Text("Logs:", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        Text(
            text = logOutput,
            modifier = Modifier.fillMaxWidth()
        )

    }
}