package no.nav.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Application.KjonnRoutes() {
    val logger = LoggerFactory.getLogger("GenderRoutes")
    val projectId = System.getenv("BIGQUERY_PROJECT_ID")
        ?: throw IllegalStateException("BIGQUERY_PROJECT_ID må settes som miljøvariabel")

    routing {
        kjonnStatistikkRoutes(projectId, logger)
        kjonnAvdelingRoutes(projectId, logger)
        kjonnSeksjonRoutes(projectId, logger)
        kjonnRolleRoutes(projectId, logger)
        kjonnLedernivaRoutes(projectId, logger)
        kjonnAldersgruppeRoutes(projectId, logger)
        kjonnAnsiennitetsgruppeRoutes(projectId, logger)
    }
}
