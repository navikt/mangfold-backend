package no.nav.models

import kotlinx.serialization.Serializable

@Serializable
data class SeksjonKjonnCount(
    val seksjon: String,
    val kvinne: Long,
    val mann: Long
)