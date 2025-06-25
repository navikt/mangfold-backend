package no.nav.models

import kotlinx.serialization.Serializable

@Serializable
data class SeksjonAlderKjonnCount(
    val seksjon: String,
    val aldersgruppe: String,
    val kvinne: Long,
    val mann: Long
)

