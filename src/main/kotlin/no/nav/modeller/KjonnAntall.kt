package no.nav.modeller

import kotlinx.serialization.Serializable

@Serializable
data class KjonnAntall(
    val kjonn: String,
    val antall: Long
)