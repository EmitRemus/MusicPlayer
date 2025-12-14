package com.example.musicplayer.ui.screens.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onPlaylistPlay: (List<SongEntity>, Int) -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                PlaylistHeader(
                    playlist = playlist,
                    songCount = songs.size,
                    onPlayAll = { onPlaylistPlay(songs, 0) },
                    showMenu = showMenu,
                    onToggleMenu = { showMenu = !showMenu },
                    onDismissMenu = { showMenu = false },
                    onEdit = { showEditDialog = true; showMenu = false },
                    onDelete = { onPlaylistDelete(playlist.playlistId); showMenu = false }
                )
            }

            itemsIndexed(songs) { index, song ->
                Song(
                    song = song,
                    onPlay = { onPlaylistPlay(songs, index) },
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


@Composable
fun PlaylistHeader(
    playlist: PlaylistEntity,
    songCount: Int,
    onPlayAll: () -> Unit,
    showMenu: Boolean,
    onDismissMenu: () -> Unit,
    onToggleMenu: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 64.dp, bottom = 24.dp, start = 20.dp, end = 20.dp)
    ) {
        Text(
            text = playlist.name,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1).sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "$songCount Songs • Updated Recently",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = onPlayAll,
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Play All", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.weight(1f))

            Box {
                IconButton(
                    onClick = onToggleMenu,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = onDismissMenu,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit Details", style = MaterialTheme.typography.bodyMedium) },
                        leadingIcon = { Icon(Icons.Default.Edit, null, modifier = Modifier.size(20.dp)) },
                        onClick = onEdit
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Playlist", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error) },
                        leadingIcon = { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp)) },
                        onClick = onDelete
                    )
                }
            }
        }
    }
}
