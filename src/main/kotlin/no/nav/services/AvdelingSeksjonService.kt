package no.nav.services

import no.nav.Konfig
import no.nav.modeller.AvdelingSeksjoner
import no.nav.modeller.SeksjonKjonnData

fun hentAvdelingerMedSeksjoner(prosjektId: String): List<AvdelingSeksjoner> {
    val query = """
        SELECT 
            organisasjon_avdeling,
            organisasjon_seksjon,
            kjonn,
            COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY organisasjon_avdeling, organisasjon_seksjon, kjonn
        ORDER BY organisasjon_avdeling, organisasjon_seksjon
    """.trimIndent()
    
    val rows = runBigQuery(query, prosjektId)
    
    return rows.groupBy { it["organisasjon_avdeling"].stringValue }
        .map { (avdeling, avdelingRows) ->
            val seksjoner = avdelingRows
                .groupBy { it["organisasjon_seksjon"].stringValue }
                .map { (seksjon, seksjonRows) ->
                    val kjonnMap = seksjonRows.associate { 
                        it["kjonn"].stringValue.lowercase() to it["antall"].longValue 
                    }
                    SeksjonKjonnData(
                        gruppe = seksjon,
                        kjonnAntall = kjonnMap
                    )
                }
            
            AvdelingSeksjoner(
                avdeling = avdeling,
                seksjoner = seksjoner
            )
        }
}