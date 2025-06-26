package no.nav.services

import no.nav.modeller.KjonnGruppeAntall
import no.nav.Konfig

fun hentKjonnPerLederniva(prosjektId: String): List<KjonnGruppeAntall> {
    val query = """
        SELECT lederniva, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY lederniva, kjonn
    """.trimIndent()
    val rows = runBigQuery(query, prosjektId)
    return rows.groupBy { it["lederniva"].stringValue }
        .map { (lederniva, groupRows) ->
            val kjonnMap = groupRows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            KjonnGruppeAntall(
                gruppe = lederniva,
                kjonnAntall = kjonnMap
            )
        }
}