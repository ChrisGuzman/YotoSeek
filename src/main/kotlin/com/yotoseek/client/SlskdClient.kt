package com.yotoseek.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class SlskdSearchResult(
    val id: String,
    val filename: String,
    val size: Long,
    val bitRate: Int? = null,
    val length: Int? = null,
    val isLossless: Boolean = false
)

class SlskdClient(
    private val baseUrl: String,
    private val apiKey: String
) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun search(query: String): List<SlskdSearchResult> {
        // Generic implementation assuming a GET request to /api/v1/search
        // In reality, Slskd might be async, but this demonstrates the Ktor Client setup.
        return try {
             client.get("$baseUrl/api/v1/search") {
                 header("X-API-KEY", apiKey)
                 parameter("query", query)
             }.body<List<SlskdSearchResult>>()
        } catch (e: Exception) {
            // In a real app, use logging
            println("Error searching Slskd: ${e.message}")
            emptyList()
        }
    }

    fun selectBestMatch(results: List<SlskdSearchResult>): SlskdSearchResult? {
        // Basic selection logic: prioritize lossless or 320kbps
        return results.filter {
            it.isLossless || (it.bitRate ?: 0) >= 320
        }.maxByOrNull { it.bitRate ?: 0 } ?: results.firstOrNull()
    }
}
