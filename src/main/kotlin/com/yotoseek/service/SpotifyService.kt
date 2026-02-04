package com.yotoseek.service

import com.yotoseek.Config
import com.yotoseek.model.TrackInfo
import xyz.gianlu.librespot.core.Session
import xyz.gianlu.librespot.metadata.PlaylistId
import xyz.gianlu.librespot.metadata.TrackId
import java.util.concurrent.TimeUnit

class SpotifyService {
    private var session: Session? = null

    init {
        connect()
    }

    private fun connect() {
        if (Config.spotifyUser.isNotBlank() && Config.spotifyPass.isNotBlank()) {
            try {
                session = Session.Builder()
                    .userPass(Config.spotifyUser, Config.spotifyPass)
                    .create()
                println("Connected to Spotify as ${Config.spotifyUser}")
            } catch (e: Exception) {
                println("Failed to connect to Spotify: ${e.message}")
            }
        } else {
            println("Spotify credentials missing. Skipping connection.")
        }
    }

    fun getPlaylistTracks(uri: String): List<TrackInfo> {
        val currentSession = session ?: throw IllegalStateException("Spotify session not initialized")

        try {
            val playlistId = PlaylistId.fromUri(uri)
            val playlist = currentSession.playlist().getPlaylist(playlistId)

            // Wait for the playlist to load if necessary, though getPlaylist usually blocks or returns the object.
            // Depending on librespot version, we might need to handle mapped tracks.

            val tracks = mutableListOf<TrackInfo>()

            playlist.contents.items.forEach { item ->
                val trackUri = item.uri
                if (trackUri.startsWith("spotify:track:")) {
                    try {
                        val trackId = TrackId.fromUri(trackUri)
                        // Fetch track metadata. This might be slow for large playlists as it does a request per track.
                        // Ideally we'd batch this or use what's in the playlist object if available.
                        // Librespot's Playlist object usually contains minimal info.

                        val track = currentSession.api().getMetadata(trackId)
                        val artistName = track.artistList.joinToString(", ") { it.name }
                        tracks.add(TrackInfo(artistName, track.name))
                    } catch (e: Exception) {
                        println("Skipping track $trackUri: ${e.message}")
                    }
                }
            }
            return tracks

        } catch (e: Exception) {
            println("Error fetching playlist: ${e.message}")
            return emptyList()
        }
    }
}
