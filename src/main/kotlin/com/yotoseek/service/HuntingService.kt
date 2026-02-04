package com.yotoseek.service

import com.yotoseek.client.SlskdClient
import com.yotoseek.client.SlskdSearchResult
import kotlinx.coroutines.delay

class HuntingService(
    private val spotifyService: SpotifyService,
    private val slskdClient: SlskdClient
) {
    suspend fun hunt(playlistUri: String) {
        println("Starting hunt for playlist: $playlistUri")

        val tracks = try {
            spotifyService.getPlaylistTracks(playlistUri)
        } catch (e: Exception) {
            println("Failed to fetch tracks from Spotify: ${e.message}")
            return
        }

        println("Found ${tracks.size} tracks. Starting search...")

        tracks.forEach { track ->
            val query = "${track.artist} ${track.title}"
            println("Searching for: $query")

            val results = slskdClient.search(query)

            if (results.isNotEmpty()) {
                val bestMatch = slskdClient.selectBestMatch(results)
                if (bestMatch != null) {
                    println("Found match for '$query': ${bestMatch.filename} (${bestMatch.bitRate}kbps)")
                    // In a real scenario, we would trigger a download here.
                    // e.g., slskdClient.download(bestMatch.id)
                } else {
                    println("No suitable match found for '$query'")
                }
            } else {
                println("No results found for '$query'")
            }

            // Respect rate limits or avoid flooding Slskd
            delay(1000)
        }

        println("Hunt complete for playlist: $playlistUri")
    }
}
