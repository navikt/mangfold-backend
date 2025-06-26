package no.nav.services

import no.nav.modeller.LedernivaKjonnAntall
import no.nav.Konfig

fun hentKjonnPerLederniva(prosjektId: String): List<LedernivaKjonnAntall> {
    val query = """
        SELECT lederniva, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY lederniva, kjonn
    """.trimIndent()
    val rows = runBigQuery(query, prosjektId)
    return rows.groupBy { it["lederniva"].stringValue }
        .map { (nivaa, groupRows) ->
            val kjonnMap = groupRows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            LedernivaKjonnAntall(
                lederNiva = nivaa,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
}