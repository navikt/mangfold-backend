package no.nav.services

import no.nav.Konfig
import no.nav.modeller.KjonnGruppeAntall
import no.nav.modeller.ToGrupperKjonnAntall

fun hentKjonnPerStillingsnavn(prosjektId: String): List<KjonnGruppeAntall> {
    val query = """
        SELECT 
            stillingsnavn,
            kjonn,
            SUM(antall) AS antall
        FROM `${Konfig.ANSATT_GRUPPERT_HR_STILLING_ANTALL}`
        GROUP BY stillingsnavn, kjonn
        ORDER BY stillingsnavn
    """.trimIndent()
    
    val rows = runBigQuery(query, prosjektId)
    
    return rows.groupBy { it["stillingsnavn"].stringValue }
        .map { (stilling, stillingRows) ->
            val kjonnMap = stillingRows.associate { 
                it["kjonn"].stringValue.lowercase() to it["antall"].longValue 
            }
            KjonnGruppeAntall(
                gruppe = stilling,
                kjonnAntall = kjonnMap
            )
        }
}

fun hentAldersgruppePerStillingsnavn(prosjektId: String): List<ToGrupperKjonnAntall> {
    val query = """
        SELECT 
            stillingsnavn,
            aldersgruppe,
            kjonn,
            SUM(antall) AS antall
        FROM `${Konfig.ANSATT_GRUPPERT_HR_STILLING_ANTALL}`
        GROUP BY stillingsnavn, aldersgruppe, kjonn
        ORDER BY stillingsnavn, aldersgruppe
    """.trimIndent()
    
    val rows = runBigQuery(query, prosjektId)
    
    return rows.groupBy { 
        val stilling = it["stillingsnavn"].stringValue
        val alder = it["aldersgruppe"].stringValue
        stilling to alder 
    }.map { (pair, groupRows) ->
        val kjonnMap = groupRows.associate { 
            it["kjonn"].stringValue.lowercase() to it["antall"].longValue 
        }
        ToGrupperKjonnAntall(
            gruppe1 = pair.first,
            gruppe2 = pair.second,
            kjonnAntall = kjonnMap
        )
    }
}