package com.example.musicplayer.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun CreatePlaylistAlert(
    onHide: () -> Unit,
    onProceed: (String) -> Unit,
) {
    var playlistName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onHide,
        title = { Text("Create Playlist") },
        text = {
            Column {
                OutlinedTextField(
                    value = playlistName,
                    onValueChange = { playlistName = it },
                    label = { Text("Playlist name") }
                )
            }
        },
        confirmButton = {
            TextButton (
                onClick = {
                    if (playlistName.isNotBlank()) {
                        onProceed(playlistName.trim())
                        playlistName = ""
                        onHide()
                    }
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onHide) {
                Text("Cancel")
            }
        }
    )
}