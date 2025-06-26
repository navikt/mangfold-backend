package no.nav.modeller

import kotlinx.serialization.Serializable

@Serializable
data class SeksjonAlderKjonnAntall(
    val seksjon: String,
    val aldersgruppe: String,
    val kvinne: Long,
    val mann: Long
)

