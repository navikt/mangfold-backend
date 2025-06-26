package no.nav.modeller

import kotlinx.serialization.Serializable

@Serializable
data class RolleKjonnAntall(
    val rolle: String,
    val kvinne: Long,
    val mann: Long
)

