# YotoSeek

Objective: "Build a multi-tenant Ktor web application called 'YotoSeek' that allows users to sync Spotify playlists/albums to their Yoto libraries via a shared slskd instance. The UI must be high-performance and use HTMX to avoid a JavaScript build process."

1. Architecture & Tech Stack:

    Language/Framework: Kotlin 2.x with Ktor 3.4+ (Server & Client).

    Frontend: Ktor HTML DSL + HTMX (for live-updating fragments) + Tailwind CSS (via CDN).

    Database: SQLite with Exposed ORM to manage Users (tokens, IDs) and Tasks (status, metadata).

    Concurrency: Use Kotlin Coroutines with a singleton WorkerManager to handle background downloads.

2. Multi-User Authentication:

    Spotify OAuth: Implement Ktor Authentication for Spotify login. Store refresh_tokens to allow background sync.

    Yoto Device Flow: Implement a /settings page that initiates the Yoto Device Authorization Flow, displaying the 6-digit code for users to pair their account.

    Session Management: Use Ktor Sessions (Cookie-based) to isolate user data.

3. The Reactive Dashboard (HTMX):

    Input: A single text input for Spotify URLs (Album or Playlist).

    Live Status Board: A table that uses hx-get with a 10-second trigger="every 10s" to poll /api/tasks and swap in an HTML fragment.

    States: Track each item as: QUEUED, SEARCHING, DOWNLOADING (X%), UPLOADING, COMPLETE.

4. Resilient Media Pipeline:

    slskd Integration: * Search slskd for the best match (prioritize islossless or 320kbps).

        Use a Resiliency Policy: If a transfer fails or a peer goes offline, implement an exponential backoff retry and a "Search Fallback" to try a different peer.

    Yoto Uploader: Use the yoto.dev API to request a uploadUrl, PUT the media, and link it to the user's library.

5. Initial Task: "Please generate the build.gradle.kts with ktor-server-html-builder and ktor-server-htmx dependencies, the SQLite schema for Users/Tasks, and the SlskdClient that includes the search-selection logic."
