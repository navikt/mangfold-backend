package no.nav.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.slf4j.Logger
import no.nav.services.hentAvdelingerMedSeksjoner

fun Route.avdelingSeksjonRoutes(projectId: String, logger: Logger) {
    get("/avdelinger-med-seksjoner") {
        logger.info("Kaller hentAvdelingerMedSeksjoner")
        try {
            val result = hentAvdelingerMedSeksjoner(projectId)
            call.respond(result)
        } catch (e: Exception) {
            logger.error("Feil i avdelinger-med-seksjoner", e)
            call.respond(HttpStatusCode.InternalServerError, "Feil i avdelinger-med-seksjoner: ${e.message}")
        }
    }
}