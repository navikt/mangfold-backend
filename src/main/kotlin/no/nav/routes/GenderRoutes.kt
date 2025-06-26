package no.nav.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.slf4j.LoggerFactory
import no.nav.services.hentTotalKjonnStatistikk
import no.nav.services.hentKjonnPerAvdeling
import no.nav.services.hentAldersgruppeKjonnPerAvdeling
import no.nav.services.hentAnsiennnitetsgruppeKjonnPerAvdeling
import no.nav.services.hentAldersgruppeKjonnPerSeksjon
import no.nav.services.hentKjonnPerSeksjon
import no.nav.services.hentKjonnPerRolle
import no.nav.services.hentKjonnPerLederniva
import no.nav.services.hentKjonnPerAldersgruppe
import no.nav.services.hentKjonnPerAnsiennitetsgruppe


fun Application.genderRoutes() {
    val logger = LoggerFactory.getLogger("GenderRoutes")
    val projectId = System.getenv("BIGQUERY_PROJECT_ID")
        ?: throw IllegalStateException("BIGQUERY_PROJECT_ID må settes som miljøvariabel")

    routing {
 get("/kjonn-statistikk") {
        logger.info("Kaller hentTotalKjonnStatistikk")
        try {
            val resultat = hentTotalKjonnStatistikk(projectId)
            call.respond(resultat)
        } catch (e: Exception) {
            logger.error("Feil i kjonn-statistikk", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                "Feil i kjonn-statistikk: ${e.message}"
            )
        }
        }

        get("/kjonn-per-avdeling") {
            logger.info("Kaller hentKjonnPerAvdeling")
            try {
                val result = hentKjonnPerAvdeling(projectId)
                call.respond(result)
            }  catch (e: Exception) {
                logger.error("Feil i kjonn-per-avdeling", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i kjonn-per-avdeling: ${e.message}"
                )
            }
        }

        get("/alder-kjonn-per-avdeling") {
            logger.info("Kaller hentAldersgruppeKjonnPerAvdeling")
            try {
                val result = hentAldersgruppeKjonnPerAvdeling(projectId)
                call.respond(result)
            } catch (e: Exception) {
                logger.error("Feil i alder-kjonn-per-avdeling", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i alder-kjonn-per-avdeling: ${e.message}"
                )
            }
        }

        get("/ansiennitet-kjonn-per-avdeling") {
            logger.info("Kaller hentAnsiennnitetsgruppeKjonnPerAvdeling")
            try {
                val result = hentAnsiennnitetsgruppeKjonnPerAvdeling(projectId)
                call.respond(result)
            } catch (e: Exception) {
                logger.error("Feil i ansiennitet-kjonn-per-avdeling", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i ansiennitet-kjonn-per-avdeling: ${e.message}")
            } 
        }

        get("/kjonn-per-seksjon") {
            logger.info("Kaller hentKjonnPerSeksjon")
            try {
                val result = hentKjonnPerSeksjon(projectId)
                call.respond(result)
            } catch (e: Exception) {
                logger.error("Feil i kjonn-per-seksjon", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i  kjonn-per-seksjon: ${e.message}")
            }
        }

        get("/alder-kjonn-per-seksjon") {
            logger.info("Kaller hentAldersgruppeKjonnPerSeksjon")
            try {
                val result = hentAldersgruppeKjonnPerSeksjon(projectId)
                call.respond(result)
            } catch (e: Exception) {
                logger.error("Feil i alder-kjonn-per-seksjon", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i alder-kjonn-per-seksjon: ${e.message}"
                )
            }
        }

        get("/kjonn-per-rolle") {
            logger.info("Kaller hentKjonnPerRolle")
            try {
                val result = hentKjonnPerRolle(projectId)
                call.respond(result)
            } catch (e: Exception) {
                logger.error("Feil i kjonn-per-rolle", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i kjonn-per-rolle: ${e.message}")
            }
        }

        get("/kjonn-per-lederniva") {
            logger.info("Kaller hentKjonnPerLederniva")
            try {
                val result = hentKjonnPerLederniva(projectId)
                call.respond(result)
            } catch (e: Exception) {
                logger.error("Feil i kjonn-per-lederniva", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i kjonn-per-lederniva: ${e.message}")
            }
        }

        get("/kjonn-per-aldersgruppe") {
            logger.info("Kaller hentKjonnPerAldersgruppe")
            try {
                val result = hentKjonnPerAldersgruppe(projectId)
                call.respond(result)
            } catch (e: Exception) {
                logger.error("Feil i kjonn-per-aldersgruppe", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i kjonn-per-aldersgruppe: ${e.message}"
                )
            }
        }

        get("/kjonn-per-ansiennitetsgruppe") {
            logger.info("Kaller hentKjonnPerAnsiennitetsgruppe")
            try {
                val result = hentKjonnPerAnsiennitetsgruppe(projectId)
                call.respond(result)
            } catch (e: Exception) {
                logger.error("Feil i kjonn-per-ansiennitetsgruppe", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i kjonn-per-ansiennitetsgruppe: ${e.message}"
                )
            }
        }
    }
}
