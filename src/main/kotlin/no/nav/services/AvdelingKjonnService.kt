package no.nav.services

import no.nav.modeller.KjonnGruppeAntall
import no.nav.modeller.ToGrupperKjonnAntall
import no.nav.Konfig

fun hentKjonnPerAvdeling(prosjektId: String): List<KjonnGruppeAntall> {
    val query = """
    WITH antall_avdeling AS (
        SELECT orgniv2_navn AS avdeling, SUM(antall) AS antall_avd
        FROM `${Konfig.ANSATT_GRUPPERT_HR_AVDELING_ANTALL}`        
        GROUP BY avdeling
    )

    SELECT orgniv2_navn AS avdeling, kjonn, SUM(antall) AS antall, sum(antall)/antall_avd andel_kjonn_seksjon, antall_avd
    FROM `${Konfig.ANSATT_GRUPPERT_HR_AVDELING_ANTALL}`        
    LEFT JOIN antall_avdeling on avdeling = orgniv2_navn
    GROUP BY avdeling, kjonn, antall_avd
    ORDER BY kjonn asc, andel_kjonn_seksjon desc

    """.trimIndent()
    val rows = runBigQuery(query, prosjektId)
    return rows.groupBy { it["avdeling"].stringValue }
        .map { (avdeling, groupRows) ->
            val kjonnMap = groupRows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            KjonnGruppeAntall(
                gruppe = avdeling,
                kjonnAntall = kjonnMap,
                erMaskert = false //Antar at det ikke er maskert her, da det er aggregert på avdeling
            )
        }
}

// fun hentAldersgruppeKjonnPerAvdeling(prosjektId: String): List<ToGrupperKjonnAntall> {
//     val query = """
//         SELECT organisasjon_avdeling, aldersgruppe, kjonn, COUNT(*) AS antall
//         FROM `${Konfig.ANSATTE_TABELL}`
//         GROUP BY organisasjon_avdeling, aldersgruppe, kjonn
//     """.trimIndent()
//     val rows = runBigQuery(query, prosjektId)
//     return rows.groupBy { 
//             val avd = it["organisasjon_avdeling"]?.stringValue ?: "Ukjent"
//             val alder = it["aldersgruppe"]?.stringValue ?: "Ukjent"
//             avd to alder
//         }
//         .map { (pair, groupRows) ->
//             val kjonnMap = groupRows.associate { (it["kjonn"]?.stringValue ?: "annet").lowercase() to it["antall"].longValue }
//         ToGrupperKjonnAntall(
//             gruppe1 = pair.first,
//             gruppe2 = pair.second,
//             kjonnAntall = kjonnMap
//         )
//         }
// }

fun hentAnsiennnitetsgruppeKjonnPerAvdeling(prosjektId: String): List<ToGrupperKjonnAntall> {
    val query = """
        SELECT 
            orgniv2_navn AS avdeling,
            ansiennitetsgruppe,
            kjonn,
            SUM(antall) AS antall
        FROM `${Konfig.ANSATT_GRUPPERT_HR_AVDELING_ANTALL}`
        WHERE orgniv1_navn = 'Arbeids- og velferdsdirektoratet'
        GROUP BY 
            avdeling,
            ansiennitetsgruppe,
            kjonn
        ORDER BY 
            avdeling,
            CASE 
                WHEN ansiennitetsgruppe = '0-2' THEN 1
                WHEN ansiennitetsgruppe = '2-4' THEN 2
                WHEN ansiennitetsgruppe = '4-6' THEN 3
                WHEN ansiennitetsgruppe = '6-8' THEN 4
                WHEN ansiennitetsgruppe = '8-10' THEN 5
                WHEN ansiennitetsgruppe = '10-16' THEN 6
                WHEN ansiennitetsgruppe = '16+' THEN 7
                ELSE 8
            END
    """.trimIndent()
    
    val rows = runBigQuery(query, prosjektId)
    return rows.groupBy { 
        val avd = it["avdeling"].stringValue
        val ans = it["ansiennitetsgruppe"].stringValue
        avd to ans
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

fun hentLedernivaKjonnPerAvdeling(prosjektId: String): List<ToGrupperKjonnAntall> {
    val query = """
        SELECT 
            orgniv2_navn AS avdeling,
            lederniva,
            kjonn,
            SUM(antall) AS antall
        FROM `${Konfig.ANSATT_GRUPPERT_HR_AVDELING_ANTALL}`
        WHERE orgniv1_navn = 'Arbeids- og velferdsdirektoratet'
        GROUP BY 
            avdeling,
            lederniva,
            kjonn
        ORDER BY 
            avdeling,
            lederniva DESC
    """.trimIndent()
    
    val rows = runBigQuery(query, prosjektId)
    return rows.groupBy { 
        val avd = it["avdeling"].stringValue
        val lederniva = it["lederniva"].stringValue
        avd to lederniva
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
