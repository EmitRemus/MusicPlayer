package com.example.musicplayer.ui.screens.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicplayer.data.local.entities.PlaylistEntity
import com.example.musicplayer.data.local.entities.SongEntity
import com.example.musicplayer.ui.composables.EditPlaylistAlert
import com.example.musicplayer.ui.composables.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    playlist: PlaylistEntity,
    songs: List<SongEntity>,
    onSongUpdate: (SongEntity) -> Unit,
    onSongAdd: (String, List<Long>) -> Unit,
    onPlaylistUpdate: (PlaylistEntity) -> Unit,
    onPlaylistDelete: (Long) -> Unit,
    onOpenPlayer: (SongEntity) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(playlist.name) },
                actions = {
                    Box {
                        IconButton (onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Edit Playlist")
                        }
                        DropdownMenu (
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit Details") },
                                leadingIcon = { Icon(Icons.Default.Edit, null) },
                                onClick = {
                                    showMenu = false
                                    showEditDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete Playlist") },
                                leadingIcon = { Icon(Icons.Default.Delete, null) },
                                onClick = {
                                    showMenu = false
                                    onPlaylistDelete(playlist.playlistId)
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            items(songs) { song ->
                Song(
                    song = song,
                    onPlay = { onOpenPlayer(song) },
                    onUpdate = onSongUpdate,
                    onAdd = onSongAdd,
                )
            }
        }
    }

    if (showEditDialog) {
        EditPlaylistAlert(
            playlist = playlist,
            onHide = { showEditDialog = false },
            onProceed = onPlaylistUpdate
        )
    }

}

