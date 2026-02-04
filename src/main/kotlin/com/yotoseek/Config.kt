package com.yotoseek

object Config {
    val spotifyClientId: String = System.getenv("SPOTIFY_CLIENT_ID") ?: ""
    val spotifyClientSecret: String = System.getenv("SPOTIFY_CLIENT_SECRET") ?: ""
    val spotifyUser: String = System.getenv("SPOTIFY_USER") ?: ""
    val spotifyPass: String = System.getenv("SPOTIFY_PASS") ?: ""
    val slskdUrl: String = System.getenv("SLSKD_URL") ?: "http://localhost:5030"
    val slskdApiKey: String = System.getenv("SLSKD_API_KEY") ?: ""
    val databasePath: String = System.getenv("DATABASE_PATH") ?: "/data/yotoseek.db"

    // Check if critical configuration is missing (optional, but good for fail-fast)
    fun validate() {
        if (spotifyClientId.isBlank() && System.getenv("IO_KTOR_DEVELOPMENT") != "true") {
            throw IllegalStateException("SPOTIFY_CLIENT_ID is missing")
        }
    }
}
