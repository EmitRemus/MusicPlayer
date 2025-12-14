package com.example.musicplayer.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import com.example.musicplayer.data.local.entities.PlaylistEntity

@Composable
fun EditPlaylistAlert(
    playlist: PlaylistEntity,
    onHide: () -> Unit,
    onProceed: (PlaylistEntity) -> Unit,
) {
    var name by remember { mutableStateOf(playlist.name) }

    AlertDialog(
        onDismissRequest = onHide,
        title = { Text("Edit Playlist") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Playlist Name") }
                )
                Spacer(Modifier.height(8.dp))
                Button (onClick = { /* Launch Image Picker */ }) {
                    Text("Change Cover Image")
                }
            }
        },
        confirmButton = {
            TextButton (onClick = {
                onProceed(playlist.copy(name = name))
                onHide()
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onHide) { Text("Cancel") }
        }
    )
}