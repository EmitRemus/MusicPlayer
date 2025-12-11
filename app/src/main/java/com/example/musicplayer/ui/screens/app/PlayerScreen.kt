package com.example.musicplayer.ui.screens.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicplayer.data.local.entities.SongEntity

@Composable
fun PlayerScreen(
    song: SongEntity?,
    onPause: () -> Unit,
    onResume: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Text(song?.title ?: "No song", style = MaterialTheme.typography.headlineSmall)
        Text(song?.artist ?: "")

        Slider(value = 0.3f, onValueChange = {})

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = onPause) {
                Icon(Icons.Filled.PlayArrow, null)
            }
            IconButton(onClick = onResume) {
                Icon(Icons.Default.PlayArrow, null)
            }
        }
    }
}

