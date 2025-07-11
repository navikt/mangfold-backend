package no.nav.modeller

import kotlinx.serialization.Serializable

@Serializable
data class KjonnGruppeAntall(
    val gruppe: String,
    val kjonnAntall: Map<String, Long>,
    val erMaskert: Boolean
)
