package no.nav.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.slf4j.Logger
import no.nav.services.hentTotalKjonnStatistikk

fun Route.kjonnStatistikkRoutes(projectId: String, logger: Logger) {
    get("/kjonn-statistikk") {
        logger.info("Kaller hentTotalKjonnStatistikk")
        try {
            val resultat = hentTotalKjonnStatistikk(projectId)
            call.respond(resultat)
        } catch (e: Exception) {
            logger.error("Feil i kjonn-statistikk", e)
            call.respond(HttpStatusCode.InternalServerError, "Feil i kjonn-statistikk: ${e.message}")
        }
    }
}