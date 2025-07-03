package no.nav.services

import no.nav.modeller.KjonnGruppeAntall
import no.nav.modeller.ToGrupperKjonnAntall
import no.nav.Konfig

fun hentKjonnPerAvdeling(prosjektId: String): List<KjonnGruppeAntall> {
    val query = """
        SELECT orgniv2_navn AS avdeling, kjonn, SUM(antall) AS antall
        FROM `${Konfig.ANSATT_GRUPPERT_HR_AVDELING_ANTALL}`        
        GROUP BY avdeling, kjonn
    """.trimIndent()
    val rows = runBigQuery(query, prosjektId)
    return rows.groupBy { it["avdeling"].stringValue }
        .map { (avdeling, groupRows) ->
            val kjonnMap = groupRows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            KjonnGruppeAntall(
                gruppe = avdeling,
                kjonnAntall = kjonnMap
            )
        }
}

fun hentAldersgruppeKjonnPerAvdeling(prosjektId: String): List<ToGrupperKjonnAntall> {
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
        ToGrupperKjonnAntall(
            gruppe1 = pair.first,
            gruppe2 = pair.second,
            kjonnAntall = kjonnMap
        )
        }
}

fun hentAnsiennnitetsgruppeKjonnPerAvdeling(prosjektId: String): List<ToGrupperKjonnAntall> {
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
            ToGrupperKjonnAntall(
                gruppe1 = pair.first,
                gruppe2 = pair.second,
                kjonnAntall = kjonnMap
            )
        }
}

fun hentLedernivaKjonnPerAvdeling(prosjektId: String): List<ToGrupperKjonnAntall> {
    val query = """
        SELECT organisasjon_avdeling, lederniva, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY organisasjon_avdeling, lederniva, kjonn
    """.trimIndent()
    val rows = runBigQuery(query, prosjektId)
    return rows.groupBy { 
            val avd = it["organisasjon_avdeling"]?.stringValue ?: "Ukjent"
            val lederniva = it["lederniva"]?.stringValue ?: "Ukjent"
            avd to lederniva
        }
        .map { (pair, groupRows) ->
            val kjonnMap = groupRows.associate { 
                (it["kjonn"]?.stringValue ?: "annet").lowercase() to it["antall"].longValue 
            }
            ToGrupperKjonnAntall(
                gruppe1 = pair.first,
                gruppe2 = pair.second,
                kjonnAntall = kjonnMap
            )
        }
}
