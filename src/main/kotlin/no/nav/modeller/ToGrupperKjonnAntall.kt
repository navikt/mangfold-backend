package no.nav.modeller

import kotlinx.serialization.Serializable

@Serializable
data class ToGrupperKjonnAntall(
    val gruppe1: String,
    val gruppe2: String,
    val kjonnAntall: Map<String, Long>
)