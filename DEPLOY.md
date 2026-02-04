# Deployment Guide

This repository is configured for "Zero-Touch" deployment to Fly.io.

## Prerequisites

1.  A Fly.io account.
2.  `flyctl` installed locally.
3.  A GitHub repository with these files.

## Initial Setup

1.  **Launch the App**:
    Run `fly launch` to initialize the app (if not already done). Select the options but **do not deploy yet**.
    Ensure `fly.toml` is respected.

2.  **Create Persistent Volume**:
    The database requires a persistent volume named `yotoseek_data`.
    ```bash
    fly volumes create yotoseek_data --size 1
    ```

3.  **Set Secrets**:
    Configure the environment variables for the application.
    ```bash
    fly secrets set SPOTIFY_CLIENT_ID="your_id" \
                    SPOTIFY_CLIENT_SECRET="your_secret" \
                    SLSKD_URL="http://your-slskd-instance" \
                    SLSKD_API_KEY="your_key"
    ```

4.  **Configure GitHub Actions**:
    *   Get a Fly API Token: `fly tokens create deploy -x 999999h`
    *   Go to GitHub Repo -> Settings -> Secrets and variables -> Actions.
    *   Add a new repository secret named `FLY_API_TOKEN` with the value from the previous step.

## Zero-Touch Deployment

Once setup is complete, every push to the `main` branch will automatically:
1.  Build the Docker image.
2.  Deploy it to Fly.io.
3.  Run a health check.

## Management Tasks

### Rotating API Keys

To rotate Spotify or Slskd keys:

1.  **Update Fly Secrets**:
    ```bash
    fly secrets set SPOTIFY_CLIENT_SECRET="new_secret"
    ```
    This will trigger a redeploy with the new values.

2.  **Update GitHub Secrets** (if needed for build/test in future):
    Update the secrets in the GitHub UI.

### Adding New Friends (Users)

Currently, users are managed directly in the database.

1.  **Connect to the Console**:
    ```bash
    fly ssh console
    ```

2.  **Access the Database**:
    ```bash
    sqlite3 /data/yotoseek.db
    ```

3.  **Insert User**:
    ```sql
    INSERT INTO Users (spotify_refresh_token, yoto_auth_token) VALUES ('token1', 'token2');
    ```
    *Note: You will need to obtain these tokens via OAuth flows manually for now.*

4.  **Exit**:
    Type `.exit` to leave sqlite3, and `exit` to leave the SSH session.
