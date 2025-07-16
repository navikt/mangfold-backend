// package no.nav.services

// import org.junit.jupiter.api.Assertions.*
// import org.junit.jupiter.api.Test

// class AnsattDataQualityTest {

//     @Test
//     fun `datakvalitet - reell datakilde`() {
//         val prosjektId = System.getenv("BIGQUERY_PROSJEKTID") ?: "heda-prod-2664"
//         val data = hentAnsattDetaljer(prosjektId)

//         // 1. Ingen rad med seksjon == avdeling
//         assertTrue(data.all { it.seksjon != it.avdeling }, "Rader med seksjon == avdeling skal være filtrert ut.")

//         // 2. Antall er alltid positivt
//         assertTrue(data.all { it.antall > 0 }, "Alle antall skal være positive.")

//         // 3. Ingen tomme eller blanke verdier i viktige felter
//         data.forEach { ansatt ->
//             assertTrue(ansatt.avdeling.isNotBlank(), "Avdeling kan ikke være tom")
//             assertTrue(ansatt.seksjon.isNotBlank(), "Seksjon kan ikke være tom")
//             assertTrue(ansatt.kjonn.isNotBlank(), "Kjønn kan ikke være tom")
//             assertTrue(ansatt.aldersgruppe.isNotBlank(), "Aldersgruppe kan ikke være tom")
//             assertTrue(ansatt.ansiennitetsgruppe.isNotBlank(), "Ansiennitetsgruppe kan ikke være tom")
//             assertTrue(ansatt.lederniva.isNotBlank(), "Ledernivå kan ikke være tom")
//             assertTrue(ansatt.stillingsnavn.isNotBlank(), "Stillingsnavn kan ikke være tom")
//         }

//         // 4. Ingen duplikater på unik nøkkelkombinasjon (forebygger dobbelttelling)
//         val nøkkelSet = mutableSetOf<String>()
//         data.forEach { ansatt ->
//             val nøkkel = listOf(
//                 ansatt.avdeling,
//                 ansatt.seksjon,
//                 ansatt.kjonn,
//                 ansatt.aldersgruppe,
//                 ansatt.ansiennitetsgruppe,
//                 ansatt.lederniva,
//                 ansatt.stillingsnavn
//             ).joinToString("|")
//             assertTrue(nøkkelSet.add(nøkkel), "Dobbelttelling/duplikat funnet for kombinasjon: $nøkkel")
//         }

//         // 5. Sikre at summen av antall for alle rader er lik totalen av unike nøkkel-rader
//         // (Ekstra sjekk for dobbelttelling hvis antall brukes til summering)
//         val totalAntall = data.sumOf { it.antall }
//         val totalUnikeAntall = data.distinctBy { ansatt ->
//             listOf(
//                 ansatt.avdeling,
//                 ansatt.seksjon,
//                 ansatt.kjonn,
//                 ansatt.aldersgruppe,
//                 ansatt.ansiennitetsgruppe,
//                 ansatt.lederniva,
//                 ansatt.stillingsnavn
//             ).joinToString("|")
//         }.sumOf { it.antall }
//         assertEquals(totalAntall, totalUnikeAntall, "Dobbelttelling oppdaget: totalAntall=$totalAntall, totalUnikeAntall=$totalUnikeAntall")
//     }
// }