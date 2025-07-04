package no.nav.services

import no.nav.Konfig
import no.nav.modeller.KjonnGruppeAntall

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