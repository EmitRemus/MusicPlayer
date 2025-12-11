package com.example.musicplayer.ui.screens.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.musicplayer.domain.models.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    songs: List<Song>,
    name: String,
    onOpenPlayer: (Song) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(name) }) }
    ) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            items(songs) { song ->
                ListItem(
                    headlineContent = { Text(song.title) },
                    supportingContent = { Text(song.artist) },
                    modifier = Modifier.clickable { onOpenPlayer(song) }
                )
            }
        }
    }
}

