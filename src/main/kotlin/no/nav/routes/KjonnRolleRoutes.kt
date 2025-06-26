package no.nav.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.slf4j.Logger
import no.nav.services.hentKjonnPerRolle

fun Route.kjonnRolleRoutes(projectId: String, logger: Logger) {
    get("/kjonn-per-rolle") {
        logger.info("Kaller hentKjonnPerRolle")
        try {
            val result = hentKjonnPerRolle(projectId)
            call.respond(result)
        } catch (e: Exception) {
            logger.error("Feil i kjonn-per-rolle", e)
            call.respond(HttpStatusCode.InternalServerError, "Feil i kjonn-per-rolle: ${e.message}")
        }
    }
}