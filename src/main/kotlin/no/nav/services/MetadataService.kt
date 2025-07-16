package no.nav.services

import no.nav.modeller.Metadata
import no.nav.Konfig
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun hentMetadataOppdateringsDato(prosjektId: String): List<Metadata> {
    val query = """
    SELECT Datalast_dato_timestamp as dato
    FROM `${Konfig.METADATA_TABELL}`
    ORDER BY dato desc
    LIMIT 1
    """.trimIndent()

    val formatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy", java.util.Locale.forLanguageTag("nb-NO"))

    val rows = runBigQuery(query, prosjektId)
    return rows.map { row ->
        Metadata(
            sisteOppdateringsDato = OffsetDateTime.parse(
                (row["dato"]).getTimestampInstant().toString(), // INSTANT timestamp type converted to ISO-8601 string
                DateTimeFormatter.ISO_DATE_TIME
            ).format(formatter) // format to "dd MMMM yyyy"
        )
    }
}