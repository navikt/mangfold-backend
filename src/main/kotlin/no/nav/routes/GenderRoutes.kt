package no.nav.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.slf4j.LoggerFactory
import no.nav.services.hentTotalKjonnStatistikk
import no.nav.services.hentKjonnPerAvdeling
import no.nav.services.hentAldersgruppeKjonnPerAvdeling
import no.nav.models.GenderStatsResponse
import no.nav.models.GenderCount
import no.nav.models.DepartmentGenderCount

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
    }   
}
