// package no.nav.services

// import no.nav.modeller.KjonnGruppeAntall
// import no.nav.modeller.ToGrupperKjonnAntall
// import no.nav.Konfig

// fun hentKjonnPerSeksjon(prosjektId: String): List<KjonnGruppeAntall> {
//     val query = """
//         SELECT organisasjon_seksjon, kjonn, COUNT(*) AS antall
//         FROM `${Konfig.ANSATTE_TABELL}`
//         GROUP BY organisasjon_seksjon, kjonn
//     """.trimIndent()
//     val rows = runBigQuery(query, prosjektId)
//     return rows.groupBy { it["organisasjon_seksjon"].stringValue }
//         .map { (seksjon, groupRows) ->
//             val kjonnMap = groupRows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
//             KjonnGruppeAntall(
//                 gruppe = seksjon,
//                 kjonnAntall = kjonnMap
//             )
//         }
// }

// fun hentAldersgruppeKjonnPerSeksjon(prosjektId: String): List<ToGrupperKjonnAntall> {
//     val query = """
//         SELECT organisasjon_seksjon, aldersgruppe, kjonn, COUNT(*) AS antall
//         FROM `${Konfig.ANSATTE_TABELL}`
//         GROUP BY organisasjon_seksjon, aldersgruppe, kjonn
//     """.trimIndent()
//     val rows = runBigQuery(query, prosjektId)
//     return rows.groupBy { 
//             val seksjon = it["organisasjon_seksjon"]?.stringValue ?: "Ukjent"
//             val alder = it["aldersgruppe"]?.stringValue ?: "Ukjent"
//             seksjon to alder
//         }
//         .map { (pair, groupRows) ->
//             val kjonnMap = groupRows.associate { (it["kjonn"]?.stringValue ?: "annet").lowercase() to it["antall"].longValue }
//             ToGrupperKjonnAntall(
//                 gruppe1 = pair.first,
//                 gruppe2 = pair.second,
//                 kjonnAntall = kjonnMap
//             )
//         }
// }

// fun hentAnsiennitetsgruppeKjonnPerSeksjon(prosjektId: String): List<ToGrupperKjonnAntall> {
//     val query = """
//         SELECT organisasjon_seksjon, ansiennitetsgruppe, kjonn, COUNT(*) AS antall
//         FROM `${Konfig.ANSATTE_TABELL}`
//         GROUP BY organisasjon_seksjon, ansiennitetsgruppe, kjonn
//     """.trimIndent()
    
//     val rows = runBigQuery(query, prosjektId)
//     return rows.groupBy { 
//             val seksjon = it["organisasjon_seksjon"]?.stringValue ?: "Ukjent"
//             val ansiennitet = it["ansiennitetsgruppe"]?.stringValue ?: "Ukjent"
//             seksjon to ansiennitet
//         }
//         .map { (pair, groupRows) ->
//             val kjonnMap = groupRows.associate { 
//                 (it["kjonn"]?.stringValue ?: "annet").lowercase() to it["antall"].longValue 
//             }
//             ToGrupperKjonnAntall(
//                 gruppe1 = pair.first,
//                 gruppe2 = pair.second,
//                 kjonnAntall = kjonnMap
//             )
//         }
// }

// fun hentLedernivaKjonnPerSeksjon(prosjektId: String): List<ToGrupperKjonnAntall> {
//     val query = """
//         SELECT organisasjon_seksjon, lederniva, kjonn, COUNT(*) AS antall
//         FROM `${Konfig.ANSATTE_TABELL}`
//         GROUP BY organisasjon_seksjon, lederniva, kjonn
//     """.trimIndent()
    
//     val rows = runBigQuery(query, prosjektId)
//     return rows.groupBy { 
//             val seksjon = it["organisasjon_seksjon"]?.stringValue ?: "Ukjent"
//             val lederniva = it["lederniva"]?.stringValue ?: "Ukjent"
//             seksjon to lederniva
//         }
//         .map { (pair, groupRows) ->
//             val kjonnMap = groupRows.associate { 
//                 (it["kjonn"]?.stringValue ?: "annet").lowercase() to it["antall"].longValue 
//             }
//             ToGrupperKjonnAntall(
//                 gruppe1 = pair.first,
//                 gruppe2 = pair.second,
//                 kjonnAntall = kjonnMap
//             )
//         }
// }