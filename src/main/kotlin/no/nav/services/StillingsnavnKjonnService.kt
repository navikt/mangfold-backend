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
                WHEN aldersgruppe = 'Ukjent alder' THEN 999
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

    // Finn alle unike aldersgrupper i sortert rekkefølge (slik SQL gir)
    val alleAldersgrupper = rows
        .map { it["aldersgruppe"].stringValue }
        .distinct()
        .sortedWith(compareBy {
            val str = it
            when {
                str.startsWith("<") -> 0
                Regex("""^\d+""").find(str) != null -> Regex("""^\d+""").find(str)!!.value.toInt()
                str == "Ukjent alder" -> 999
                else -> 999
            }
        })

    // Gruppér rows per stillingsnavn
    val stillingTilRows = rows.groupBy { it["stillingsnavn"].stringValue }

    // For hver stilling, lag ToGrupperKjonnAntall for hver aldersgruppe
    return stillingTilRows.flatMap { (stilling, gruppRows) ->
        val total = gruppRows.sumOf { it["antall"].longValue }
        val erMaskert = total < 5

        // For hver aldersgruppe i rekkefølge, lag ToGrupperKjonnAntall
        alleAldersgrupper.map { alder ->
            val rowsForAlder = gruppRows.filter { it["aldersgruppe"].stringValue == alder }
            val kjonnMap = if (erMaskert) {
                mapOf("kvinne" to 0L, "mann" to 0L)
            } else {
                rowsForAlder.associate { 
                    it["kjonn"].stringValue.lowercase() to it["antall"].longValue 
                }
            }
            ToGrupperKjonnAntall(
                gruppe1 = stilling,
                gruppe2 = alder,
                kjonnAntall = kjonnMap,
                erMaskert = erMaskert
            )
        }
    }
}