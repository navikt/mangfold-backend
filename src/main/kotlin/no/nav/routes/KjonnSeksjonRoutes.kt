// package no.nav.routes

// import io.ktor.server.application.*
// import io.ktor.server.response.*
// import io.ktor.server.routing.*
// import io.ktor.http.*
// import org.slf4j.Logger
// import no.nav.services.hentKjonnPerSeksjon
// import no.nav.services.hentAldersgruppeKjonnPerSeksjon
// import no.nav.services.hentAnsiennitetsgruppeKjonnPerSeksjon
// import no.nav.services.hentLedernivaKjonnPerSeksjon

// fun Route.kjonnSeksjonRoutes(projectId: String, logger: Logger) {
//     get("/kjonn-per-seksjon") {
//         logger.info("Kaller hentKjonnPerSeksjon")
//         try {
//             val result = hentKjonnPerSeksjon(projectId)
//             call.respond(result)
//         } catch (e: Exception) {
//             logger.error("Feil i kjonn-per-seksjon", e)
//             call.respond(HttpStatusCode.InternalServerError, "Feil i kjonn-per-seksjon: ${e.message}")
//         }
//     }
//     get("/alder-kjonn-per-seksjon") {
//         logger.info("Kaller hentAldersgruppeKjonnPerSeksjon")
//         try {
//             val result = hentAldersgruppeKjonnPerSeksjon(projectId)
//             call.respond(result)
//         } catch (e: Exception) {
//             logger.error("Feil i alder-kjonn-per-seksjon", e)
//             call.respond(HttpStatusCode.InternalServerError, "Feil i alder-kjonn-per-seksjon: ${e.message}")
//         }
//     }
//     get("/ansiennitet-kjonn-per-seksjon") {
//         logger.info("Kaller hentAnsiennitetsgruppeKjonnPerSeksjon")
//         try {
//             val result = hentAnsiennitetsgruppeKjonnPerSeksjon(projectId)
//             call.respond(result)
//         } catch (e: Exception) {
//             logger.error("Feil i ansiennitet-kjonn-per-seksjon", e)
//             call.respond(
//                 HttpStatusCode.InternalServerError,
//                 "Feil i ansiennitet-kjonn-per-seksjon: ${e.message}"
//             )
//         }
//     }
//     get("/lederniva-kjonn-per-seksjon") {
//         logger.info("Kaller hentLedernivaKjonnPerSeksjon")
//         try {
//             val result = hentLedernivaKjonnPerSeksjon(projectId)
//             call.respond(result)
//         } catch (e: Exception) {
//             logger.error("Feil i lederniva-kjonn-per-seksjon", e)
//             call.respond(
//                 HttpStatusCode.InternalServerError,
//                 "Feil i lederniva-kjonn-per-seksjon: ${e.message}"
//             )
//         }
//     }
// }