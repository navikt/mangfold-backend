package no.nav.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.slf4j.Logger
import no.nav.services.hentMetadataOppdateringsDato

fun Route.sistoppdatertRoutes(projectId: String, logger: Logger) {
    get("/sistoppdatert") {
        logger.info("Kaller hentMetadataOppdateringsDato")
        try {
            val resultat = hentMetadataOppdateringsDato(projectId)
            call.respond(resultat)
        } catch (e: Exception) {
            logger.error("Feil i metadata henting", e)
            call.respond(HttpStatusCode.InternalServerError, "Feil i metadata henting: ${e.message}")
        }
    }
}