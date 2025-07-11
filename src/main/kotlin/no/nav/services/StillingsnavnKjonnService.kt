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

    // 1. Gruppér alle rader per stillingsnavn
    val stillingTilRader = rows.groupBy { it["stillingsnavn"].stringValue }

    // 2. Finn for hver stilling total antall personer på tvers av alle aldersgrupper og kjønn
    val stillingMaskering: Map<String, Boolean> = stillingTilRader.mapValues { (_, rader) ->
        rader.sumOf { it["antall"].longValue } < 5
    }

    // 3. Lag output: For hver (stilling, aldersgruppe), sett erMaskert = true hvis stillingen skal maskeres
    return rows.groupBy { 
        val stilling = it["stillingsnavn"].stringValue
        val alder = it["aldersgruppe"].stringValue
        stilling to alder 
    }.map { (pair, groupRows) ->
        val stilling = pair.first
        val erMaskert = stillingMaskering[stilling] ?: false
        val kjonnMap = if (erMaskert) {
            mapOf("kvinne" to 0L, "mann" to 0L)
        } else {
            groupRows.associate { 
                it["kjonn"].stringValue.lowercase() to it["antall"].longValue 
            }
        }
        ToGrupperKjonnAntall(
            gruppe1 = stilling,
            gruppe2 = pair.second,
            kjonnAntall = kjonnMap,
            erMaskert = erMaskert
        )
    }
}