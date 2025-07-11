// package no.nav.routes

// import io.ktor.server.application.*
// import io.ktor.server.response.*
// import io.ktor.server.routing.*
// import io.ktor.http.*
// import org.slf4j.Logger
// import no.nav.services.hentKjonnPerAnsiennitetsgruppe

// fun Route.kjonnAnsiennitetsgruppeRoutes(projectId: String, logger: Logger) {
//     get("/kjonn-per-ansiennitetsgruppe") {
//         logger.info("Kaller hentKjonnPerAnsiennitetsgruppe")
//         try {
//             val result = hentKjonnPerAnsiennitetsgruppe(projectId)
//             call.respond(result)
//         } catch (e: Exception) {
//             logger.error("Feil i kjonn-per-ansiennitetsgruppe", e)
//             call.respond(HttpStatusCode.InternalServerError, "Feil i kjonn-per-ansiennitetsgruppe: ${e.message}")
//         }
//     }
// }