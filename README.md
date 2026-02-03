# YotoSeek

**YotoSeek** is a multi-tenant Ktor web application that bridges the gap between Spotify playlists/albums and Yoto libraries using a shared **slskd** instance for media retrieval. It leverages HTMX for a high-performance, reactive UI without the complexity of a heavy JavaScript build chain.

## Features (Planned/In-Progress)

*   **Spotify & Yoto Integration**: Syncs music from Spotify to Yoto cards.
*   **Multi-User Support**: Individual sessions with Spotify OAuth and Yoto Device Flow authentication.
*   **Reactive UI**: Built with Ktor HTML DSL and HTMX for real-time status updates.
*   **Smart Downloading**: Searches Slskd for high-quality audio (lossless or 320kbps).
*   **Background Processing**: Handles queuing, searching, downloading, and uploading asynchronously.

## Tech Stack

*   **Language**: Kotlin 2.x
*   **Server**: Ktor 3.x (Netty engine)
*   **Database**: SQLite with Exposed ORM
*   **Frontend**: Ktor HTML DSL + HTMX + Tailwind CSS (CDN)
*   **Build Tool**: Gradle (Kotlin DSL)

## Development Setup

### Prerequisites

1.  **Java JDK 21+**: Ensure you have a compatible Java Development Kit installed.
    *   Verify with `java -version`.
2.  **Slskd Instance**: You need access to a running instance of [slskd](https://github.com/slskd/slskd).
    *   You will need the `URL` and `API Key` for this instance.

### Running Locally

1.  **Clone the Repository**:
    ```bash
    git clone <repository-url>
    cd yotoseek
    ```

2.  **Configuration**:
    *   *Note: Configuration via environment variables or a config file is currently being implemented. For now, the application starts a basic server on port 8080.*

3.  **Run the Server**:
    Use the Gradle wrapper to start the application in development mode:
    ```bash
    ./gradlew run
    ```
    The server will start at `http://0.0.0.0:8080`.

4.  **Run Tests**:
    ```bash
    ./gradlew test
    ```

### Project Structure

*   `src/main/kotlin/com/yotoseek/db`: Database schema definitions (Users, Tasks).
*   `src/main/kotlin/com/yotoseek/client`: API clients (SlskdClient).
*   `src/main/kotlin/com/yotoseek/Application.kt`: Server entry point and module configuration.
