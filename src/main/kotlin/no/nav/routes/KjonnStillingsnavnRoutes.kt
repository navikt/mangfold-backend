package no.nav.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.slf4j.Logger
import no.nav.services.hentKjonnPerStillingsnavn

fun Route.kjonnStillingsnavnRoutes(projectId: String, logger: Logger) {
    get("/kjonn-per-stilling") {
        logger.info("Kaller hentKjonnPerStillingsnavn")
        try {
            val result = hentKjonnPerStillingsnavn(projectId)
            call.respond(result)
        } catch (e: Exception) {
            logger.error("Feil i kjonn-per-stilling", e)
            call.respond(
                HttpStatusCode.InternalServerError, 
                "Feil i kjonn-per-stilling: ${e.message}"
            )
        }
    }
}