package com.example.musicplayer.ui.screens.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.ui.composables.BottomPlayer
import com.example.musicplayer.ui.viewmodel.PlayerViewModel
import com.example.musicplayer.ui.viewmodel.PlaylistsViewModel
import com.example.musicplayer.ui.viewmodel.SongsViewModel

@Composable
fun MusicAppNavHost() {
    val nav = rememberNavController()

    val songsVM: SongsViewModel = viewModel()
    val playlistsVM: PlaylistsViewModel = viewModel()
    val playerVM: PlayerViewModel = viewModel()

    val songs by songsVM.songs.collectAsState()
    val playlists by playlistsVM.playlists.collectAsState()

    val metadata by playerVM.metadata.collectAsState()
    val isPlaying by playerVM.isPlaying.collectAsState()

    Scaffold(
        bottomBar = {
            BottomPlayer(
                metadata = metadata,
                isPlaying = isPlaying,
                onPause = { playerVM.pause() },
                onResume = { playerVM.resume() },
                onNext = { playerVM.next() },
                onPrevious = { playerVM.previous() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = nav,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    songs = songs,
                    playlists = playlists,
                    onOpenPlaylist = { id -> nav.navigate("playlist/$id") },
                    onCreatePlaylist = { name -> playlistsVM.createPlaylist(name) },
                    onSongPlay = { song -> playerVM.play(song) },
                    onSongUpdate = { song -> songsVM.editSong(song) },
                    onSongAdd = { path, lists -> playlistsVM.addSongToPlaylists(path, lists) }
                )
            }

            composable("playlist/{id}") { entry ->
                val id = entry.arguments?.getString("id")?.toLongOrNull() ?: return@composable
                val playlist = playlists.find { it.playlistId == id } ?: return@composable
                val playlistSongs by playlistsVM.playlistSongs.collectAsState()

                LaunchedEffect(id) { playlistsVM.getSongsInPlaylist(id) }

                PlaylistScreen(
                    playlist = playlist,
                    songs = playlistSongs,
                    onSongUpdate = { song -> songsVM.editSong(song) },
                    onSongAdd = { path, lists -> playlistsVM.addSongToPlaylists(path, lists) },
                    onSongPlay = { song -> playerVM.play(song) },
                    onPlaylistDelete = {
                        playlistsVM.deletePlaylist(id)
                        nav.popBackStack()
                    },
                    onPlaylistUpdate = { p -> playlistsVM.renamePlaylist(p.playlistId, p.name) },
                    onPlaylistPlay = { songs -> playerVM.playPlaylist(songs) }
                )
            }
        }
    }
}


