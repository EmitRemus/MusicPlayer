package com.example.musicplayer.ui.screens.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicplayer.data.local.entities.PlaylistEntity
import com.example.musicplayer.data.local.entities.SongEntity
import com.example.musicplayer.ui.composables.CreatePlaylistAlert
import com.example.musicplayer.ui.composables.Song

@Composable
fun HomeScreen(
    songs: List<SongEntity>,
    playlists: List<PlaylistEntity>,
    onOpenPlaylist: (Long) -> Unit,
    onCreatePlaylist: (String) -> Unit,
    onPlaylistPlay: (List<SongEntity>, Int) -> Unit,
    onSongUpdate: (SongEntity) -> Unit,
    onSongAdd: (String, List<Long>) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            item {
                Text(
                    "Your Playlists",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(16.dp))
            }

            item {
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
                    items(playlists) { playlist ->
                        PlaylistCard(
                            playlist = playlist,
                            onClick = { onOpenPlaylist(playlist.playlistId) }
                        )
                    }
                    item {
                        AddPlaylistCard(onClick = { showDialog = true })
                    }
                }
                Spacer(Modifier.height(40.dp))
            }

            item {
                Text(
                    "Your songs",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(16.dp))
            }

            itemsIndexed(songs) { index, song ->
                Song(
                    song = song,
                    onPlay = { onPlaylistPlay(songs, index) },
                    onUpdate = onSongUpdate,
                    onAdd = onSongAdd
                )
            }
        }
    }

    if (showDialog) {
        CreatePlaylistAlert(
            onProceed = { name -> onCreatePlaylist(name) },
            onHide = { showDialog = false }
        )
    }
}

@Composable
fun PlaylistCard(
    playlist: PlaylistEntity,
    onClick: () -> Unit
) {
    Column( modifier = Modifier.padding(end = 16.dp) ) {
        Card( modifier = Modifier
                .size(150.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder for album art
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            playlist.name,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(150.dp)
        )
    }
}

@Composable
fun AddPlaylistCard(onClick: () -> Unit) {
    Column(modifier = Modifier.padding(end = 16.dp)) {
        Card(
            modifier = Modifier
                .size(150.dp)
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Playlist",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("New Playlist", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.width(150.dp))
    }
}
