package no.nav.services

import no.nav.modeller.AnsiennitetsgruppeKjonnAntall
import no.nav.Konfig

fun hentKjonnPerAnsiennitetsgruppe(prosjektId: String): List<AnsiennitetsgruppeKjonnAntall> {
    val query = """
        SELECT ansiennitetsgruppe, kjonn, COUNT(*) AS antall
        FROM `${Konfig.ANSATTE_TABELL}`
        GROUP BY ansiennitetsgruppe, kjonn
    """.trimIndent()
    val rows = runBigQuery(query, prosjektId)
    return rows.groupBy { it["ansiennitetsgruppe"].stringValue }
        .map { (ansiennitetsgruppe, groupRows) ->
            val kjonnMap = groupRows.associate { it["kjonn"].stringValue.lowercase() to it["antall"].longValue }
            AnsiennitetsgruppeKjonnAntall(
                ansiennitetsgruppe = ansiennitetsgruppe,
                kvinne = kjonnMap["kvinne"] ?: 0,
                mann = kjonnMap["mann"] ?: 0
            )
        }
}