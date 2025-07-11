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
            SUM(antall) AS antall
        FROM `${Konfig.ANSATT_GRUPPERT_HR_STILLING_ANTALL}`
        GROUP BY 
            stillingsnavn, 
            aldersgruppe, 
            kjonn
        ORDER BY 
            stillingsnavn,
            CASE 
                WHEN aldersgruppe = '<30' THEN 1
                WHEN aldersgruppe = '30-50' THEN 2
                WHEN aldersgruppe = '50+' THEN 3
                ELSE 0
            END
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