package no.nav.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.slf4j.Logger
import no.nav.services.hentKjonnPerSeksjon
import no.nav.services.hentAldersgruppeKjonnPerSeksjon

fun Route.kjonnSeksjonRoutes(projectId: String, logger: Logger) {
    get("/kjonn-per-seksjon") {
        logger.info("Kaller hentKjonnPerSeksjon")
        try {
            val result = hentKjonnPerSeksjon(projectId)
            call.respond(result)
        } catch (e: Exception) {
            logger.error("Feil i kjonn-per-seksjon", e)
            call.respond(HttpStatusCode.InternalServerError, "Feil i kjonn-per-seksjon: ${e.message}")
        }
    }
    get("/alder-kjonn-per-seksjon") {
        logger.info("Kaller hentAldersgruppeKjonnPerSeksjon")
        try {
            val result = hentAldersgruppeKjonnPerSeksjon(projectId)
            call.respond(result)
        } catch (e: Exception) {
            logger.error("Feil i alder-kjonn-per-seksjon", e)
            call.respond(HttpStatusCode.InternalServerError, "Feil i alder-kjonn-per-seksjon: ${e.message}")
        }
    }
}