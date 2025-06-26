package no.nav.services

import no.nav.modeller.KjonnAntall
import no.nav.Konfig

fun hentTotalKjonnStatistikk(prosjektId: String): List<KjonnAntall> {
    val query = """
        SELECT kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY kjonn
    """.trimIndent()
    return runBigQuery(query, prosjektId).map { rad ->
        KjonnAntall(
            kjonn = rad["kjonn"].stringValue.lowercase(),
            antall = rad["antall"].longValue
        )
    }
}