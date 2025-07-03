package no.nav.services

import no.nav.modeller.KjonnAntall
import no.nav.Konfig

fun hentTotalKjonnStatistikk(prosjektId: String): List<KjonnAntall> {
    val query = """
        SELECT kjonn, SUM(antall) AS antall
        FROM `${Konfig.ANSATT_GRUPPERT_HR_AVDELING_ANTALL}`
        GROUP BY kjonn
    """.trimIndent()
    return runBigQuery(query, prosjektId).map { rad ->
        KjonnAntall(
            kjonn = rad["kjonn"].stringValue.lowercase(),
            antall = rad["antall"].longValue
        )
    }
}