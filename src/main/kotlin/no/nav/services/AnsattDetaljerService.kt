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
            lederniva ASC
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
    // // Filtrer ut alle der seksjonsnavn er lik avdelingsnavn
    // .filterNot { ansatt ->
    //     ansatt.seksjon == ansatt.avdeling
    // }
    .sortedWith(
        compareBy<AnsattDetaljer> { it.avdeling }
            .thenBy { it.seksjon }
            .thenBy { sorterAldersgruppe(it.aldersgruppe) }
            .thenBy { sorterAnsiennitetsgruppe(it.ansiennitetsgruppe) }
            .thenBy { it.lederniva }
    )
}

// Sorteringslogikk for aldersgrupper
private fun sorterAldersgruppe(aldersgruppe: String): Int {
    return when {
        aldersgruppe.startsWith("<") -> 0
        Regex("""^\d+""").find(aldersgruppe) != null ->
            Regex("""^\d+""").find(aldersgruppe)!!.value.toInt()
        else -> 999
    }
}

// Sorteringslogikk for ansiennitetsgrupper
private fun sorterAnsiennitetsgruppe(ansiennitetsgruppe: String): Int {
    return Regex("""^\d+""").find(ansiennitetsgruppe)?.value?.toInt() ?: 999
}