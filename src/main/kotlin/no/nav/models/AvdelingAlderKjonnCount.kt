package no.nav.models

import kotlinx.serialization.Serializable

@Serializable
data class AvdelingAlderKjonnCount(
    val avdeling: String,
    val aldersgruppe: String,
    val kvinne: Long,
    val mann: Long
)