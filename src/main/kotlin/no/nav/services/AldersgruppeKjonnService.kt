// package no.nav.services

// import no.nav.modeller.KjonnGruppeAntall
// import no.nav.Konfig

// fun hentKjonnPerAldersgruppe(prosjektId: String): List<KjonnGruppeAntall> {
//     val query = """
//         SELECT aldersgruppe, kjonn, COUNT(*) AS antall
//         FROM `${Konfig.ANSATTE_TABELL}`
//         GROUP BY aldersgruppe, kjonn
//     """.trimIndent()
//     val rows = runBigQuery(query, prosjektId)
//     return rows.groupBy { it["aldersgruppe"].stringValue }
//         .map { (aldersgruppe, groupRows) ->
//             val kjonnMap = groupRows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
//             KjonnGruppeAntall(
//                 gruppe = aldersgruppe,
//                 kjonnAntall = kjonnMap
//             )
//         }
// }