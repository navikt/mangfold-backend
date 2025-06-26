package no.nav.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.slf4j.Logger
import no.nav.services.hentAldersgruppePerRolle

fun Route.aldersgruppePerRolle(projectId: String, logger: Logger) {
    get("/aldersgruppe-per-rolle") {
        logger.info("Kaller hentAldersgruppePerRolle")
        try {
            val result = hentAldersgruppePerRolle(projectId)
            call.respond(result)
        } catch (e: Exception) {
            logger.error("Feil i aldersgruppe-per-rolle", e)
            call.respond(HttpStatusCode.InternalServerError, "Feil i aldersgruppe-per-rolle: ${e.message}")
        }
    }
}