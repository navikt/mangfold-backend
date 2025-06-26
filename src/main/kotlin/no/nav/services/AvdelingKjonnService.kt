package no.nav.services

import no.nav.modeller.AvdelingKjonnAntall
import no.nav.modeller.AvdelingAlderKjonnAntall
import no.nav.modeller.AvdelingAnsiennitetKjonnAntall
import no.nav.Konfig

fun hentKjonnPerAvdeling(prosjektId: String): List<AvdelingKjonnAntall> {
    val query = """
        SELECT organisasjon_avdeling, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY organisasjon_avdeling, kjonn
    """.trimIndent()
    val rows = runBigQuery(query, prosjektId)
    return rows.groupBy { it["organisasjon_avdeling"].stringValue }
        .map { (avdeling, groupRows) ->
            val kjonnMap = groupRows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            AvdelingKjonnAntall(
                avdeling = avdeling,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
}

fun hentAldersgruppeKjonnPerAvdeling(prosjektId: String): List<AvdelingAlderKjonnAntall> {
    val query = """
        SELECT organisasjon_avdeling, aldersgruppe, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY organisasjon_avdeling, aldersgruppe, kjonn
    """.trimIndent()
    val rows = runBigQuery(query, prosjektId)
    return rows.groupBy { 
            val avd = it["organisasjon_avdeling"]?.stringValue ?: "Ukjent"
            val alder = it["aldersgruppe"]?.stringValue ?: "Ukjent"
            avd to alder
        }
        .map { (pair, groupRows) ->
            val kjonnMap = groupRows.associate { (it["kjonn"]?.stringValue ?: "annet").lowercase() to it["antall"].longValue }
            AvdelingAlderKjonnAntall(
                avdeling = pair.first,
                aldersgruppe = pair.second,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0,
            )
        }
}

fun hentAnsiennnitetsgruppeKjonnPerAvdeling(prosjektId: String): List<AvdelingAnsiennitetKjonnAntall> {
    val query = """
        SELECT organisasjon_avdeling, ansiennitetsgruppe, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY organisasjon_avdeling, ansiennitetsgruppe, kjonn
    """.trimIndent()
    val rows = runBigQuery(query, prosjektId)
    return rows.groupBy { 
            val avd = it["organisasjon_avdeling"]?.stringValue ?: "Ukjent"
            val ans = it["ansiennitetsgruppe"]?.stringValue ?: "Ukjent"
            avd to ans
        }
        .map { (pair, groupRows) ->
            val kjonnMap = groupRows.associate { (it["kjonn"]?.stringValue ?: "annet").lowercase() to it["antall"].longValue }
            AvdelingAnsiennitetKjonnAntall(
                avdeling = pair.first,
                ansiennitetsgruppe = pair.second,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
}