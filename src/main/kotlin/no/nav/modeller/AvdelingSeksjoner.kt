package no.nav.modeller

import kotlinx.serialization.Serializable

@Serializable
data class AvdelingSeksjoner(
    val avdeling: String,
    val seksjoner: List<SeksjonKjonnData>
)

@Serializable
data class SeksjonKjonnData(
    val gruppe: String,
    val kjonnAntall: Map<String, Long>
)