package com.yotoseek

import com.yotoseek.db.Tasks
import com.yotoseek.db.Users
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // 1. Initialize Config
    Config.validate()

    // 2. Database Setup
    val dbFile = File(Config.databasePath)
    // Ensure directory exists
    if (dbFile.parentFile != null && !dbFile.parentFile.exists()) {
        dbFile.parentFile.mkdirs()
    }

    Database.connect("jdbc:sqlite:${Config.databasePath}", "org.sqlite.JDBC")

    transaction {
        SchemaUtils.create(Users, Tasks)
    }

    // 3. Ktor Plugins
    install(CallLogging)
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: ${cause.localizedMessage}", status = HttpStatusCode.InternalServerError)
        }
    }

    // 4. Routing
    routing {
        get("/health") {
            call.respond(HttpStatusCode.OK, mapOf("status" to "healthy"))
        }

        get("/") {
            call.respondText("YotoSeek is running!")
        }
    }
}
