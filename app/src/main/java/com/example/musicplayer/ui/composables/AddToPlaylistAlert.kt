package com.example.musicplayer.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicplayer.data.local.entities.SongEntity
import com.example.musicplayer.ui.viewmodel.PlaylistsViewModel

@Composable
fun AddToPlaylistDialog(
    song: SongEntity,
    onHide: () -> Unit,
    onProceed: (String, List<Long>) -> Unit
) {
    var selectedPlaylists by remember { mutableStateOf(setOf<Long>()) }

    val playlistsVM: PlaylistsViewModel = viewModel()
    val playlists by playlistsVM.playlists.collectAsState()

    AlertDialog(
        onDismissRequest = onHide,
        title = { Text("Add to Playlists") },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(playlists, key = { it.playlistId }) { playlist ->
                    val isChecked = selectedPlaylists.contains(playlist.playlistId)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {
                                selectedPlaylists = if (isChecked) {
                                    selectedPlaylists - playlist.playlistId
                                } else {
                                    selectedPlaylists + playlist.playlistId
                                }
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(playlist.name, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onProceed(song.path, selectedPlaylists.toList())
                    onHide()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onHide) {
                Text("Cancel")
            }
        }
    )
}
