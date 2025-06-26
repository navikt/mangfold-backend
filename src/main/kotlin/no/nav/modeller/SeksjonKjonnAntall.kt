package no.nav.modeller

import kotlinx.serialization.Serializable

@Serializable
data class SeksjonKjonnAntall(
    val seksjon: String,
    val kvinne: Long,
    val mann: Long
)