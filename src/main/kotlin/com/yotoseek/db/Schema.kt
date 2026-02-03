package com.yotoseek.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

enum class TaskStatus {
    QUEUED,
    SEARCHING,
    DOWNLOADING,
    UPLOADING,
    COMPLETE,
    FAILED
}

object Users : IntIdTable() {
    val spotifyRefreshToken = varchar("spotify_refresh_token", 512).nullable()
    val yotoAuthToken = varchar("yoto_auth_token", 512).nullable()
}

object Tasks : IntIdTable() {
    val user = reference("user_id", Users)
    val status = enumerationByName("status", 50, TaskStatus::class).default(TaskStatus.QUEUED)
    val metadata = text("metadata").nullable() // JSON string for album info etc
    val spotifyUrl = varchar("spotify_url", 512)
    val progress = integer("progress").default(0) // 0-100
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}
