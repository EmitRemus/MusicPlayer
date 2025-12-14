package com.example.musicplayer.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicplayer.data.local.entities.SongEntity

@Composable
fun EditSongAlert (
    song: SongEntity,
    onHide: () -> Unit,
    onProceed: (SongEntity) -> Unit,
) {
    var title by remember { mutableStateOf(song.title) }
    var artist by remember { mutableStateOf(song.artist) }
    var album by remember { mutableStateOf(song.album) }

    AlertDialog(
        onDismissRequest = onHide,
        title = { Text("Edit Song") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Song Name") }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = artist,
                    onValueChange = { artist = it },
                    label = { Text("Artist") }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = album ?: "",
                    onValueChange = { album = it },
                    label = { Text("Album") }
                )
            }
        },
        confirmButton = {
            TextButton (onClick = {
                onProceed(song.copy(title = title, artist = artist, album = album))
                onHide()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onHide) {
                Text("Cancel")
            }
        }
    )
}