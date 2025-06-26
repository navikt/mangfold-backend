package no.nav.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.slf4j.Logger
import no.nav.services.hentKjonnPerAldersgruppe

fun Route.kjonnAldersgruppeRoutes(projectId: String, logger: Logger) {
    get("/kjonn-per-aldersgruppe") {
        logger.info("Kaller hentKjonnPerAldersgruppe")
        try {
            val result = hentKjonnPerAldersgruppe(projectId)
            call.respond(result)
        } catch (e: Exception) {
            logger.error("Feil i kjonn-per-aldersgruppe", e)
            call.respond(HttpStatusCode.InternalServerError, "Feil i kjonn-per-aldersgruppe: ${e.message}")
        }
    }
}