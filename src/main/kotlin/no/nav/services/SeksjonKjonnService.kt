package no.nav.services

import no.nav.modeller.SeksjonKjonnAntall
import no.nav.modeller.SeksjonAlderKjonnAntall
import no.nav.Konfig

fun hentKjonnPerSeksjon(prosjektId: String): List<SeksjonKjonnAntall> {
    val query = """
        SELECT organisasjon_seksjon, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY organisasjon_seksjon, kjonn
    """.trimIndent()
    val rows = runBigQuery(query, prosjektId)
    return rows.groupBy { it["organisasjon_seksjon"].stringValue }
        .map { (seksjon, groupRows) ->
            val kjonnMap = groupRows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            SeksjonKjonnAntall(
                seksjon = seksjon,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
}

fun hentAldersgruppeKjonnPerSeksjon(prosjektId: String): List<SeksjonAlderKjonnAntall> {
    val query = """
        SELECT organisasjon_seksjon, aldersgruppe, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY organisasjon_seksjon, aldersgruppe, kjonn
    """.trimIndent()
    val rows = runBigQuery(query, prosjektId)
    return rows.groupBy { 
            val seksjon = it["organisasjon_seksjon"]?.stringValue ?: "Ukjent"
            val alder = it["aldersgruppe"]?.stringValue ?: "Ukjent"
            seksjon to alder
        }
        .map { (pair, groupRows) ->
            val kjonnMap = groupRows.associate { (it["kjonn"]?.stringValue ?: "annet").lowercase() to it["antall"].longValue }
            SeksjonAlderKjonnAntall(
                seksjon = pair.first,
                aldersgruppe = pair.second,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
}