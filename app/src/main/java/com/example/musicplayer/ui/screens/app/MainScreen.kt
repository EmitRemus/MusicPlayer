package com.example.musicplayer.ui.screens.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicplayer.ui.screens.dev.DevScreen
import com.example.musicplayer.ui.viewmodel.DevViewModel
import com.example.musicplayer.ui.viewmodel.PlayerViewModel
import com.example.musicplayer.ui.viewmodel.PlaylistsViewModel
import com.example.musicplayer.ui.viewmodel.SongsViewModel

@Composable
fun MusicAppNavHost() {
    val nav = rememberNavController()

    val songsVM: SongsViewModel = viewModel ()
    val playlistsVM: PlaylistsViewModel = viewModel()
    val playerVM: PlayerViewModel = viewModel()
    val devVM: DevViewModel = viewModel ()
    val log by devVM.log.collectAsState()

    val songs by songsVM.songs.collectAsState()
    val playlists by playlistsVM.playlists.collectAsState()

    NavHost(nav, startDestination = "home") {
        composable ("dev") {
            DevScreen(
                // SONG DB
                onScanClicked = { devVM.scanMusic() },
                onShowSongsClicked = { devVM.showAllSongs() },

                // PLAYLISTS
                onCreatePlaylistClicked = { devVM.createPlaylist("New Playlist") },
                onShowPlaylistsClicked = { devVM.showPlaylists() },
                onDeletePlaylistClicked = { id -> devVM.deletePlaylist(id) },

                // ADD SONG TO PLAYLIST
//                onAddSongToPlaylistClicked = { songId, playlistId ->
//                    vm.addSongToPlaylist(songId, playlistId)
//                },

                // PLAYBACK
                onTestPlayClicked = {
                    devVM.playFirstSong()
                },
//                onPlaySongClicked = { vm.playSong(it) },
                onPauseClicked = { devVM.pausePlayback() },
                onResumeClicked = { devVM.resumePlayback() },
                onNextClicked = { devVM.nextTrack() },
                onPreviousClicked = { devVM.previousTrack() },
                onShowCurrentClicked = { devVM.showCurrentSong() },

                // LOG OUTPUT
                logOutput = log
            )
        }

        composable("home") {
            HomeScreen(
                playlists = playlists,
                recentlyAdded = songs.takeLast(10),
                onOpenPlaylist = { id -> nav.navigate("playlist/$id") },
                onCreatePlaylist = { name -> playlistsVM.createPlaylist(name) },
                onOpenPlayer = { song ->
                    playerVM.play(song.path)
                    nav.navigate("player/${song.path}")
                },
                onOpenDev = { nav.navigate("dev") },
                onSongUpdate = { song -> songsVM.editSong(song) }
            )
        }

        composable("songs") {
            SongsScreen(
                songs = songs,
                onOpenPlayer = { song ->
                    playerVM.play(song.path)
                    nav.navigate("player/${song.path}")
                }
            )
        }

        composable("playlist/{id}") { entry ->
            val id = entry.arguments?.getString("id")!!.toLong()

            LaunchedEffect (id) {
                // load playlist songs
            }

            // TODO load songs from playlistsVM.getSongsInPlaylist(id)
        }

        composable("player/{path}") { entry ->
            val path = entry.arguments?.getString("path")
            val song = songs.find { it.path == path }

            PlayerScreen(
                song = song,
                onPause = { playerVM.pause() },
                onResume = { playerVM.resume() }
            )
        }
    }
}

