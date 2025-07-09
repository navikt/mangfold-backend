package no.nav.modeller

import kotlinx.serialization.Serializable

@Serializable
data class AnsattDetaljer(
    val avdeling: String,
    val seksjon: String,
    val kjonn: String,
    val aldersgruppe: String,
    val ansiennitetsgruppe: String,
    val lederniva: String,
    val stillingsnavn: String,
    val antall: Long
)