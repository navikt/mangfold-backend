// package no.nav.services

// import no.nav.modeller.KjonnGruppeAntall
// import no.nav.Konfig

// fun hentKjonnPerRolle(prosjektId: String): List<KjonnGruppeAntall> {
//     val query = """
//         SELECT rolle, kjonn, COUNT(*) AS antall
//         FROM `${Konfig.ANSATTE_TABELL}`
//         GROUP BY rolle, kjonn
//     """.trimIndent()
//     val rows = runBigQuery(query, prosjektId)
//     return rows.groupBy { it["rolle"].stringValue }
//         .map { (rolle, groupRows) ->
//             val kjonnMap = groupRows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
//             KjonnGruppeAntall(
//                 gruppe = rolle,
//                 kjonnAntall = kjonnMap
//             )
//         }
// }