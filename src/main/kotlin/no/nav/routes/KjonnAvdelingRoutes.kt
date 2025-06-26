package no.nav.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.slf4j.Logger
import no.nav.services.hentKjonnPerAvdeling
import no.nav.services.hentAldersgruppeKjonnPerAvdeling
import no.nav.services.hentAnsiennnitetsgruppeKjonnPerAvdeling

fun Route.kjonnAvdelingRoutes(projectId: String, logger: Logger) {
    get("/kjonn-per-avdeling") {
        logger.info("Kaller hentKjonnPerAvdeling")
        try {
            val result = hentKjonnPerAvdeling(projectId)
            call.respond(result)
        } catch (e: Exception) {
            logger.error("Feil i kjonn-per-avdeling", e)
            call.respond(HttpStatusCode.InternalServerError, "Feil i kjonn-per-avdeling: ${e.message}")
        }
    }
    get("/alder-kjonn-per-avdeling") {
        logger.info("Kaller hentAldersgruppeKjonnPerAvdeling")
        try {
            val result = hentAldersgruppeKjonnPerAvdeling(projectId)
            call.respond(result)
        } catch (e: Exception) {
            logger.error("Feil i alder-kjonn-per-avdeling", e)
            call.respond(HttpStatusCode.InternalServerError, "Feil i alder-kjonn-per-avdeling: ${e.message}")
        }
    }
    get("/ansiennitet-kjonn-per-avdeling") {
        logger.info("Kaller hentAnsiennnitetsgruppeKjonnPerAvdeling")
        try {
            val result = hentAnsiennnitetsgruppeKjonnPerAvdeling(projectId)
            call.respond(result)
        } catch (e: Exception) {
            logger.error("Feil i ansiennitet-kjonn-per-avdeling", e)
            call.respond(HttpStatusCode.InternalServerError, "Feil i ansiennitet-kjonn-per-avdeling: ${e.message}")
        }
    }
}