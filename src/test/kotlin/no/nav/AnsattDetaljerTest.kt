package no.nav.services

import no.nav.modeller.AnsattDetaljer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AnsattServiceTest {

    @Test
    fun `filtrerer ut alle personer der seksjonsnavn er lik avdelingsnavn`() {
        val testData = listOf(
            // Skal filtreres ut (uansett ledernivå)
            AnsattDetaljer(
                avdeling = "Testavdeling",
                seksjon = "Testavdeling",
                kjonn = "Kvinne",
                aldersgruppe = "30-55",
                ansiennitetsgruppe = "0-2",
                lederniva = "Avdelingsdirektør",
                stillingsnavn = "Avdelingsdirektør",
                antall = 1
            ),
            AnsattDetaljer(
                avdeling = "Testavdeling",
                seksjon = "Testavdeling",
                kjonn = "Mann",
                aldersgruppe = "30-55",
                ansiennitetsgruppe = "0-2",
                lederniva = "Kontorsjef",
                stillingsnavn = "Seniorrådgiver",
                antall = 1
            ),
            // Skal beholdes
            AnsattDetaljer(
                avdeling = "Testavdeling",
                seksjon = "Seksjon 1",
                kjonn = "Kvinne",
                aldersgruppe = "30-55",
                ansiennitetsgruppe = "4-6",
                lederniva = "Kontorsjef",
                stillingsnavn = "Seniorrådgiver",
                antall = 2
            ),
            AnsattDetaljer(
                avdeling = "Testavdeling",
                seksjon = "Seksjon 2",
                kjonn = "Mann",
                aldersgruppe = "30-55",
                ansiennitetsgruppe = "4-6",
                lederniva = "Kontorsjef",
                stillingsnavn = "Seniorrådgiver",
                antall = 2
            )
        )

        val filtrert = testData.filterNot { ansatt ->
            ansatt.seksjon == ansatt.avdeling
        }

        assertEquals(2, filtrert.size)
        assertTrue(filtrert.none { it.seksjon == it.avdeling })
        assertEquals("Seksjon 1", filtrert[0].seksjon)
        assertEquals("Seksjon 2", filtrert[1].seksjon)
    }
}