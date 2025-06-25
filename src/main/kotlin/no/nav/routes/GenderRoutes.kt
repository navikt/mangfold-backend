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
import no.nav.models.GenderStatsResponse
import no.nav.models.GenderCount
import no.nav.models.DepartmentGenderCount
import no.nav.models.SeksjonKjonnCount

fun Application.genderRoutes() {
    val logger = LoggerFactory.getLogger("GenderRoutes")
    val projectId = System.getenv("BIGQUERY_PROJECT_ID")
        ?: throw IllegalStateException("BIGQUERY_PROJECT_ID må settes som miljøvariabel")

    routing {
        get("/gender-stats") {
            logger.info("Kaller hentTotalKjonnStatistikk")
            try {
                val resultMap = hentTotalKjonnStatistikk(projectId)
                val response = GenderStatsResponse(
                    totalKjonnsfordeling = resultMap.map { (kjonn, antall) ->
                        GenderCount(kjonn = kjonn, antall = antall)
                    }
                )
                call.respond(response)
            } catch (e: Exception) {
                logger.error("Feil i gender-stats", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i gender-stats: ${e.message}"
                )
            }
        }

        get("/gender-per-department") {
            logger.info("Kaller hentKjonnPerAvdeling")
            try {
                val result = hentKjonnPerAvdeling(projectId)
                call.respond(result)
            }  catch (e: Exception) {
                logger.error("Feil i gender-per-department", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i gender-per-department: ${e.message}"
                )
            }
        }

        get("/gender-age-per-department") {
            logger.info("Kaller hentAldersgruppeKjonnPerAvdeling")
            try {
                val result = hentAldersgruppeKjonnPerAvdeling(projectId)
                call.respond(result)
            } catch (e: Exception) {
                logger.error("Feil i gender-age-per-department", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i gender-age-per-department: ${e.message}"
                )
            }
        }

        get("/gender-seniority-per-department") {
            logger.info("Kaller hentAnsiennnitetsgruppeKjonnPerAvdeling")
            try {
                val result = hentAnsiennnitetsgruppeKjonnPerAvdeling(projectId)
                call.respond(result)
            } catch (e: Exception) {
                logger.error("Feil i gender-seniority-per-department", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i gender-seniority-per-department: ${e.message}")
            } 
        }

        get("/gender-per-section") {
            logger.info("Kaller hentKjonnPerSeksjon")
            try {
                val result = hentKjonnPerSeksjon(projectId)
                call.respond(result)
            } catch (e: Exception) {
                logger.error("Feil i gender-per-section", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i  gender-per-section: ${e.message}")
            }
        }

        get("/gender-age-per-section") {
            logger.info("Kaller hentAldersgruppeKjonnPerSeksjon")
            try {
                val result = hentAldersgruppeKjonnPerSeksjon(projectId)
                call.respond(result)
            } catch (e: Exception) {
                logger.error("Feil i gender-age-per-section", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Feil i fender-age-per-section: ${e.message}"
                )
            }
        }
    }
}
