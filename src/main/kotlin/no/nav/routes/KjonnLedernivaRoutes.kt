package no.nav.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.slf4j.Logger
import no.nav.services.hentKjonnPerLederniva

fun Route.kjonnLedernivaRoutes(projectId: String, logger: Logger) {
    get("/kjonn-per-lederniva") {
        logger.info("Kaller hentKjonnPerLederniva")
        try {
            val result = hentKjonnPerLederniva(projectId)
            call.respond(result)
        } catch (e: Exception) {
            logger.error("Feil i kjonn-per-lederniva", e)
            call.respond(HttpStatusCode.InternalServerError, "Feil i kjonn-per-lederniva: ${e.message}")
        }
    }
}