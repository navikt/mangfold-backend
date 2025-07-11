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
        ORDER BY avdeling ASC, seksjon ASC
    """.trimIndent()
    
    val rows = runBigQuery(query, prosjektId)
    
    return rows.groupBy { it["avdeling"].stringValue }
        .map { (avdeling, avdelingRows) ->
            val seksjoner = avdelingRows
                .groupBy { it["seksjon"].stringValue }
                .map { (seksjon, seksjonRows) ->

                    val total = seksjonRows.sumOf { it["antall"].longValue }
                    val (kjonnMap, erMaskert) = if (total < 5) {
                        mapOf("kvinne" to 0L, "mann" to 0L) to true
                    } else {
                     seksjonRows.associate { 
                        it["kjonn"].stringValue.lowercase() to it["antall"].longValue 
                        } to false
                    }
                    SeksjonKjonnData(
                        gruppe = seksjon,
                        kjonnAntall = kjonnMap,
                        erMaskert = erMaskert
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
            SUM(antall) AS antall,
            CASE WHEN aldersgruppe = '<30' THEN 1
                 WHEN aldersgruppe = '30-50' THEN 2
                 WHEN aldersgruppe = '50+' THEN 3
                 ELSE 0
            END AS aldersgruppe_sort
        FROM `${Konfig.ANSATT_GRUPPERT_HR_AVDELING_ANTALL}`
        WHERE orgniv1_navn = 'Arbeids- og velferdsdirektoratet'
            AND orgniv2_navn != org_seksjon
        GROUP BY 
            avdeling,
            seksjon,
            aldersgruppe,
            kjonn
        ORDER BY 
            avdeling ASC,
            seksjon ASC,
            aldersgruppe_sort
    """.trimIndent()
    
    val rows = runBigQuery(query, prosjektId)
    
    val alleAldersgrupper = listOf("<30", "30-50", "50+")
    
    return rows.groupBy { it["avdeling"].stringValue }
        .map { (avdeling, avdelingRows) ->
            val seksjoner = avdelingRows
                .groupBy { it["seksjon"].stringValue }
                .map { (seksjon, seksjonRows) ->
                    val total = seksjonRows.sumOf { it["antall"].longValue }
                    val erMaskert = total < 5

                    val aldersgruppeMap = if (erMaskert) {
                        alleAldersgrupper.associateWith { 
                            KjonnAntallData(kvinne = 0, mann = 0)
                        }
                    } else {
                        val gruppertData = seksjonRows
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
                        
                        alleAldersgrupper.associateWith { aldersgruppe ->
                            gruppertData[aldersgruppe] ?: KjonnAntallData(kvinne = 0, mann = 0)
                        }
                    }
                    
                    SeksjonAldersgrupper(
                        seksjon = seksjon,
                        aldersgrupper = aldersgruppeMap,
                        erMaskert = erMaskert
                    )
                }
            
            AvdelingAldersgrupperSeksjoner(
                avdeling = avdeling,
                seksjoner = seksjoner
            )
        }
}