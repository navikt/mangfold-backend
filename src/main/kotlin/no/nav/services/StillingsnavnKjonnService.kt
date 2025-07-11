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
            val total = stillingRows.sumOf { it["antall"].longValue }
            val (kjonnMap, erMaskert) = if (total < 5) {
                mapOf("kvinne" to 0L, "mann" to 0L) to true
            } else {
                stillingRows.associate { 
                    it["kjonn"].stringValue.lowercase() to it["antall"].longValue 
                } to false
            }
            KjonnGruppeAntall(
                gruppe = stilling,
                kjonnAntall = kjonnMap,
                erMaskert = erMaskert
            )
        }
}

fun hentAldersgruppePerStillingsnavn(prosjektId: String): List<ToGrupperKjonnAntall> {
    val query = """
        SELECT 
            stillingsnavn,
            aldersgruppe,
            kjonn,
            SUM(antall) AS antall,
        CASE
            WHEN aldersgruppe LIKE '<%' THEN 0
            WHEN REGEXP_EXTRACT(aldersgruppe, r'^(\d+)') IS NOT NULL
                THEN CAST(REGEXP_EXTRACT(aldersgruppe, r'^(\d+)') AS INT64)
            ELSE 999
        END AS aldersgruppe_sort
        FROM `${Konfig.ANSATT_GRUPPERT_HR_STILLING_ANTALL}`
        GROUP BY 
            stillingsnavn, 
            aldersgruppe, 
            kjonn
        ORDER BY 
            stillingsnavn,
            aldersgruppe_sort
    """.trimIndent()
    
    val rows = runBigQuery(query, prosjektId)
    
    return rows.groupBy { 
        val stilling = it["stillingsnavn"].stringValue
        val alder = it["aldersgruppe"].stringValue
        stilling to alder 
    }.map { (pair, groupRows) ->

        val total = groupRows.sumOf { it["antall"].longValue }
        val (kjonnMap, erMaskert) = if (total < 5) {
            mapOf("kvinne" to 0L, "mann" to 0L) to true
        } else {
            groupRows.associate { 
                it["kjonn"].stringValue.lowercase() to it["antall"].longValue 
            } to false
        }
        ToGrupperKjonnAntall(
            gruppe1 = pair.first,
            gruppe2 = pair.second,
            kjonnAntall = kjonnMap,
            erMaskert = erMaskert
        )
    }
}