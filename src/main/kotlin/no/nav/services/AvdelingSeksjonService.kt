package no.nav.services

import no.nav.Konfig
import no.nav.modeller.AvdelingSeksjoner
import no.nav.modeller.SeksjonKjonnData
import no.nav.modeller.AvdelingAldersgrupperSeksjoner
import no.nav.modeller.SeksjonAldersgrupper
import no.nav.modeller.KjonnAntallData

fun hentAvdelingerMedSeksjoner(prosjektId: String): List<AvdelingSeksjoner> {
    val query = """
        SELECT
            orgniv2_navn AS avdeling,
            org_seksjon AS seksjon,
            kjonn,
            SUM(antall) AS antall
        FROM `${Konfig.ANSATT_GRUPPERT_HR_AVDELING_ANTALL}`
        WHERE orgniv1_navn = 'Arbeids- og velferdsdirektoratet'
            AND orgniv2_navn != org_seksjon
        GROUP BY avdeling, seksjon, kjonn
        ORDER BY avdeling, seksjon
    """.trimIndent()
    
    val rows = runBigQuery(query, prosjektId)
    
    return rows.groupBy { it["avdeling"].stringValue }
        .map { (avdeling, avdelingRows) ->
            val seksjoner = avdelingRows
                .groupBy { it["seksjon"].stringValue }
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

fun hentAldersgrupperPerAvdelingSeksjoner(prosjektId: String): List<AvdelingAldersgrupperSeksjoner> {
    val query = """
        SELECT 
            orgniv2_navn AS avdeling,
            org_seksjon AS seksjon,
            aldersgruppe,
            kjonn,
            SUM(antall) AS antall
        FROM `${Konfig.ANSATT_GRUPPERT_HR_AVDELING_ANTALL}`
        WHERE orgniv1_navn = 'Arbeids- og velferdsdirektoratet'
            AND orgniv2_navn != org_seksjon
        GROUP BY 
            avdeling,
            seksjon,
            aldersgruppe,
            kjonn
        ORDER BY 
            avdeling,
            seksjon,
            aldersgruppe
    """.trimIndent()
    
    val rows = runBigQuery(query, prosjektId)
    
    return rows.groupBy { it["avdeling"].stringValue }
        .map { (avdeling, avdelingRows) ->
            val seksjoner = avdelingRows
                .groupBy { it["seksjon"].stringValue }
                .map { (seksjon, seksjonRows) ->
                    val aldersgruppeMap = seksjonRows
                        .groupBy { it["aldersgruppe"].stringValue }
                        .mapValues { (_, aldersgruppe) ->
                            val kjonnMap = aldersgruppe.associate { 
                                it["kjonn"].stringValue.lowercase() to it["antall"].longValue 
                            }
                            KjonnAntallData(
                                kvinne = kjonnMap["kvinne"] ?: 0,
                                mann = kjonnMap["mann"] ?: 0
                            )
                        }
                    
                    SeksjonAldersgrupper(
                        seksjon = seksjon,
                        aldersgrupper = aldersgruppeMap
                    )
                }
            
            AvdelingAldersgrupperSeksjoner(
                avdeling = avdeling,
                seksjoner = seksjoner
            )
        }
}