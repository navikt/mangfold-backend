package no.nav.services

import no.nav.Konfig
import no.nav.modeller.AnsattDetaljer

fun hentAnsattDetaljer(prosjektId: String): List<AnsattDetaljer> {
    val query = """
        SELECT *
        FROM `${Konfig.ANSATT_GRUPPERT_HR_ANSATT_STILLING_PER_SEKSJON}`
        ORDER BY 
            orgniv2_navn ASC,
            org_seksjon ASC,
            CASE 
                WHEN aldersgruppe = '<30' THEN 1
                WHEN aldersgruppe = '30-50' THEN 2
                WHEN aldersgruppe = '50+' THEN 3
                ELSE 4
            END ASC,
            CASE 
                WHEN ansiennitetsgruppe = '0-2' THEN 1
                WHEN ansiennitetsgruppe = '2-4' THEN 2
                WHEN ansiennitetsgruppe = '4-6' THEN 3
                WHEN ansiennitetsgruppe = '6-8' THEN 4
                WHEN ansiennitetsgruppe = '8-10' THEN 5
                WHEN ansiennitetsgruppe = '10-16' THEN 6
                WHEN ansiennitetsgruppe = '16+' THEN 7
                ELSE 8
            END ASC,
            CAST(lederniva AS INT64) ASC
    """.trimIndent()
    
    val rows = runBigQuery(query, prosjektId)
    return rows.map { row ->
        AnsattDetaljer(
            avdeling = row["orgniv2_navn"].stringValue,
            seksjon = row["org_seksjon"].stringValue,
            kjonn = row["kjonn"].stringValue,
            aldersgruppe = row["aldersgruppe"].stringValue,
            ansiennitetsgruppe = row["ansiennitetsgruppe"].stringValue,
            lederniva = row["lederniva"].stringValue,
            stillingsnavn = row["stillingsnavn"].stringValue,
            antall = row["antall"].longValue
        )
    }
}