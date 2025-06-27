package no.nav

import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import no.nav.routes.KjonnRoutes

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    val config = environment.config
    val projectId = config.propertyOrNull("bigquery.projectId")?.getString()
        ?: System.getenv("BIGQUERY_PROJECT_ID")
        ?: throw IllegalStateException("BIGQUERY_PROJECT_ID må settes")

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
        })
    }

    KjonnRoutes(projectId)
}
