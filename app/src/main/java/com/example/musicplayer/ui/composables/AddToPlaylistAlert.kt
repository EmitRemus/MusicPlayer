package com.example.musicplayer.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
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
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(14.dp),

        title = {
            Text(
                "Add to Playlists",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
            ) {
                items(playlists, key = { it.playlistId }) { playlist ->
                    val isChecked = selectedPlaylists.contains(playlist.playlistId)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                selectedPlaylists = if (isChecked) {
                                    selectedPlaylists - playlist.playlistId
                                } else {
                                    selectedPlaylists + playlist.playlistId
                                }
                            }
                            .padding(vertical = 4.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { }
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            playlist.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onProceed(song.path, selectedPlaylists.toList())
                    onHide()
                },
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Confirm", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onHide) {
                Text("Cancel", fontWeight = FontWeight.Normal)
            }
        }
    )
}
