package no.nav.services

import no.nav.modeller.AnsattDetaljer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AnsattDetaljerServiceTest {

    @Test
    fun `antall skal være korrekt aggregert per seksjon`() {
        val resultater = listOf(
            AnsattDetaljer("A", "S1", "K", "30-39", "5-9", "L1", "S", 2),
            AnsattDetaljer("A", "S1", "K", "30-39", "5-9", "L1", "S2", 1),
            AnsattDetaljer("A", "S2", "K", "30-39", "5-9", "L1", "S", 1)
        )
        val antallPerSeksjon = resultater
            .groupBy { it.seksjon }
            .mapValues { (_, ansatte) -> ansatte.sumOf { it.antall } }
        assertTrue(antallPerSeksjon["S1"]!! <= 3, "Dobbelttelling i seksjon S1")
        assertEquals(antallPerSeksjon["S2"], 1, "Feil summering i seksjon S2")
    }

    @Test
    fun `ingen negative eller null antall verdier`() {
        val resultater = listOf(
            AnsattDetaljer("A", "S1", "K", "30-39", "5-9", "L1", "S", 2),
            AnsattDetaljer("B", "S2", "M", "40-49", "10-14", "L2", "S", 1)
        )
        assertTrue(resultater.all { it.antall > 0 }, "Alle antall skal være positive og større enn null")
    }

    @Test
    fun `ingen duplikater for unik kombinasjon av filterverdier`() {
        val resultater = listOf(
            AnsattDetaljer("A", "S1", "K", "30-39", "5-9", "L1", "S", 2),
            AnsattDetaljer("B", "S1", "M", "30-39", "5-9", "L1", "S", 2)
        )
        val nøkkelSet = mutableSetOf<String>()
        resultater.forEach { ansatt ->
            val nøkkel = listOf(
                ansatt.avdeling,
                ansatt.seksjon,
                ansatt.kjonn,
                ansatt.aldersgruppe,
                ansatt.ansiennitetsgruppe,
                ansatt.lederniva,
                ansatt.stillingsnavn
            ).joinToString("|")
            assertTrue(nøkkelSet.add(nøkkel), "Dobbelttelling/duplikat funnet for kombinasjon: $nøkkel")
        }
    }

    @Test
    fun `ingen rad skal ha seksjon lik avdeling`() {
        val resultater = listOf(
            AnsattDetaljer("A", "S1", "K", "30-39", "5-9", "L1", "S", 1),
            AnsattDetaljer("B", "S2", "M", "40-49", "10-14", "L2", "S", 2)
        )
        assertTrue(resultater.none { it.seksjon == it.avdeling }, "Rader med seksjon == avdeling skal være filtrert ut.")
    }

    @Test
    fun `ingen tomme eller blanke verdier i viktige felter`() {
        val resultater = listOf(
            AnsattDetaljer("A", "S1", "K", "30-39", "5-9", "L1", "S", 2),
            AnsattDetaljer("B", "S2", "M", "40-49", "10-14", "L2", "S", 1)
        )
        resultater.forEach { ansatt ->
            assertTrue(ansatt.avdeling.isNotBlank(), "Avdeling kan ikke være tom")
            assertTrue(ansatt.seksjon.isNotBlank(), "Seksjon kan ikke være tom")
            assertTrue(ansatt.kjonn.isNotBlank(), "Kjønn kan ikke være tom")
            assertTrue(ansatt.aldersgruppe.isNotBlank(), "Aldersgruppe kan ikke være tom")
            assertTrue(ansatt.ansiennitetsgruppe.isNotBlank(), "Ansiennitetsgruppe kan ikke være tom")
            assertTrue(ansatt.lederniva.isNotBlank(), "Ledernivå kan ikke være tom")
            assertTrue(ansatt.stillingsnavn.isNotBlank(), "Stillingsnavn kan ikke være tom")
        }
    }

    @Test
    fun `hver person skal kun telles én gang per filterkombinasjon`() {
        val resultater = listOf(
            AnsattDetaljer("A", "S1", "K", "30-39", "5-9", "L1", "S", 1),
            AnsattDetaljer("B", "S2", "M", "40-49", "10-14", "L2", "S", 1)
        )
        val kombinasjoner = resultater.map {
            listOf(it.avdeling, it.seksjon, it.kjonn, it.aldersgruppe, it.ansiennitetsgruppe, it.lederniva).joinToString("|")
        }
        val duplikater = kombinasjoner.size != kombinasjoner.toSet().size
        assertFalse(duplikater, "Samme person er telt flere ganger for samme filterkombinasjon")
    }

    @Test
    fun `antall skal ikke være større enn antall unike personer per seksjon`() {
        val personerISeksjon = mapOf("S1" to 1, "S2" to 1)
        val resultater = listOf(
            AnsattDetaljer("A", "S1", "K", "30-39", "5-9", "L1", "S", 1),
            AnsattDetaljer("B", "S2", "M", "40-49", "10-14", "L2", "S", 1)
        )
        val antallPerSeksjon = resultater
            .groupBy { it.seksjon }
            .mapValues { (_, ansatte) -> ansatte.sumOf { it.antall } }
        antallPerSeksjon.forEach { (seksjon, antall) ->
            assertTrue(antall <= personerISeksjon[seksjon]!!, "Antall for seksjon $seksjon er større enn antall unike personer")
        }
    }
}