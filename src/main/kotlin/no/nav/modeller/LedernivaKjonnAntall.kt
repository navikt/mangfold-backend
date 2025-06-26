package no.nav.modeller

import kotlinx.serialization.Serializable

@Serializable
data class LedernivaKjonnAntall(
    val lederNiva: String,
    val kvinne: Long,
    val mann: Long
)