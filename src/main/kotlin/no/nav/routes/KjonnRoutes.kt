package no.nav.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Application.KjonnRoutes(projectId: String) {
    val logger = LoggerFactory.getLogger("KjonnRoutes")

    routing {
        kjonnStatistikkRoutes(projectId, logger)
        kjonnAvdelingRoutes(projectId, logger)
        kjonnSeksjonRoutes(projectId, logger)
        kjonnRolleRoutes(projectId, logger)
        kjonnLedernivaRoutes(projectId, logger)
        kjonnAldersgruppeRoutes(projectId, logger)
        kjonnAnsiennitetsgruppeRoutes(projectId, logger)
        aldersgruppePerRolle(projectId, logger)
        avdelingSeksjonRoutes(projectId, logger)
        kjonnStillingsnavnRoutes(projectId, logger)
    }
}
