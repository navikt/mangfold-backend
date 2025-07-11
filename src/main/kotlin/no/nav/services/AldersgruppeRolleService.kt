// package no.nav.services

// import no.nav.modeller.ToGrupperKjonnAntall
// import no.nav.Konfig

// fun hentAldersgruppePerRolle(prosjektId: String): List<ToGrupperKjonnAntall> {
//     val query = """
//         SELECT aldersgruppe, rolle, kjonn, COUNT(*) AS antall
//         FROM `${Konfig.ANSATTE_TABELL}`
//         GROUP BY aldersgruppe, rolle, kjonn
//     """.trimIndent()
//     val rows = runBigQuery(query, prosjektId)
//     return rows.groupBy { Pair(it["aldersgruppe"].stringValue, it["rolle"].stringValue) }
//         .map { (key, groupRows) ->
//             val (aldersgruppe, rolle) = key
//             val kjonnMap = groupRows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
//             ToGrupperKjonnAntall(
//                 gruppe1 = aldersgruppe,
//                 gruppe2 = rolle,
//                 kjonnAntall = kjonnMap
//             )
//         }
// }